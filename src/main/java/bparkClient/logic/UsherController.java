package bparkClient.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import bparkClient.logic.UsherController;
import common.RequestType;
import common.entities.ParkingSession;
import common.entities.Subscriber;

/**
 * Controller responsible for operations performed by the usher,
 * such as registering new subscribers and retrieving lists of subscribers and active parking sessions.
 */
public class UsherController {
	private static final UsherController instance = new UsherController();

	private UsherController() {}

	/**
	 * Returns the singleton instance of the UsherController.
	 *
	 * @return the shared instance of UsherController
	 */
	public static UsherController getInstance() {
	    return instance;
	}

	/**
	 * Generate a unique subscriber code.
	 *
	 * @return a unique, formatted subscriber code string
	 */
	private String generateRandomSubId() {
        String uuidPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "SUB-" + uuidPart;
    }

	/**
	 * Registers a new subscriber by generating a unique subscriber ID
	 * and sending the subscriber details to the server.
	 * Tries up to 5 times in case of ID conflict.
	 *
	 * @param name the subscriber's name
	 * @param phone_number the subscriber's phone number
	 * @param email the subscriber's email
	 * @return the registered Subscriber object, or null if registration failed
	 */
    public static Subscriber registerSubscriber(String name, String phone_number, String email) {
        int attempts = 0;
        final int MAX_ATTEMPTS = 5;

        while (attempts < MAX_ATTEMPTS) {
            String subId = getInstance().generateRandomSubId();
            Subscriber sub = new Subscriber(subId, name, phone_number, email);
            Object res = Bparkclient.getInstance().sendAndWait(RequestType.REGISTER_SUBSCRIBER, sub);

            if (res instanceof Subscriber) {
                return (Subscriber) res;
            } else {
                System.out.println("Attempt " + (attempts + 1) + " failed — ID might exist. Retrying...");
                attempts++;
            }
        }

        System.err.println("Failed to register subscriber after " + MAX_ATTEMPTS + " attempts.");
        return null;
    }

	/**
	 * Retrieves a list of all subscribers from the server.
	 *
	 * @return a list of Subscriber objects, or an empty list if the response is invalid
	 */
    public List<Subscriber> createSubInfo() {
        Object res = Bparkclient.getInstance().sendAndWait(RequestType.GET_ALL_SUBSCRIBERS, null);
        return (res instanceof List<?>) ? (List<Subscriber>) res : new ArrayList<>();
    }

	/**
	 * Retrieves a list of all currently active parking sessions.
	 *
	 * @return a list of ParkingSession objects, or an empty list if the response is invalid
	 */
    public List<ParkingSession> createActiveParkingList() {
        Object res = Bparkclient.getInstance().sendAndWait(RequestType.GET_ACTIVE_SESSIONS, null);
        return (res instanceof List<?>) ? (List<ParkingSession>) res : new ArrayList<>();
    }
}
