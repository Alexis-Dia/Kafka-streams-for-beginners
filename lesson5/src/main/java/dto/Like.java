package dto;

import java.time.LocalDateTime;

public class Like {

    private final Gender gender;
    private final Integer boyId;
    private final Integer girlId;

    private final LocalDateTime eventTime;

    public Like(Gender gender, Integer boyId, Integer girlId, LocalDateTime eventTime) {
        this.gender = gender;
        this.boyId = boyId;
        this.girlId = girlId;
        this.eventTime = eventTime;
    }

    public Gender getGender() {
        return gender;
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
        return "Like{" +
                "gender=" + gender +
                ", boyId=" + boyId +
                ", girlId=" + girlId +
                ", eventTime=" + eventTime +
                '}';
    }
}
