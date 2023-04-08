//import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
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

public class FlinkKafkaApp {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("sentences5")
                .setGroupId("my-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        DataStream<String> lines = env.fromSource(source, WatermarkStrategy.noWatermarks(), "sentences5");

        //DataStream<String> modifiedStream = lines.flatMap(new TopicParser());
        DataStream<Like> modifiedStream = lines.flatMap(new LikeParser());


        DataStream<Match> stats = modifiedStream
                .keyBy(like -> like.getBoyId() + "_" + like.getGirlId())
                .process(new MatchFunction());

        stats.toString();

        modifiedStream.print();
        env.execute("FlinkKafkaApp");
        //DataStream<String> orderA = env.fromCollection(Arrays.asList("new", "new3"));
        //orderA.print();
    }

        public static class TopicParser implements FlatMapFunction<String, String> {
        @Override
        public void flatMap(String value, Collector<String> out) {
            for (String token : value.split("\\W")) {
                out.collect(token + "_");
            }
        }
    }

    public static class LikeParser implements FlatMapFunction<String, Like> {
        @Override
        public void flatMap(String value, Collector<Like> out) {
            String[] data = value.split("_");
            if (data.length != 3) {
                throw new RuntimeException("Unsupported format");
            }
            int boyId = Integer.valueOf(data[0]);
            int girlId = Integer.valueOf(data[1]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime eventTime = LocalDateTime.parse(data[2], formatter);
            Like like = new Like(boyId, girlId, eventTime);
            out.collect(like);
            //"12345_54321_2023-04-04 15:15"
        }
    }

}
