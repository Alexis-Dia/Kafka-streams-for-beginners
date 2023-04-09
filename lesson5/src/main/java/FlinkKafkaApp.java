//import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import dto.Gender;
import dto.Like;
import dto.Match;
import matchFunction.MatchFunction;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;

import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Test data:
 * "MALE_12345_54321_2023-04-04 15:15"
 * "MALE_12345_54322_2023-04-04 19:15"
 * "MALE_12345_54323_2023-04-04 15:15"
 * "FEMALE_12345_54321_2023-04-04 17:15"
 */
public class FlinkKafkaApp {
    private static final String UNSUPPORTED_FORMAT = "Unsupported format";
    private static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    private static final String TOPIC_NAME = "sentences14";
    private static final String SEPARATOR = "_";
    private static final String FLINK_KAFKA_APP = "FlinkKafkaApp";
    private static final String LOCALHOST = "localhost";
    private static final String PORT = "9092";
    private static final String MY_GROUP = "my-group";

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers(LOCALHOST + ":" + PORT)
                .setTopics(TOPIC_NAME)
                .setGroupId(MY_GROUP)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        DataStream<String> lines = env.fromSource(source, WatermarkStrategy.noWatermarks(), TOPIC_NAME);

        //DataStream<String> modifiedStream = lines.flatMap(new TopicParser());
        DataStream<Like> modifiedStream = lines.flatMap(new LikeParser());


        DataStream<Match> stats = modifiedStream
                .keyBy(like -> {
                    String keyBy = like.getGender().name().equals(Gender.MALE) ?
                            like.getBoyId() + SEPARATOR + like.getGirlId() :
                            like.getGirlId() + SEPARATOR + like.getBoyId();
                    System.out.println(keyBy);
                    return keyBy;
                })
                .process(new MatchFunction());

        stats.toString();

        modifiedStream.print();
        env.execute(FLINK_KAFKA_APP);
        //DataStream<String> orderA = env.fromCollection(Arrays.asList("new", "new3"));
        //orderA.print();
    }

        public static class TopicParser implements FlatMapFunction<String, String> {
        @Override
        public void flatMap(String value, Collector<String> out) {
            for (String token : value.split("\\W")) {
                out.collect(token + SEPARATOR);
            }
        }
    }

    public static class LikeParser implements FlatMapFunction<String, Like> {

        @Override
        public void flatMap(String value, Collector<Like> out) {
            String[] data = value.split(SEPARATOR);
            if (data.length != 4) {
                throw new RuntimeException(UNSUPPORTED_FORMAT);
            }
            Gender gender = Gender.valueOf(data[0]);
            int boyId = Integer.valueOf(data[1]);
            int girlId = Integer.valueOf(data[2]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM);
            LocalDateTime eventTime = LocalDateTime.parse(data[3], formatter);
            Like like = new Like(gender, boyId, girlId, eventTime);
            out.collect(like);
        }
    }

}
