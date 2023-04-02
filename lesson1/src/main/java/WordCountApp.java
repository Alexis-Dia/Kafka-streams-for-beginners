import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Arrays;
import java.util.Properties;

public class WordCountApp {

    private static final String topicName = "sentences";
    public static final String WORDS_WITH_COUNTS_TOPIC = "WordsWithCountsTopic";

    /**
     * WordCount hello-workd application from official Kafka example:
     *     https://kafka.apache.org/documentation/streams/
     * Also example of WordCount application:
     *     https://gist.github.com/fhussonnois/43cc27dd5e3b5143f574040d6f2d58c8
     * @param args topicName (Name of the Kafka topic to read)
     */
    public static void main(String[] args) {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "WordCount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream(topicName);
        textLines
                .flatMapValues((key, value) ->
                        Arrays.asList(value.toLowerCase()
                                .split(" ")))
                .groupBy((key, word) -> word)
                .count(Materialized.with(Serdes.String(), Serdes.Long()))
                .toStream()
                .to("word-count", Produced.with(Serdes.String(), Serdes.Long()));
        textLines.foreach((k, v) -> System.out.println("Key = " + k + " Value = " + v));

        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, props);

        System.out.println("Starting the stream");
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping the stream");
            streams.close();
        }));
    }
}
