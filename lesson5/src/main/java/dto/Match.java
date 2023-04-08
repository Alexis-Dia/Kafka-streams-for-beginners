package dto;

import java.time.LocalDateTime;

public class Match {

    private final Integer boyId;
    private final Integer girlId;

    private final LocalDateTime eventTime;

    public Match(Integer boyId, Integer girlId, LocalDateTime eventTime) {
        this.boyId = boyId;
        this.girlId = girlId;
        this.eventTime = eventTime;
    }

    public Integer getBoyId() {
        return boyId;
    }

    public Integer getGirlId() {
        return girlId;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    @Override
    public String toString() {
        return "Match{" +
                "boyId=" + boyId +
                ", girlId=" + girlId +
                ", eventTime=" + eventTime +
                '}';
    }
}
