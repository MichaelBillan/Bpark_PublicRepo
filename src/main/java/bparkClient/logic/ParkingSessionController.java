package bparkClient.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import common.RequestType;
import common.entities.ParkingSession;
import common.entities.Reservation;

/**
 * Controller responsible for managing parking sessions in the client.
 * Handles session creation, retrieval, validation, extension, and termination.
 */
public class ParkingSessionController {

    private static final ParkingSessionController instance = new ParkingSessionController();

    /**
     * Returns the singleton instance of the ParkingSessionController.
     * @return the singleton instance
     */
    public static ParkingSessionController getInstance() {
        return instance;
    }

    /**
     * Generates a unique parking code for a new parking session.
     * The format of the parking code is BP-YYYYMMDD-XXXX
     * YYYYMMDD is the current date,
     * XXXX is a 4-character upper case string derived from a UUID
     * Example: BP-20250623-9F3A
     * This method ensures uniqueness without requiring a database lookup.
     * @return a unique, formatted parking code string
     */
    public String generateParkingCode() {
        String datePart = LocalDate.now().toString().replace("-", "");
        String uuidPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return "BP-" + datePart + "-" + uuidPart;
    }

    /**
     * Creates a new parking session and returns the generated parking code.
     *
     * @param subId the subscriber's ID
     * @return the generated parking code if the session was created, otherwise null
     */
    public ParkingSession createSession(String subId) {
        String parkingCode = generateParkingCode();
        LocalDateTime startTime = LocalDateTime.now();
        int duration = 240;
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.GET_AVAILABLE_SPOT_ID, null);
        String spaceId = (response instanceof String) ? (String) response : null;
        if (spaceId == null) {
            System.err.println("No available parking spots found.");
            return null;
        }
        ParkingSession session = new ParkingSession(
                0,
                parkingCode,
                startTime,
                duration,
                null,
                ParkingSession.Status.ACTIVE,
                0,
                spaceId,
                subId
        );

        Object insertResponse = Bparkclient.getInstance().sendAndWait(RequestType.START_SESSION, session);
        if (insertResponse instanceof ParkingSession) {
            Bparkclient.getInstance().sendAndWait(RequestType.MARK_SPOT_OCCUPIED, session.getSpaceId());
            return session;
        } else {
            System.err.println("Failed to create parking session.");
            return null;
        }
    }

    /**
     * Retrieves the current duration (in minutes) of a parking session by its code.
     * If the session is still active, the duration is calculated based on start time.
     * If the session is completed, the stored duration is returned.
     *
     * @param parkingCode the unique parking code of the session
     * @return the duration in minutes, or -1 if not found or error occurred
     */
    public int getDurationForSession(String parkingCode) {
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.GET_DURATION_FOR_SESSION, parkingCode);
        if (response instanceof Integer duration) {
            return duration;
        } else {
            return -1;
        }
    }

    /**
     * Creates a parking session from a reservation.
     *
     * @param confirmationCode the reservation confirmation code
     * @return the generated parking code, or null if failed
     */
    public String createSessionFromReservation(String confirmationCode) {

        Object obj = Bparkclient.getInstance().sendAndWait(RequestType.GET_RESERVATION_BY_CODE, confirmationCode);
        if (!(obj instanceof Reservation reservation)) {
            System.err.println("No reservation found for code: " + confirmationCode);
            return null;
        }

        String parkingCode = generateParkingCode();

        ParkingSession session = new ParkingSession(
                0,
                parkingCode,
                LocalDateTime.now(),
                reservation.getDuration(),
                null,
                ParkingSession.Status.ACTIVE,
                0,
                reservation.getSpaceId(),
                reservation.getSubId()
        );

        Bparkclient.getInstance().sendAndWait(RequestType.START_SESSION, session);
        Bparkclient.getInstance().sendAndWait(RequestType.UPDATE_RESERVATION_STATUS_CONFIRMED, confirmationCode);
        Bparkclient.getInstance().sendAndWait(RequestType.MARK_SPOT_OCCUPIED, reservation.getSpaceId());

        return parkingCode;
    }

    /**
     * Retrieves a parking session by its parking code.
     *
     * @param parkingCode the code of the session to retrieve
     * @return the ParkingSession if found, otherwise null
     */
    public ParkingSession getSessionByParkingCode(String parkingCode) {
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.GET_SESSION_BY_PARKING_CODE, parkingCode);
        return (response instanceof ParkingSession) ? (ParkingSession) response : null;
    }

    /**
     * Requests to extend the duration of a parking session.
     *
     * @param parkingCode the parking code of the session to extend
     * @param additionalTime the additional time to add to the session (in minutes)
     * @return status code from server: 0 for success, or error code otherwise
     */
    public int extendSession(String parkingCode, int additionalTime) {
        Object[] payload = new Object[] { parkingCode, additionalTime };
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.EXTEND_SESSION, payload);
        return (int) response;
    }

    /**
     * Requests to end a parking session.
     *
     * @param parkingCode the parking code of the session to end
     */
    public void endSession(String parkingCode) {
        boolean ended = (boolean) Bparkclient.getInstance().sendAndWait(RequestType.END_SESSION, parkingCode);
        if (!ended) {
            System.err.println("Failed to end parking session. Spot will not be released.");
            return;
        }

        ParkingSession session = getSessionByParkingCode(parkingCode);
        if (session != null) {
            String spaceId = session.getSpaceId();
            System.out.println("space id:" + spaceId);
            ParkingSpaceController.getInstance().releaseSpot(spaceId);
        }
    }

    /**
     * Checks if the given parking code is valid and active in the system.
     *
     * @param parkingCode the parking code to validate
     * @return true if the code exists and is valid, otherwise false
     */
    public boolean validateParkingCode(String parkingCode) {
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.VALIDATE_PARKING_CODE, parkingCode);
        return (response instanceof Boolean) ? (Boolean) response : false;
    }

    /**
     * Retrieves the active parking code associated with a subscriber ID.
     *
     * @param subId the subscriber ID
     * @return the parking code of the active session, or null if not found
     */
    public String getActiveSessionBySubId(String subId) {
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.GET_PARKING_CODE_BY_SUBSCRIBER_ID, subId);
        String parkingCode = ((ParkingSession) response).getParkingCode();
        return parkingCode;
    }

    /**
     * Retrieves a list of late (overdue) parking sessions.
     *
     * @return a list of late ParkingSession objects
     */
    public List<ParkingSession> getLateSessions() {
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.GET_LATE_SESSIONS, null);
        if (response instanceof List<?>) {
            @SuppressWarnings("unchecked")
            List<ParkingSession> sessions = (List<ParkingSession>) response;
            return sessions;
        }
        return List.of();
    }

    /**
     * Retrieves the current expected pickup time for a given parking code.
     *
     * @param parkingCode the parking code
     * @return the pickup time as LocalDateTime, or null if not available
     */
    public LocalDateTime currentPickupTime(String parkingCode) {
        Object response = Bparkclient.getInstance().sendAndWait(RequestType.GET_CURRENT_PICKUP_TIME, parkingCode);
        if (response instanceof LocalDateTime) {
            return (LocalDateTime) response;
        }
        return null;
    }

}
