package ch.giuntini.coworkingspace.model;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;

public class UpdatingBooking {
    
    @Future
    private LocalDateTime startTime;

    @Future
    private LocalDateTime endTime;

    private User booker;

    private Section section;

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

    public User getBooker() {
        return this.booker;
    }

    public void setBooker(User booker) {
        this.booker = booker;
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
