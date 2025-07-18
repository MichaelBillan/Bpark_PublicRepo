package bparkClient.logic;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import common.RequestType;
import common.entities.ParkingSession;
import common.entities.Reservation;
import common.entities.Subscriber;

/**
 * Handles logic related to subscribers, including identification, contact updates,
 * reservation history retrieval, and parking reservations.
 */
public class SubscriberController {

    /** Singleton instance of SubscriberController. */
    private static final SubscriberController instance = new SubscriberController();

    /** Private constructor to enforce singleton pattern. */
    private SubscriberController() {}

    /**
     * Returns the singleton instance of this controller.
     *
     * @return the instance of SubscriberController
     */
    public static SubscriberController getInstance() {
        return instance;
    }

    /**
     * Identifies subscriber by subId.
     *
     * @param subId the subscriber ID
     * @return true if the subscriber exists, false otherwise
     */
    public boolean identifySubscriberViaSubId(String subId) {
        Object result = Bparkclient.getInstance().sendAndWait(RequestType.GET_SUBSCRIBER_BY_ID, subId);
        return (result instanceof Subscriber);
    }

    /**
     * Sends a SCAN_VIA_TAG request to the server and waits for a randomly selected subscriber ID.
     *
     * @return the subscriber ID if successful, or null if failed
     */
    public String scanViaTag() {
        try {
            Object response = Bparkclient.getInstance().sendAndWait(RequestType.SCAN_VIA_TAG, null);

            if (response instanceof String subId && subId != null && !subId.isBlank()) {
                return subId;
            }

        } catch (Exception e) {
            System.err.println("scanViaTag failed: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves subscriber details from the server.
     *
     * @param subId the subscriber ID
     * @return a Subscriber object if found, otherwise null
     */
    public Subscriber getSubDetails(String subId) {
        Object result = Bparkclient.getInstance().sendAndWait(RequestType.GET_SUBSCRIBER_BY_ID, subId);
        if (result instanceof Subscriber) {
            return (Subscriber) result;
        }
        return null;
    }

    /**
     * Changes the phone number of the subscriber.
     *
     * @param subId          subscriber ID
     * @param newPhoneNumber new phone number
     */
    public void changePhone(String subId, String newPhoneNumber) {
        String[] payload = new String[] { subId, newPhoneNumber };
        Bparkclient.getInstance().sendAndWait(RequestType.UPDATE_SUBSCRIBER_PHONE, payload);
    }

    /**
     * Changes the email of the subscriber.
     *
     * @param subId    subscriber ID
     * @param newEmail new email
     */
    public void changeEmail(String subId, String newEmail) {
        String[] payload = new String[] { subId, newEmail };
        Bparkclient.getInstance().sendAndWait(RequestType.UPDATE_SUBSCRIBER_EMAIL, payload);
    }

    /**
     * Retrieves the historical parking sessions for a specific subscriber.
     * Allows the subscriber to view all past parking sessions.
     *
     * @param subId the ID of the subscriber whose history is requested
     * @return a list of ParkingSession objects associated with the subscriber,
     *         or an empty list if none are found
     */
    public List<ParkingSession> generateParkingHistory(String subId) {
        Object result = Bparkclient.getInstance().sendAndWait(RequestType.GET_PARKING_SESSION_HISTORY, subId);
        if (result instanceof List<?>) {
            List<?> list = (List<?>) result;
            if (!list.isEmpty() && list.get(0) instanceof ParkingSession) {
                @SuppressWarnings("unchecked")
                List<ParkingSession> sessions = (List<ParkingSession>) result;
                return sessions;
            }
        }
        return Collections.emptyList();
    }

    /**
     * Retrieves the historical reservation records for a specific subscriber.
     * Allows the subscriber to view all past reservation attempts and statuses.
     *
     * @param subId the ID of the subscriber whose reservation history is requested
     * @return a list of Reservation objects associated with the subscriber,
     *         or an empty list if none are found
     */
    public List<Reservation> generateReservationHistory(String subId) {
        Object result = Bparkclient.getInstance().sendAndWait(RequestType.GET_RESERVATION_HISTORY, subId);
        if (result instanceof List<?>) {
            List<?> list = (List<?>) result;
            if (!list.isEmpty() && list.get(0) instanceof Reservation) {
                @SuppressWarnings("unchecked")
                List<Reservation> reservations = (List<Reservation>) result;
                return reservations;
            }
        }
        return Collections.emptyList();
    }
    /**
     * Attempts to reserve a parking spot for the subscriber, enforcing advance notice and availability rules.
     * The reservation must be made at least 24 hours in advance, no more than 7 days ahead,
     * and only if at least 40% of the total spots are available on the requested date.
     *
     * @param subId          the subscriber ID
     * @param startDateTime  the desired start time of the reservation
     * @param endDateTime    the desired end time of the reservation
     * @return a confirmation code string if successful, or a descriptive error message
     */
    public String reserveParking(String subId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        LocalDateTime now = LocalDateTime.now();

        if (startDateTime.isBefore(now.plusHours(24)) || startDateTime.isAfter(now.plusDays(7))) {
            return "Pick valid time";
        }

        LocalTime startAllowed = LocalTime.of(7, 0);
        LocalTime endAllowed = LocalTime.of(21, 0);

        LocalTime startTime = startDateTime.toLocalTime();
        LocalTime endTime = endDateTime.toLocalTime();

        if (startTime.isBefore(startAllowed) || startTime.isAfter(endAllowed) ||
            endTime.isBefore(startAllowed) || endTime.isAfter(endAllowed)) {
            return "Reservation hours must be between 07:00 and 21:00";
        }

        String dateStr = startDateTime.toLocalDate().toString();
        Object reservedCountObj = Bparkclient.getInstance().sendAndWait(RequestType.GET_RESERVED_SPOTS_COUNT, dateStr);
        int reservedCount = (reservedCountObj instanceof Integer) ? (Integer) reservedCountObj : -1;
        if (reservedCount == -1) return "ERROR";

        int totalSpots = 10;
        double availablePercent = ((totalSpots - reservedCount) * 100.0) / totalSpots;
        if (availablePercent <= 60.0) {
            return "The parking lot is full";
        }

        String confirmationCode = ReservationController.getInstance().createReservation(subId, startDateTime, endDateTime);
        if (confirmationCode != null && !confirmationCode.isEmpty())
            return confirmationCode;
        else
            return "System error";
    }

    /**
     * Requests the server to send the parking code via email to the current subscriber.
     *
     * @return a status message indicating success or failure
     */
    public String sendParkingCodeByEmail() {
        String subId = AppSessionData.getInstance().getSubscriberCode();
        if (subId == null || subId.isEmpty()) {
            return "Subscriber ID not available.";
        }

        Object response = Bparkclient.getInstance().sendAndWait(RequestType.SEND_PARKING_CODE_VIA_EMAIL, subId);

        if (response instanceof Boolean success && success) {
            return "The code has been sent to your email.";
        } else {
            return "Failed to send the code. Please try again.";
        }
    }
}
