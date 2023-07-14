package ch.giuntini.coworkingspace.model;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;

import org.hibernate.validator.constraints.Length;

public class UpdatingBooking {
    
    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;

    private Section section;

    @Length(max = 8191)
    private String wish;

    @Length(max = 8191)
    private String wishFeedback;

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getWishFeedback() {
        return wishFeedback;
    }

    public void setWishFeedback(String wishFeedback) {
        this.wishFeedback = wishFeedback;
    }
}
