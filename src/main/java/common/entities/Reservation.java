package common.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a reservation request made by a subscriber for a specific parking space.
 */
public class Reservation implements Serializable{
    private final String resId;
    private final String confirmationCode;
    private ReservationStatus reservationStatus;
    private final LocalDateTime startTime;
    private int duration; // in minutes
    private final String subId;
    private final String spaceId;

    public enum ReservationStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

    public Reservation(String resId, String confirmationCode, ReservationStatus reservationStatus,
                       LocalDateTime startTime, int duration, String subId, String spaceId) {
        this.resId = resId;
        this.confirmationCode = confirmationCode;
        this.reservationStatus = reservationStatus;
        this.startTime = startTime;
        this.duration = duration;
        this.subId = subId;
        this.spaceId = spaceId;
    }

    public String getResId() {
        return resId;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }

    public String getSubId() {
        return subId;
    }

    public String getSpaceId() {
        return spaceId;
    }

    @Override
    public String toString() {
        return "Reservation [resId=" + resId +
               ", confirmationCode=" + confirmationCode +
               ", reservationStatus=" + reservationStatus +
               ", startTime=" + startTime +
               ", endTime=" + getEndTime() +
               ", duration=" + duration +
               ", subId=" + subId +
               ", spaceId=" + spaceId + "]";
    }
}
