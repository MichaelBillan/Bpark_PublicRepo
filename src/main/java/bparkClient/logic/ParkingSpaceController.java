package bparkClient.logic;

import common.Request;
import java.time.LocalDateTime;

import bparkServer.db.DataBaseController;
import common.RequestType;
import common.entities.ParkingSession;

/**
 * Controls parking space operations such as creating sessions,
 * checking availability, and managing extensions.
 */
public class ParkingSpaceController {

    /**
     * The singleton instance of ParkingSpaceController class, shared across all classes 
     * that require access to parking space management functionality.
     */
    private static final ParkingSpaceController instance = new ParkingSpaceController();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private ParkingSpaceController() {}

    /**
     * Returns the singleton instance of ParkingSpaceController.
     * @return the singleton instance
     */
    public static ParkingSpaceController getInstance() {
        return instance;
    }

    /**
     * Sends a request to release a specific parking spot identified by its space ID.
     * This method communicates with the server to update the status of the given 
     * parking spot to indicate that it is no longer in use.
     * 
     * @param spaceId the unique identifier of the parking space to be released
     */
    public void releaseSpot(String spaceId) {
        Bparkclient.getInstance().sendAndWait(RequestType.RELEASE_SPOT, spaceId);
    }

    /**
     * Checks and returns a message about the number of available parking spots.
     * Sends a request to the server and interprets the response.
     * 
     * @return a message indicating how many parking spots are available,
     *         or an error message if something went wrong
     */
    public String checkAvailableSpots() {
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.GET_AVAILABLE_SPOTS, null);
        int available = (response instanceof Integer) ? (int) response : -1;

        if (available == -1) {
            System.err.println("Error: Failed to retrieve available parking spots.");
            return "System Error Occurred!";
        } else if (available == 0) {
            return "There are no available parking spots!";
        } else {
            return "There are " + available + " parking spots";
        }
    }

    /**
     * Checks if there are any available parking spots.
     * 
     * @return true if there are available spots, false otherwise
     */
    public boolean hasAvailableSpots() {
        Object parkResponse = Bparkclient.getInstance().sendAndWait(RequestType.GET_AVAILABLE_SPOTS, null);
        return (parkResponse instanceof Integer && ((Integer) parkResponse) > 0);
    }
}
