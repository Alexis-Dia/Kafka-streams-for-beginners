package com;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

/**
 * 	About Store in Kafka-stream:
 * 		Is Kafka Stream StateStore global over all instances or just local? https://stackoverflow.com/questions/40274884/is-kafka-stream-statestore-global-over-all-instances-or-just-local
 * 		How to add a custom StateStore to the Kafka Streams DSL processor? - https://stackoverflow.com/questions/40221539/how-to-add-a-custom-statestore-to-the-kafka-streams-dsl-processor
 */
public class StoreLesson {

    private static final String SOME_INPUT_TOPIC = "some-input-topic";
    private static final String SOME_OUTPUT_TOPIC = "some-output-topic";
    private static final String SOME_STORE = "someStore";

    public static void main(String[] args) {

        final StreamsBuilder builder = new StreamsBuilder();

        // get and set properties
        RtdStreamProperties rtdStreamProperties = new RtdStreamProperties();
        Properties props = rtdStreamProperties.getProperties();
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // create key/value store for bus positions
        final StoreBuilder<KeyValueStore<String, String>> storeBuilder = Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(SOME_STORE),
                Serdes.String(),
                Serdes.serdeFrom(Serdes.String().serializer(), Serdes.String().deserializer()));

        builder.addStateStore(storeBuilder);

        // stream positions from the rtd-bus-position topic
        final KStream<String, String> stream = builder.stream(SOME_INPUT_TOPIC);

        // calculate something
        final KStream<String, String> streamEnriched =
                stream.transform(
                        new MyTransformerSupplier(SOME_STORE),
                        SOME_STORE
                );

        streamEnriched
                .mapValues(value -> {
                    System.out.println("Value in the enriched stream " + value);
                    return value;
                })
                .to(SOME_OUTPUT_TOPIC);

        stream.foreach((k, v) -> System.out.println("Key = " + k + " Value = " + v));

        // run it
        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        streams.cleanUp();
        streams.start();

        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    }
}
