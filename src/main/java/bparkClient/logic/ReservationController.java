package bparkClient.logic;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import common.RequestType;
import common.entities.Reservation;

/**
 * Handles creation and validation of parking reservations.
 * Responsible for interacting with the server to create, validate,
 * and manage reservation data.
 */
public class ReservationController {

    /**
     * Singleton instance of ReservationController.
     */
	private static final ReservationController instance = new ReservationController();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private ReservationController() {}

    /**
     * Gets the singleton instance of ReservationController.
     *
     * @return the shared instance of the controller
     */
    public static ReservationController getInstance() {
        return instance;
    }

    /**
     * Creates a reservation and saves it in the database.
     * This method first requests an available parking spot, then reserves it,
     * and finally inserts the reservation into the database.
     *
     * @param subId the subscriber's ID
     * @param startDatetime the start date and time of the reservation
     * @param endDateTime the end date and time of the reservation
     * @return the confirmation code if reservation was successful, or null otherwise
     */
    public String createReservation(String subId, LocalDateTime startDatetime, LocalDateTime endDateTime) {
        int duration = (int) Duration.between(startDatetime, endDateTime).toMinutes();
        LocalDate date = startDatetime.toLocalDate();
        Object spotIdObj = Bparkclient.getInstance().sendAndWait(RequestType.GET_AVAILABLE_SPOT_ID_NONRESERVED, date);
        System.out.println("spotId: " + spotIdObj);
        if (!(spotIdObj instanceof String spotId) || spotId == null)
            return null;

        String uuidStr = UUID.randomUUID().toString().replace("-", "");
        String resId = "Res-" + uuidStr.substring(0, 8);
        System.out.println("resId: " + resId);
        String confirmationCode = "Con-" + uuidStr.substring(0, 8);
        System.out.println("confirmationCode: " + confirmationCode);
        Reservation reservation = new Reservation(
                resId,
                confirmationCode,
                Reservation.ReservationStatus.PENDING,
                startDatetime,
                duration,
                subId,
                spotId
        );

        Object result = Bparkclient.getInstance().sendAndWait(RequestType.CREATE_RESERVATION, reservation);
        System.out.println("result: " + result);
        if (result instanceof Boolean && (Boolean) result) {
            return confirmationCode;
        }
        return null;
    }

    /**
     * Checks if a reservation is valid for check-in.
     * If more than 15 minutes have passed since the start time,
     * the reservation is cancelled and the spot is released.
     *
     * @param confirmationCode the reservation's confirmation code
     * @return code indicating reservation status:
     *         1 - invalid confirmation code,
     *         2 - expired reservation,
     *         3 - valid reservation
     */
    public int isReservationValid(String confirmationCode) {
        /*
         * 1. "Incorrect Confirmation Code"
         * 2. "Reservation is Cancelled"
         * 3. Valid reservation
         */

        Object obj = Bparkclient.getInstance().sendAndWait(RequestType.GET_RESERVATION_BY_CODE, confirmationCode);
        if (!(obj instanceof Reservation reservation)) {
            return 1;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = reservation.getStartTime();

        Duration diff = Duration.between(startTime, now);

        /*
         * For future activation a ready to use functionality:
         * Allow reservation activation only between 20 minutes before and 15 minutes after reserved start time.
         * 
         * long minutesFromStart = Duration.between(startTime, now).toMinutes();
         * if (minutesFromStart < -20) {
         * 		// Too early
         * 		return 4;
         * }
         */

        if (diff.toMinutes() > 15) {
            Object result = Bparkclient.getInstance().sendAndWait(RequestType.CANCEL_RESERVATION, reservation.getResId());
            Bparkclient.getInstance().sendAndWait(RequestType.RELEASE_SPOT, reservation.getSpaceId());
            return 2;
        }

        return 3;
    }

    /**
     * Checks if the subscriber has an active reservation.
     *
     * @param subCode the subscriber code
     * @return true if there is an active reservation, false otherwise
     */
    public boolean hasActiveReservation(String subCode) {
        Object resResponse = Bparkclient.getInstance().sendAndWait(RequestType.HAS_RESERVATION, subCode);
        return (resResponse instanceof Boolean && (Boolean) resResponse);
    }
}
