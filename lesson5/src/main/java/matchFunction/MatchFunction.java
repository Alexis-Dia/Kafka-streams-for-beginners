package matchFunction;

import dto.Like;
import dto.Match;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * https://stackoverflow.com/questions/72139591/apache-flink-stream-event-delay
 */
public class MatchFunction extends KeyedProcessFunction<String, Like, Match> {

    ValueState<Boolean> liked;

    @Override
    public void open(Configuration parameters) {
        liked = getRuntimeContext().getState(new ValueStateDescriptor<>("like", Boolean.class));
    }

    @Override
    public void processElement(Like like, KeyedProcessFunction<String, Like, Match>.Context context, Collector<Match> out) throws Exception {
        Boolean matched = liked.value();
        if (matched != null) {
            liked.clear();
            context.timerService().deleteEventTimeTimer(like.getEventTime().toInstant(ZoneOffset.UTC).toEpochMilli());
            Match match = new Match(like.getBoyId(), like.getGirlId(), LocalDateTime.now());
            out.collect(match);
            System.out.println("Match = " + match);
        } else {
            liked.update(true);
            context.timerService().registerEventTimeTimer(like.getEventTime().toInstant(ZoneOffset.UTC).toEpochMilli() + Duration.ofMinutes(1l).toMillis());
        }
    }

    @Override
    public void onTimer(long timeStamp, OnTimerContext ctx, Collector<Match> out) throws IOException {
        if (liked.value() != null) {
            liked.clear();
        }
    }
}
