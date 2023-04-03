import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;

import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class FlinkKafkaApp {

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        KafkaSource<String> source = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("sentences")
                .setGroupId("my-group")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .build();
        DataStream<String> lines = env.fromSource(source, WatermarkStrategy.noWatermarks(), "sentences");

        DataStream<String> modifiedStream = lines.flatMap(new TopicParser());
        modifiedStream.print();
        env.execute("FlinkKafkaApp");
        //DataStream<String> orderA = env.fromCollection(Arrays.asList("new", "new3"));
        //orderA.print();
    }

        public static class TopicParser implements FlatMapFunction<String, String> {
        @Override
        public void flatMap(String value, Collector<String> out) {
            //out.collect(value + "_");
            for (String token : value.split("\\W")) {
                out.collect(token + "_");
            }
        }
    }

}
