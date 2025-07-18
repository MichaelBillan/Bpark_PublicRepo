package common.entities;

/**
 * Represents a parking lot manager who is also an usher with extended permissions.
 */
public class ParkingLotManager extends Usher {
    private final String managerId;

    public ParkingLotManager(String name, String phoneNumber, String email, String usherId, String managerId) {
        super(name, phoneNumber, email, usherId);
        this.managerId = managerId;
    }

    public String getManagerId() {
        return managerId;
    }

    @Override
    public String toString() {
        return "ParkingLotManager [managerId=" + managerId + ", " + super.toString() + "]";
    }
}
