package com;

import io.confluent.developer.avro.TicketSale;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.WindowedSerdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;

@Component
@PropertySource("classpath:application.properties")
public class AvroAggregatingCountReader {

    private static final String STREAMS_SHUTDOWN_HOOK = "streams-shutdown-hook";
    private static final String BASE_PACKAGE = "com";
    private static final String OUTPUT_TOPIC_NAME = "output.topic.name";
    private static final String OUTPUT_TOPIC_PARTITIONS = "output.topic.partitions";
    private static final String OUTPUT_TOPIC_REPLICATION_FACTOR = "output.topic.replication.factor";

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${application.id}")
    private String applicationId;

    @Value("${schema.registry.url}")
    private String schemaRegistryUrl;

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

    private Topology buildTopology(Properties envProps,
                                  final SpecificAvroSerde<TicketSale> ticketSaleSerde) {
        final StreamsBuilder builder = new StreamsBuilder();

        final String outputTopic = outputTopicName;

        builder.stream(outputTopic, Consumed.with(WindowedSerdes.timeWindowedSerdeFrom(String.class), Serdes.Long()))
                // Set key to title and value to ticket value
                .foreach((key, value) -> {
                    System.out.println("key = " + key + ", value = " + value);
                });
        Topology build = builder.build();

        return build;
    }

    private void runRecipe() throws IOException {
        Properties envProps = new Properties();

        envProps.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        envProps.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        envProps.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);

        envProps.put(OUTPUT_TOPIC_NAME, outputTopicName);
        envProps.put(OUTPUT_TOPIC_PARTITIONS, outputTopicPartition);
        envProps.put(OUTPUT_TOPIC_REPLICATION_FACTOR, outputTopicReplicationFactor);
        //envProps.put("enable.auto.commit", "false");
        //envProps.put(AUTO_OFFSET_RESET_CONFIG, "earliest");


        Properties streamProps = this.buildStreamsProperties(envProps);

        Topology topology = this.buildTopology(envProps, this.ticketSaleSerde(envProps));

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
        context.registerBean(AvroAggregatingCountReader.class);
        context.refresh();
        context.getBean(AvroAggregatingCountReader.class).runRecipe();
    }
}