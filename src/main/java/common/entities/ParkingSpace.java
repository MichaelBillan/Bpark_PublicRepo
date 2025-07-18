package common.entities;

import java.io.Serializable;

/**
 * Represents a parking space in the system.
 * Contains an identifier and its current status.
 */
public class ParkingSpace implements Serializable{
    private final String spaceId;
    private Status status;

    public enum Status {
        AVAILABLE,
        RESERVED,
        OCCUPIED
    }

    public ParkingSpace(String spaceId, Status status) {
        this.spaceId = spaceId;
        this.status = status;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ParkingSpace [spaceId=" + spaceId + ", status=" + status + "]";
    }
}
