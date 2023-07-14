package ch.giuntini.coworkingspace.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(readOnly = true)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "application_user_id", nullable = false)
    private User booker;

    @Column(nullable = false, length = 8)
    @ColumnDefault(value = "'PENDING'")
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Column(nullable = false, length = 9)
    @ColumnDefault(value = "'UNDEFINED'")
    @Enumerated(EnumType.STRING)
    private Section section;

    @Column(length = 8191)
    private String wish;

    @Column(length = 8191)
    private String wishFeedback;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public BookingStatus getStatus() {
        return this.status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
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

    public static Booking ofCreatingBooking(CreatingBooking creatingBooking) {
        Booking booking = new Booking();
        booking.setStartTime(booking.getStartTime());
        booking.setEndTime(creatingBooking.getEndTime());
        booking.setSection(creatingBooking.getSection());
        booking.setWish(creatingBooking.getWish());
        return booking;
    }
}
