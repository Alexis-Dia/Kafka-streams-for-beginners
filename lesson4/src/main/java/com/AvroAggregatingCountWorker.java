package com;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import io.confluent.developer.avro.TicketSale;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Component
@PropertySource("classpath:application.properties")
public class AvroAggregatingCountWorker {

    private static final String STREAMS_SHUTDOWN_HOOK = "streams-shutdown-hook";
    private static final String BASE_PACKAGE = "com";
    private static final String INPUT_TOPIC_NAME = "input.topic.name";
    private static final String INPUT_TOPIC_PARTITIONS = "input.topic.partitions";
    private static final String INPUT_TOPIC_REPLICATION_FACTOR = "input.topic.replication.factor";
    private static final String OUTPUT_TOPIC_NAME = "output.topic.name";
    private static final String OUTPUT_TOPIC_PARTITIONS = "output.topic.partitions";
    private static final String OUTPUT_TOPIC_REPLICATION_FACTOR = "output.topic.replication.factor";

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${application.id}")
    private String applicationId;

    @Value("${schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${input.topic.name}")
    private String inputTopicName;

    @Value("${input.topic.partitions}")
    private String inputTopicPartition;

    @Value("${input.topic.replication.factor}")
    private String inputTopicReplicationFactor;

    @Value("${output.topic.name}")
    private String outputTopicName;

    @Value("${output.topic.partitions}")
    private String outputTopicPartition;

    @Value("${output.topic.replication.factor}")
    private String outputTopicReplicationFactor;

    private Properties buildStreamsProperties(Properties envProps) {
        Properties props = new Properties();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);

        return props;
    }

    private SpecificAvroSerde<TicketSale> ticketSaleSerde(final Properties envProps) {
        final SpecificAvroSerde<TicketSale> serde = new SpecificAvroSerde<>();
        Map<String, String> config = new HashMap<>();
        config.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        serde.configure(config, false);
        return serde;
    }

    /**
     * https://coderoad.ru/56833511/-WindowedBy-Count-KStream-%D0%B2%D1%8B%D0%B1%D1%80%D0%B0%D1%81%D1%8B%D0%B2%D0%B0%D0%B5%D1%82-%D0%B8%D1%81%D0%BA%D0%BB%D1%8E%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-StreamsException
     */
    private Topology exampleOfCount(Properties envProps,
                                    final SpecificAvroSerde<TicketSale> ticketSaleSerde) {
        final StreamsBuilder builder = new StreamsBuilder();

        builder.stream(inputTopicName, Consumed.with(Serdes.String(), ticketSaleSerde))
                .map((k, v) -> new KeyValue<>(v.getTitle().toString(), v.getTicketTotalValue()))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .windowedBy(TimeWindows.of(Duration.ofMinutes(2)))
                //.windowedBy(SessionWindows.with(Duration.ofMinutes(2)).grace(Duration.ofMinutes(2)))
                //.windowedBy(SessionWindows.with(Duration.ofMinutes(2)))
                .count(Materialized.with(Serdes.String(), Serdes.Long()))
                .toStream()
                .to(outputTopicName, Produced.with(WindowedSerdes.sessionWindowedSerdeFrom(String.class), Serdes.Long()));

        Topology build = builder.build();
        return build;
    }

    private void createTopics() throws InterruptedException {
        Map<String, Object> config = new HashMap<>();
        config.put("bootstrap.servers", bootstrapServers);
        AdminClient client = AdminClient.create(config);

        Thread.sleep(500);
        client.deleteTopics(Arrays.asList(inputTopicName));
        Thread.sleep(500);
        client.deleteTopics(Arrays.asList(outputTopicName));

        List<NewTopic> topics1 = new ArrayList<>();
        List<NewTopic> topics2 = new ArrayList<>();
        topics1.add(new NewTopic(inputTopicName,
                Integer.parseInt(inputTopicPartition),
                Short.parseShort(inputTopicReplicationFactor)));
        topics2.add(new NewTopic(
                outputTopicName,
                Integer.parseInt(outputTopicPartition),
                Short.parseShort(outputTopicReplicationFactor)));

        Thread.sleep(500);
        client.createTopics(topics1);
        Thread.sleep(500);
        client.createTopics(topics2);
        client.close();
    }

    private void runRecipe() throws IOException {
        Properties envProps = new Properties();

        envProps.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        envProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        envProps.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

        envProps.put(INPUT_TOPIC_NAME, inputTopicName);
        envProps.put(INPUT_TOPIC_PARTITIONS, inputTopicPartition);
        envProps.put(INPUT_TOPIC_REPLICATION_FACTOR, inputTopicReplicationFactor);

        envProps.put(OUTPUT_TOPIC_NAME, outputTopicName);
        envProps.put(OUTPUT_TOPIC_PARTITIONS, outputTopicPartition);
        envProps.put(OUTPUT_TOPIC_REPLICATION_FACTOR, outputTopicReplicationFactor);

        Properties streamProps = this.buildStreamsProperties(envProps);

        Topology topology = this.exampleOfCount(envProps, this.ticketSaleSerde(envProps));

        try {
            this.createTopics();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final KafkaStreams streams = new KafkaStreams(topology, streamProps);
        final CountDownLatch latch = new CountDownLatch(1);

        // Attach shutdown handler to catch Control-C.
        Runtime.getRuntime().addShutdownHook(new Thread(STREAMS_SHUTDOWN_HOOK) {
            @Override
            public void run() {

                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.cleanUp();
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //context.scan(BASE_PACKAGE);
        context.registerBean(AvroAggregatingCountWorker.class);
        context.refresh();
        context.getBean(AvroAggregatingCountWorker.class).runRecipe();
    }
}