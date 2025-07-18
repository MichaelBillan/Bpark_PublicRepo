package common.entities;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents a parking session for a subscriber.
 * The endTime is computed from startTime + duration (in minutes).
 */
public class ParkingSession implements Serializable{
    private final int sessionId;
    private final String parkingCode;
    private final LocalDateTime startTime;
    private int duration; // in minutes
    private LocalDateTime lastExtensionTime;
    private Status status;
    private int extensionCount;
    private final String spaceId;   // corresponds to spot_id
    private final String subId;     // corresponds to subscriber_id

    public enum Status {
        RESERVED, ACTIVE, COMPLETED, CANCELLED
    }

    public ParkingSession(int sessionId, String parkingCode, LocalDateTime startTime, int duration,
                          LocalDateTime lastExtensionTime, Status status, int extensionCount,
                          String spaceId, String subId) {
        this.sessionId = sessionId;
        this.parkingCode = parkingCode;
        this.startTime = startTime;
        this.duration = duration;
        this.lastExtensionTime = lastExtensionTime;
        this.status = status;
        this.extensionCount = extensionCount;
        this.spaceId = spaceId;
        this.subId = subId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getParkingCode() {
        return parkingCode;
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

    public LocalDateTime getLastExtensionTime() {
        return lastExtensionTime;
    }

    public void setLastExtensionTime(LocalDateTime lastExtensionTime) {
        this.lastExtensionTime = lastExtensionTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getExtensionCount() {
        return extensionCount;
    }

    public void setExtensionCount(int extensionCount) {
        this.extensionCount = extensionCount;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public String getSubId() {
        return subId;
    }

    @Override
    public String toString() {
        return "ParkingSession [sessionId=" + sessionId +
               ", parkingCode=" + parkingCode +
               ", startTime=" + startTime +
               ", endTime=" + getEndTime() +
               ", lastExtensionTime=" + lastExtensionTime +
               ", status=" + status +
               ", duration=" + duration +
               ", extensionCount=" + extensionCount +
               ", spaceId=" + spaceId +
               ", subId=" + subId + "]";
    }
}
