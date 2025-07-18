package bparkServer.db;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import bparkServer.db.DBConnection;
import bparkServer.notifications.NotificationController;
import common.RequestType;
import common.entities.ParkingSession;
import common.entities.ParkingSession.Status;
import common.entities.Report;
import common.entities.Reservation;
import common.entities.Subscriber;

public class DataBaseController {

	/**
	 * Handles incoming requests based on the request type and provided payload.
	 * Routes each request to the appropriate method for processing.
	 *
	 * @param type    the type of request to handle
	 * @param payload the data associated with the request
	 * @return the result of the request processing, or null if unhandled
	 */
    public static Object handleRequest(RequestType type, Object payload) {

        switch (type) {
        	
            case REGISTER_SUBSCRIBER:
                if (payload instanceof Subscriber)
                    return registerSubscriber((Subscriber) payload);
                break;
                
            case GET_SUBSCRIBER_BY_ID:
                if (payload instanceof String subId)
                    return getSubscriberById(subId);
                break;
                
            case UPDATE_SUBSCRIBER_PHONE:
                if (payload instanceof String[] arr && arr.length == 2) {
                    return updateSubscriberPhone(arr[0], arr[1]);
                }
                break;
            case UPDATE_SUBSCRIBER_EMAIL:
                if (payload instanceof String[] arr && arr.length == 2) {
                    return updateSubscriberEmail(arr[0], arr[1]);
                }
                break;
                
            case GET_ALL_SUBSCRIBERS:{
                return getAllSubscribers();
            }
                
            case GET_ACTIVE_SESSIONS:{
                return getActiveSessions();
            }
                
            case VALIDATE_PARKING_CODE:
                if (payload instanceof String parkingCode) {
                    return validateParkingCode(parkingCode);
                }
                break;
                
            case GET_CURRENT_PICKUP_TIME:
                if (payload instanceof String parkingCode) {
                    return currentPickupTime(parkingCode);
                }
                break;
                
            case START_SESSION:
                if (payload instanceof ParkingSession session) {
                    return startSession(session);
                }
                break;
                
            case GET_SESSION_BY_PARKING_CODE:
                if (payload instanceof String parkingCode)
                    return getSessionByParkingCode(parkingCode);
                break;
                
            case GET_LATE_SESSIONS:{
                return getLateSessions();
            }
                
            case GET_PARKING_SESSION_HISTORY:
                if (payload instanceof String subId) {
                    return getParkingSessionHistory(subId);
                }
                break;
                
            case GET_RESERVATION_HISTORY:
                if (payload instanceof String subId) {
                    return getReservationHistory(subId);
                }
                break;
                
            case EXTEND_SESSION:
                if (payload instanceof Object[] arr && arr.length == 2) {
                    String parkingCode = (String) arr[0];
                    int additionalTime = (int) arr[1];
                    return extendSession(parkingCode, additionalTime);
                }
                break;

            case END_SESSION:
                if (payload instanceof String parkingCode) {
                    return endSession(parkingCode);
                }
                break;

            case GET_AVAILABLE_SPOT_ID:{
                return getAvailableSpotId();
            }

        	case GET_AVAILABLE_SPOTS:{
        		return getAvailableSpots();
        	}
        		
        	case RELEASE_SPOT:
        		if (payload instanceof String)
        			releaseSpot((String) payload);
        		break;
        		
        	case RESERVE_SPOT:
        	    if (payload instanceof String spotId) {
        	        return reserveSpot(spotId);
        	    }
        	    break;
        	    
        	case MARK_SPOT_OCCUPIED:
        	    if (payload instanceof String spotId) {
        	        markSpotOccupied(spotId);
        	    }
        	    break;

        	case GET_DURATION_FOR_SESSION:{
        	    if (payload instanceof String parkingCode)
        	        return getDurationForSession(parkingCode);
        	}

        	case CREATE_RESERVATION:
        	    if (payload instanceof Reservation reservation) {
        	        return insertReservation(reservation);
        	    }
        	    break;
        	    
        	case GET_RESERVATION_BY_CODE:
        	    if (payload instanceof String confirmationCode) {
        	        return getReservationByCode(confirmationCode);
        	    }
        	    break;
        	    
        	case UPDATE_RESERVATION_STATUS_CONFIRMED:
        	    if (payload instanceof String confirmationCode) {
        	        updateReservationStatusConfirmed(confirmationCode);
        	    }
        	    break;


        	case CANCEL_RESERVATION:
        	    if (payload instanceof String resId) {
        	        return cancelReservation(resId);
        	    }
        	    break;

        	    
        	case GET_RESERVED_SPOTS_COUNT:
        	    if (payload instanceof String dateStr) {
        	        return getReservedSpotsCount(dateStr);
        	    }
        	    break;
        	    
            case SCAN_VIA_TAG:{
                return getRandomSubId();
            }
            
            case SEND_PARKING_CODE_VIA_EMAIL:{
                if (payload instanceof String subId)
                    return sendParkingCodeToEmail(subId);
            }
            
            case GET_PARKING_CODE_BY_SUBSCRIBER_ID:{
                if (payload instanceof String subId)
                    return getActiveSessionBySubId(subId);
            }
            

            case CHECK_SUBSCRIBER_CODE:
			try {
				return isSubscriberCodeExists((String) payload);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            case HAS_RESERVATION:
			try {
				return checkActiveReservation((String) payload);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            case GENERATE_SESSION_DURATION_DISTRIBUTION:
			try {
				if (payload instanceof int[] arr && arr.length == 2) {
					return generateSessionDurationDistributionReportForMonth(arr[0], arr[1]);
				} else {
					return generateSessionDurationDistributionReport();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            case GENERATE_PEAK_USAGE_TIMES:
			try {
				if (payload instanceof int[] arr && arr.length == 2) {
					return generatePeakUsageTimesReportForMonth(arr[0], arr[1]);
				} else {
					return generatePeakUsageTimesReport();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            case GENERATE_SPOT_UTILIZATION:
			try {
				if (payload instanceof int[] arr && arr.length == 2) {
					return generateSpotUtilizationReportForMonth(arr[0], arr[1]);
				} else {
					return generateSpotUtilizationReport();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            case GENERATE_LATE_PARKING_REPORT:
			try {
				return generateLateParkingReportForMonth(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            case GET_AVAILABLE_SPOT_ID_NONRESERVED:
            	return getAvailableSpotIdNonReserved((LocalDate) payload);

            case GENERATE_SUBSCRIBER_REPORT:
                if (payload instanceof int[] arr && arr.length == 2) {
                    return getTopUsersForMonth(arr[0], arr[1]);
                }
                break;

            case GENERATE_TOP_EXTENDERS_REPORT:
                if (payload instanceof int[] arr && arr.length == 2) {
                    return generateTopExtendersReport(arr[0], arr[1]);
                }
                break;

            default:
                System.err.println("Unhandled request type: " + type);
        }
        return null;
    }
    
    


    /**
     * Refreshes the status of every parking spot for the given date.
     *
     * @param date the day for which to apply the statuses
     */
    public static void refreshDailySpotStatuses(LocalDate date) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement psReset =
                     conn.prepareStatement("UPDATE parkingspots SET spot_status = 'AVAILABLE'")) {
                psReset.executeUpdate();
            }

            String reserveSql = """
                    UPDATE parkingspots ps
                    JOIN reservations r ON ps.spot_id = r.space_id
                    SET  ps.spot_status = 'RESERVED'
                    WHERE DATE(r.start_time) = ?
                      AND r.reservation_status = 'PENDING'
                    """;
            try (PreparedStatement psReserve = conn.prepareStatement(reserveSql)) {
                psReserve.setDate(1, java.sql.Date.valueOf(date));
                psReserve.executeUpdate();
            }

            conn.commit();
            System.out.println("[Scheduler] Parking-spot statuses refreshed for " + date);
        } catch (Exception e) {
            System.err.println("refreshDailySpotStatuses failed: " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ignored) {}
                DBConnection.getInstance().releaseConnection(conn);
            }
        }
    }

    
    
    /**
     * Returns the spot_id of an available parking spot for a specific date, or null if none are available.
     *
     * @param date the date to check availability (LocalDate)
     * @return the spot_id if available, otherwise null
     */
    private static String getAvailableSpotIdNonReserved(LocalDate date) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = """
                SELECT ps.spot_id
                FROM parkingspots ps
                WHERE ps.spot_id NOT IN (
                    SELECT r.space_id
                    FROM reservations r
                    WHERE DATE(r.start_time) = ?
                )
                LIMIT 1
                """;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, java.sql.Date.valueOf(date));
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("spot_id");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getAvailableSpotId failed: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.getInstance().releaseConnection(conn);
            }
        }
        return null;
    }

    
    /**
     * Updates the parking spot status to OCCUPIED.
     *
     * @param spotId the ID of the parking spot
     */
    private static void markSpotOccupied(String spotId) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "UPDATE parkingspots SET spot_status = 'OCCUPIED' WHERE spot_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, spotId);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("markSpotOccupied failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
    }

    
    /**
     * Updates reservation status to CONFIRMED based on confirmation code.
     *
     * @param confirmationCode the reservation confirmation code
     */
    private static void updateReservationStatusConfirmed(String confirmationCode) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "UPDATE reservations SET reservation_status = 'CONFIRMED' WHERE confirmation_code = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, confirmationCode);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            System.err.println("updateReservationStatusConfirmed failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
    }

    
    
    /**
     * Retrieves all parking sessions for a specific subscriber (session history).
     *
     * @param subId the ID of the subscriber
     * @return a list of ParkingSession objects representing the subscriber's session history
     */
    private static List<ParkingSession> getParkingSessionHistory(String subId) {
        List<ParkingSession> sessions = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM parking_sessions WHERE subscriber_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, subId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        ParkingSession ps = new ParkingSession(
                            rs.getInt("session_id"),
                            rs.getString("parking_code"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getInt("duration"),
                            rs.getTimestamp("last_extension_time") != null
                                ? rs.getTimestamp("last_extension_time").toLocalDateTime()
                                : null,
                            ParkingSession.Status.valueOf(rs.getString("status")),
                            rs.getInt("extension_count"),
                            rs.getString("space_id"),
                            rs.getString("subscriber_id")
                        );
                        sessions.add(ps);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getParkingSessionHistory failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return sessions;
    }

    /**
     * Retrieves all reservation records for a specific subscriber.
     *
     * @param subId the ID of the subscriber
     * @return a list of Reservation objects representing the subscriber's reservation history
     */
    private static List<Reservation> getReservationHistory(String subId) {
        List<Reservation> reservations = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM reservations WHERE sub_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, subId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Reservation reservation = new Reservation(
                            rs.getString("res_id"),
                            rs.getString("confirmation_code"),
                            Reservation.ReservationStatus.valueOf(rs.getString("reservation_status")),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getInt("duration"),
                            rs.getString("sub_id"),
                            rs.getString("space_id")
                        );
                        reservations.add(reservation);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getReservationHistory failed: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.getInstance().releaseConnection(conn);
            }
        }

        return reservations;
    }

    
    /**
     * Retrieves a Reservation object by its confirmation code.
     *
     * @param confirmationCode the confirmation code of the reservation
     * @return the Reservation object if found, otherwise null
     */
    private static Reservation getReservationByCode(String confirmationCode) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM reservations WHERE confirmation_code = ? LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, confirmationCode);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Reservation(
                            rs.getString("res_id"),
                            rs.getString("confirmation_code"),
                            Reservation.ReservationStatus.valueOf(rs.getString("reservation_status")),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getInt("duration"),
                            rs.getString("sub_id"),
                            rs.getString("space_id")
                        );
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getReservationByCode failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return null;
    }
    
    /**
     * Cancels a reservation by setting its status to CANCELLED.
     * @param resId the reservation ID
     * @return true if cancellation succeeded, false otherwise
     */
    private static boolean cancelReservation(String resId) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "UPDATE reservations SET reservation_status = 'CANCELLED' WHERE res_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, resId);
                int rows = stmt.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            System.err.println("cancelReservation failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return false;
    }

    
    /**
     * Inserts a new reservation into the database.
     *
     * @param reservation the Reservation object containing reservation details
     * @return true if the reservation was inserted successfully, false otherwise
     */
    private static boolean insertReservation(Reservation reservation) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO reservations " +
                "(res_id, confirmation_code, reservation_status, start_time, duration, sub_id, space_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, reservation.getResId());
                stmt.setString(2, reservation.getConfirmationCode());
                stmt.setString(3, reservation.getReservationStatus().name());
                stmt.setTimestamp(4, Timestamp.valueOf(reservation.getStartTime()));
                stmt.setInt(5, reservation.getDuration());
                stmt.setString(6, reservation.getSubId());
                stmt.setString(7, reservation.getSpaceId());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    // Get subscriber details from DB
                    Subscriber sub = getSubscriberById(reservation.getSubId());
                    if (sub != null) {
                        bparkServer.notifications.NotifyEmail emailer = new bparkServer.notifications.NotifyEmail();
                        String subject = "BPark Reservation Confirmed";
                        String message = "Dear " + sub.getName() + ",\n\n"
                            + "Your reservation has been successfully made.\n"
                            + "Scheduled entry time: " + reservation.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "\n\n"
                            + "Your confirmation code is: " + reservation.getConfirmationCode() + "\n\n"
                            + "Please keep this code safe. You'll need it when entering or exiting the parking.\n\n"
                            + "Thank you for using BPark,\n"
                            + "The BPark Team";
                        emailer.sendNotification(sub.getEmail(), subject, message);
                    }
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("insertReservation failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return false;
    }


    
    /**
     * Sets the status of the specified parking spot to 'RESERVED'.
     *
     * @param spotId the ID of the parking spot to reserve
     * @return the spotId if the status was updated successfully, otherwise null
     */
    private static String reserveSpot(String spotId) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "UPDATE parkingspots SET spot_status = 'RESERVED' WHERE spot_id = ? AND spot_status = 'AVAILABLE'";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, spotId);
                int rows = stmt.executeUpdate();
                // Only return spotId if update happened (i.e., it was AVAILABLE)
                if (rows > 0) {
                    return spotId;
                }
            }
        } catch (Exception e) {
            System.err.println("reserveSpot failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return null;
    }

    
    /**
     * Returns the count of reservations that start on a specific date.
     *
     * @param dateStr the date string in "yyyy-MM-dd" format
     * @return the number of reservations for the given date, or -1 if an error occurs
     */
    private static int getReservedSpotsCount(String dateStr) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            // Use DATE(start_time) to compare only the date part
            String sql = "SELECT COUNT(*) FROM reservations WHERE DATE(start_time) = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, dateStr);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getReservedSpotsCount failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return -1;
    }

    
    /**
     * Updates the phone number of a subscriber in the database.
     *
     * @param subId the ID of the subscriber whose phone number is to be updated
     * @param newPhone the new phone number to set for the subscriber
     * @return true if the update was successful, false otherwise
     */
    private static boolean updateSubscriberPhone(String subId, String newPhone) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "UPDATE subscribers SET phone_number = ? WHERE subscriber_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newPhone);
                stmt.setString(2, subId);
                int rows = stmt.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            System.err.println("updateSubscriberPhone failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return false;
    }

    /**
     * Updates the email address of a subscriber in the database.
     *
     * @param subId the ID of the subscriber whose email is to be updated
     * @param newEmail the new email address to set for the subscriber
     * @return true if the update was successful, false otherwise
     */
    private static boolean updateSubscriberEmail(String subId, String newEmail) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "UPDATE subscribers SET email = ? WHERE subscriber_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, newEmail);
                stmt.setString(2, subId);
                int rows = stmt.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            System.err.println("updateSubscriberEmail failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return false;
    }

    
    /**
     * Retrieves a subscriber by their ID.
     *
     * @param subId the subscriber's ID
     * @return the Subscriber object if found, otherwise null
     */
    private static Subscriber getSubscriberById(String subId) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM subscribers WHERE subscriber_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, subId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new Subscriber(
                            rs.getString("subscriber_id"),
                            rs.getString("name"),
                            rs.getString("phone_number"),
                            rs.getString("email")
                        );
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getSubscriberById failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return null;
    }

    
    /**
     * Retrieves all parking sessions that are overdue (late) and still active.
     *
     * @return a list of late ParkingSession objects
     */
    private static List<ParkingSession> getLateSessions() {
        List<ParkingSession> lateSessions = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql =
                "SELECT * FROM parking_sessions " +
                "WHERE status = 'ACTIVE' " +
                "AND DATE_ADD(start_time, INTERVAL duration MINUTE) < NOW()";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ParkingSession session = new ParkingSession(
                        rs.getInt("session_id"),
                        rs.getString("parking_code"),
                        rs.getTimestamp("start_time").toLocalDateTime(),
                        rs.getInt("duration"),
                        rs.getTimestamp("last_extension_time") != null
                            ? rs.getTimestamp("last_extension_time").toLocalDateTime()
                            : null,
                        ParkingSession.Status.valueOf(rs.getString("status")),
                        rs.getInt("extension_count"),
                        rs.getString("space_id"),
                        rs.getString("subscriber_id")
                    );
                    lateSessions.add(session);
                }
            }
        } catch (Exception e) {
            System.err.println("getLateSessions failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return lateSessions;
    }

    
    /**
     * Retrieves a parking session by its parking code.
     *
     * @param parkingCode the code of the session to retrieve
     * @return the ParkingSession object if found, otherwise null
     */
    private static ParkingSession getSessionByParkingCode(String parkingCode) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM parking_sessions WHERE parking_code = ? LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, parkingCode);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new ParkingSession(
                            rs.getInt("session_id"),
                            rs.getString("parking_code"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getInt("duration"),
                            rs.getTimestamp("last_extension_time") != null
                                ? rs.getTimestamp("last_extension_time").toLocalDateTime()
                                : null,
                            ParkingSession.Status.valueOf(rs.getString("status")),
                            rs.getInt("extension_count"),
                            rs.getString("space_id"),
                            rs.getString("subscriber_id")
                        );
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getSessionByParkingCode failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return null;
    }

    /**
     * Validates whether the given parking code exists in the system and is either in 'ACTIVE' or 'RESERVED' status.
     *
     * This method checks the `parking_sessions` table in the database to verify that the provided parking code
     * corresponds to a session that is currently active or reserved.
     *
     * @param parkingCode the parking code to validate
     * @return {@code true} if the parking code exists and has an ACTIVE or RESERVED status; {@code false} otherwise
     */
    private static boolean validateParkingCode(String parkingCode) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT 1 FROM parking_sessions WHERE parking_code = ? AND (status = 'ACTIVE' OR status = 'RESERVED') LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, parkingCode);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (Exception e) {
            System.err.println("validateParkingCode failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return false;
    }



    
    /**
     * Inserts a new parking session into the database.
     *
     * @param session the ParkingSession object containing the session details
     * @return the ParkingSession with its generated session ID, or null if insertion failed
     */
    private static ParkingSession startSession(ParkingSession session) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO parking_sessions " +
                "(parking_code, start_time, duration, last_extension_time, status, extension_count, space_id, subscriber_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            	stmt.setString(1, session.getParkingCode());
            	stmt.setTimestamp(2, java.sql.Timestamp.valueOf(session.getStartTime()));
            	stmt.setInt(3, session.getDuration());
                if (session.getLastExtensionTime() != null) {
                    stmt.setTimestamp(4, java.sql.Timestamp.valueOf(session.getLastExtensionTime()));
                } else {
                    stmt.setNull(4, java.sql.Types.TIMESTAMP);
                }
                stmt.setString(5, session.getStatus().name());
                stmt.setInt(6, session.getExtensionCount());
                stmt.setString(7, session.getSpaceId());
                stmt.setString(8, session.getSubId());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int sessionId = generatedKeys.getInt(1);
                            return new ParkingSession(
                                sessionId,
                                session.getParkingCode(),
                                session.getStartTime(),
                                session.getDuration(),
                                session.getLastExtensionTime(),
                                session.getStatus(),
                                session.getExtensionCount(),
                                session.getSpaceId(),
                                session.getSubId()
                            );
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("startSession failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return null;
    }

    
    /**
     * Ends a parking session identified by the given parking code.
     * Updates the session status to 'COMPLETED' and calculates its duration.
     * If the duration exceeds 240 minutes, an immediate notification is sent to the subscriber.
     *
     * @param parkingCode the unique code of the session to end
     * @return true if the session was successfully ended, false otherwise
     */
    private static boolean endSession(String parkingCode) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();

            // Step 1: Retrieve session details
            String fetchSql = "SELECT session_id, subscriber_id, start_time, duration FROM parking_sessions " +
                              "WHERE parking_code = ? AND (status = 'ACTIVE' OR status = 'RESERVED')";
            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchSql)) {
                fetchStmt.setString(1, parkingCode);
                try (ResultSet rs = fetchStmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("No session found for code: '" + parkingCode + "'");
                        return false;
                    }

                    int sessionId = rs.getInt("session_id");
                    String subscriberId = rs.getString("subscriber_id");
                    LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                    LocalDateTime now = LocalDateTime.now();
                    int currentDuration = rs.getInt("duration");
                    long durationMinutes = Duration.between(startTime, now).toMinutes();

                    // Step 2: Update status and duration
                    String updateSql = "UPDATE parking_sessions SET status = 'COMPLETED', duration = ? WHERE session_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, (int) durationMinutes);
                        updateStmt.setInt(2, sessionId);
                        int rows = updateStmt.executeUpdate();
                        if (rows == 0) return false;
                    }
                    System.out.println("durationMinutes: " + durationMinutes);
                    System.out.println("currentDuration: " + currentDuration);
                    // Step 3: Notify subscriber if duration exceeded
                    if (durationMinutes > currentDuration) {
                        Subscriber subscriber = getSubscriberById(subscriberId);
                        if (subscriber != null) {
                            ParkingSession session = new ParkingSession(
                                sessionId,
                                parkingCode,
                                startTime,
                                (int) durationMinutes,
                                null, // lastExtensionTime
                                Status.COMPLETED,
                                0, // extensionCount
                                null, // spaceId
                                subscriberId
                            );
                            int lateDuration = (int) (durationMinutes - currentDuration);
                            insertToLateTable(subscriberId, parkingCode, lateDuration);
                            new NotificationController().notifyImmediateLatePickup(subscriber, session);
                        } else {
                            System.err.println("Subscriber not found for late pickup notification: " + subscriberId);
                        }
                    }

                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("endSession failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return false;
    }
    /**
     * Inserts a record into the {@code late_parkings} table for a subscriber who was late picking up their vehicle.
     *
     * This method logs the subscriber's ID, the relevant parking code, and the duration of the delay (in minutes).
     *
     * @param subscriberId the ID of the subscriber who was late
     * @param parkingCode the code of the parking session associated with the delay
     * @param lateDuration the number of minutes the subscriber was late
     */
    private static void insertToLateTable(String subscriberId, String parkingCode, int lateDuration) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();

            String sql = "INSERT INTO late_parkings (subId, parkingCode, lateDuration) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, subscriberId);
                stmt.setString(2, parkingCode);
                stmt.setInt(3, lateDuration);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Late arrival record inserted successfully.");
                } else {
                    System.out.println("Failed to insert late arrival record.");
                }
            }
        } catch (Exception e) {
            System.err.println("insertToLateTable failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
    }


    
    /**
     * Extends the duration of a parking session identified by parking code.
     *
     * Business logic:
     * 1. Only allow extension requests between 15 and 60 minutes before session ends.
     * 2. Maximum 3 extensions per session.
     * 3. Session cannot extend past 21:00.
     * 4. No conflicting reservation for the same spot after the new end time.
     *
     * @param parkingCode    the unique code of the session to extend
     * @param additionalTime the number of minutes to add to the session duration
     * @return true if the session was successfully extended, false otherwise
     */
    private static int extendSession(String parkingCode, int additionalTime) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();

            String fetchSql = "SELECT * FROM parking_sessions WHERE parking_code = ? AND status = 'ACTIVE' LIMIT 1";
            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchSql)) {
                fetchStmt.setString(1, parkingCode);
                try (ResultSet rs = fetchStmt.executeQuery()) {
                    if (!rs.next()) return 1;

                    int sessionId = rs.getInt("session_id");
                    LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                    int duration = rs.getInt("duration");
                    int extensionCount = rs.getInt("extension_count");
                    String spaceId = rs.getString("space_id");

                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime sessionEnd = startTime.plusMinutes(duration);
                    
                    int minutesLeft = (int) Duration.between(now, sessionEnd).toMinutes();
                    System.out.println("minutes left: " + minutesLeft);
                    if (minutesLeft < 15) {
                        System.err.println("Extension not allowed 15 minutes before session end.");
                        return 2;
                    }
					
                    if (extensionCount >= 3) {
                        System.err.println("Max 3 extensions allowed.");
                        return 3;
                    }
                    
                    LocalDateTime newEnd = sessionEnd.plusMinutes(additionalTime);

                    /*
                    if (newEnd.toLocalTime().isAfter(java.time.LocalTime.of(21, 0))) {

                        System.err.println("Cannot extend session past 21:00.");
                        return 4;
                    }
					*/
                    String conflictSql = "SELECT 1 FROM reservations WHERE space_id = ? AND reservation_status = 'PENDING' " +
                            "AND start_time BETWEEN ? AND ?";
                    try (PreparedStatement conflictStmt = conn.prepareStatement(conflictSql)) {
                        conflictStmt.setString(1, spaceId);
                        conflictStmt.setTimestamp(2, Timestamp.valueOf(sessionEnd));
                        conflictStmt.setTimestamp(3, Timestamp.valueOf(newEnd));
                        try (ResultSet conflictRs = conflictStmt.executeQuery()) {
                            if (conflictRs.next()) {
                                System.err.println("Conflicting reservation exists.");
                                return 5;
                            }
                        }
                    }

                    String updateSql = "UPDATE parking_sessions SET duration = duration + ?, last_extension_time = NOW(), extension_count = extension_count + 1 WHERE session_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, additionalTime);
                        updateStmt.setInt(2, sessionId);
                        int rows = updateStmt.executeUpdate();
                        return 6;//Success
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("extendSession failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return -1;//Unknown fail
    }

    /**
     * Retrieves the expected pickup time (end time) of an active parking session by parking code.
     *
     * The pickup time is calculated by adding the duration (in minutes) to the session's start time.
     * This method only returns a result for sessions with status 'ACTIVE'.
     *
     * @param parkingCode the unique parking code identifying the session
     * @return the calculated pickup time as {@link LocalDateTime}, or {@code null} if no active session is found or an error occurs
     */
    private static LocalDateTime currentPickupTime (String parkingCode) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();

            String fetchSql = "SELECT start_time, duration FROM parking_sessions WHERE parking_code = ? AND status = 'ACTIVE' LIMIT 1";
            try (PreparedStatement fetchStmt = conn.prepareStatement(fetchSql)) {
                fetchStmt.setString(1, parkingCode);
                try (ResultSet rs = fetchStmt.executeQuery()) {
                    if (rs.next()) {
                        LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
                        int duration = rs.getInt("duration");
                        return startTime.plusMinutes(duration);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getSessionEndTime failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return null;
    }

    
    /**
     * Returns the spot_id of an available parking spot, or null if none are available.
     *
     * @return the spot_id if available, otherwise null
     */
    private static String getAvailableSpotId() { // Need to be changes, the correct logic is to check available parking spot in the reservation table in the same date
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            // here we need to check if I can reserve in a future time
            // the current problem is that we check based on the current state of the parking spots - that is not good
            String sql = "SELECT spot_id FROM parkingspots WHERE spot_status = 'AVAILABLE' LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("spot_id");
                }
            }
        } catch (Exception e) {
            System.err.println("getAvailableSpotId failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return null;
    }

    private static Integer getDurationForSession(String parkingCode) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT start_time, status, duration FROM parking_sessions WHERE parking_code = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, parkingCode);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String status = rs.getString("status");
                        if ("COMPLETED".equals(status)) {
                            return rs.getInt("duration");
                        } else {
                            LocalDateTime start = rs.getTimestamp("start_time").toLocalDateTime();
                            long minutes = Duration.between(start, LocalDateTime.now()).toMinutes();
                            return (int) minutes;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getDurationForSession failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return null;
    }

    
    /**
     * Updates the specified parking spot in the database to 'AVAILABLE' status.
     *
     * @param spaceId the ID of the parking spot to be released
     */
    private static void releaseSpot(String spaceId) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "UPDATE parkingspots SET spot_status = 'AVAILABLE' WHERE spot_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, spaceId);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated == 0) {
                    System.err.println("No spot was updated. Possibly invalid spot_id: " + spaceId);
                }
            }
        } catch (Exception e) {
            System.err.println("releaseSpot failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
    }

    
    /**
     * Inserts a new subscriber into the database.
     *
     * @param sub the Subscriber object containing registration details
     * @return the Subscriber if registration was successful, otherwise null
     */
	private static Subscriber registerSubscriber(Subscriber sub) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "INSERT INTO subscribers (subscriber_id, name, phone_number, email) VALUES (?, ?, ?, ?)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, sub.getSubId());
                stmt.setString(2, sub.getName());
                stmt.setString(3, sub.getPhoneNumber());
                stmt.setString(4, sub.getEmail());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    // Send welcome email
                    bparkServer.notifications.NotifyEmail emailer = new bparkServer.notifications.NotifyEmail();
                    String subject = "Welcome to BPark!";
                    String message = "Dear " + sub.getName() + ",\n\nCongratulations on joining BPark as a subscriber! We are excited to have you with us.\n\n Your new subscriber ID is: " + sub.getSubId() + "\n\nBest regards,\nThe BPark Team";
                    emailer.sendNotification(sub.getEmail(), subject, message);
                    return sub;
                } else {
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("registerSubscriber failed: " + e.getMessage());
            return null;
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
    }

	
	/**
	 * Retrieves a list of all subscribers from the database.
	 *
	 * @return a list of Subscriber objects
	 */
    private static List<Subscriber> getAllSubscribers() {
        List<Subscriber> subs = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM subscribers";

            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                	Subscriber s = new Subscriber(
                		    rs.getString("subscriber_id"),
                		    rs.getString("name"),
                		    rs.getString("phone_number"),
                		    rs.getString("email")
                		);
                    subs.add(s);
                }
            }
        } catch (Exception e) {
            System.err.println("getAllSubscribers failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }

        return subs;
    }
    
    
    /**
     * Returns the count of currently available parking spots from the database.
     *
     * @return the number of available spots, or -1 if an error occurs
     */
    private static Integer getAvailableSpots() {
    	Connection conn = null;
    	
    	try {
    		conn = DBConnection.getInstance().getConnection();
    		 String sql = "SELECT COUNT(*) AS available FROM parkingspots WHERE spot_status = 'AVAILABLE'";
    		 try (PreparedStatement stmt = conn.prepareStatement(sql);
    				 ResultSet rs = stmt.executeQuery()) {
    			 if (rs.next()) {
    				 return Integer.valueOf(rs.getInt("available"));
    			 }
    		 }
    	}
    	catch (Exception e) {
    		System.err.println("getAvailableSpots failed: " + e.getMessage());
    	}
    	finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
    	return Integer.valueOf(-1);
    }
    
    
    /**
     * Retrieves all currently active parking sessions from the database.
     * 
     * This method queries the 'parking_sessions' table and returns all records
     * where the status is 'ACTIVE', regardless of the subscriber ID.
     *
     * @return a list of {@link ParkingSession} objects with status 'ACTIVE',
     *         or an empty list if none are found or an error occurs
     */
    private static List<ParkingSession> getActiveSessions() {
        List<ParkingSession> sessions = new ArrayList<>();
        Connection conn = null;

        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM parking_sessions WHERE status = 'ACTIVE'";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        ParkingSession ps = new ParkingSession(
                            rs.getInt("session_id"),
                            rs.getString("parking_code"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getInt("duration"),
                            rs.getTimestamp("last_extension_time") != null
                                ? rs.getTimestamp("last_extension_time").toLocalDateTime()
                                : null,
                            ParkingSession.Status.valueOf(rs.getString("status")),
                            rs.getInt("extension_count"),
                            rs.getString("space_id"),
                            rs.getString("subscriber_id")
                        );
                        sessions.add(ps);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getActiveSessions failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }

        return sessions;
    }

    
    private static String getRandomSubId() {
        Connection conn = null;
        String subId = null;

        try {
            conn = DBConnection.getInstance().getConnection();

            String sql = "SELECT subscriber_id FROM subscribers ORDER BY RAND() LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    subId = rs.getString("subscriber_id");
                }
            }

        } catch (Exception e) {
            System.err.println("getRandomSubId failed: " + e.getMessage());
        } finally {
            if (conn != null) {
                DBConnection.getInstance().releaseConnection(conn);
            }
        }

        return subId;
    }

    private static boolean sendParkingCodeToEmail(String subId) {
        Subscriber subscriber = getSubscriberById(subId);
        ParkingSession session = getActiveSessionBySubId(subId);

        return NotificationController.getInstance().sendParkingCodeByEmail(subscriber, session);
    }



    
    public static boolean isSubscriberCodeExists(String code) throws InterruptedException {
        String query = "SELECT COUNT(*) FROM subscribers WHERE subscriber_id = ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean checkActiveReservation(String subCode) throws InterruptedException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE sub_id = ? AND reservation_status = 'PENDING'";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, subCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private static ParkingSession getActiveSessionBySubId(String subId) {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT * FROM parking_sessions WHERE subscriber_id = ? AND status = 'ACTIVE' LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, subId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new ParkingSession(
                            rs.getInt("session_id"),
                            rs.getString("parking_code"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getInt("duration"),
                            rs.getTimestamp("last_extension_time") != null ? rs.getTimestamp("last_extension_time").toLocalDateTime() : null,
                            ParkingSession.Status.valueOf(rs.getString("status")),
                            rs.getInt("extension_count"),
                            rs.getString("space_id"),
                            rs.getString("subscriber_id")
                        );
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getActiveSessionBySubId failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return null;
    }

    



    /**
     * Tests the database connection and prints database name and server time.
     *
     * @return true if the connection is successful, false otherwise
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT DATABASE() as db_name, NOW() as server_time")) {
                if (rs.next()) {
                    System.out.println("DB Connected successfully!");
                    System.out.println("Connected to database: " + rs.getString("db_name"));
                    System.out.println("Server time: " + rs.getString("server_time"));
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to connect to DB: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return false;
    }

    private static Report generateSessionDurationDistributionReport() throws InterruptedException {
        int[] bins = {30, 60, 120, 240};
        String[] binLabels = {"0-30", "31-60", "61-120", "121-240", "241+"};
        int[] counts = new int[binLabels.length];
        String sql = "SELECT duration FROM parking_sessions";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int duration = rs.getInt("duration");
                    int binIndex = 0;
                    while (binIndex < bins.length && duration > bins[binIndex]) {
                        binIndex++;
                    }
                    counts[binIndex]++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        List<String> headers = List.of("Duration Range (min)", "Session Count");
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < binLabels.length; i++) {
            rows.add(List.of(binLabels[i], String.valueOf(counts[i])));
        }
        return new Report(
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Session Duration Distribution",
            headers,
            rows
        );
    }

    private static Report generatePeakUsageTimesReport() throws InterruptedException {
        int[] hourCounts = new int[24];
        String sql = "SELECT start_time FROM parking_sessions";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("start_time");
                    if (ts != null) {
                        int hour = ts.toLocalDateTime().getHour();
                        hourCounts[hour]++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        List<String> headers = List.of("Hour of Day", "Session Count");
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            rows.add(List.of(String.format("%02d:00", i), String.valueOf(hourCounts[i])));
        }
        return new Report(
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Peak Usage Times",
            headers,
            rows
        );
    }

    private static Report generateSpotUtilizationReport() throws InterruptedException {
        String sql = "SELECT space_id, COUNT(*) as session_count FROM parking_sessions GROUP BY space_id ORDER BY session_count DESC LIMIT 20";
        List<String> headers = List.of("Spot ID", "Session Count");
        List<List<String>> rows = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String spotId = rs.getString("space_id");
                    int count = rs.getInt("session_count");
                    rows.add(List.of(spotId, String.valueOf(count)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return new Report(
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Spot Utilization",
            headers,
            rows
        );
    }

    /**
     * Returns a list of top users (subscribers) who parked the most in a given month.
     * Each entry is an Object[]: [Subscriber, Integer sessionCount]
     * @param year the year (e.g., 2024)
     * @param month the month (1-12)
     * @return list of Object[]
     */
    private static List<Object[]> getTopUsersForMonth(int year, int month) {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT s.subscriber_id, s.name, s.phone_number, s.email, COUNT(ps.session_id) as session_count " +
                "FROM subscribers s " +
                "JOIN parking_sessions ps ON s.subscriber_id = ps.subscriber_id " +
                "WHERE EXTRACT(YEAR FROM ps.start_time) = ? AND EXTRACT(MONTH FROM ps.start_time) = ? " +
                "GROUP BY s.subscriber_id, s.name, s.phone_number, s.email " +
                "ORDER BY session_count DESC LIMIT 10";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Subscriber sub = new Subscriber(
                            rs.getString("subscriber_id"),
                            rs.getString("name"),
                            rs.getString("phone_number"),
                            rs.getString("email")
                        );
                        int count = rs.getInt("session_count");
                        result.add(new Object[]{sub, count});
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("getTopUsersForMonth failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return result;
    }

    /**
     * Generates a report of the top extenders (subscribers with the most session extensions) for a given month and year.
     * @param year The year for the report.
     * @param month The month for the report.
     * @return A Report object containing subscriber ID, name, and total extensions.
     */
    public static Report generateTopExtendersReport(int year, int month) {
        String sql = "SELECT s.subscriber_id, s.name, SUM(ps.extension_count) as total_extensions " +
                     "FROM subscribers s " +
                     "JOIN parking_sessions ps ON s.subscriber_id = ps.subscriber_id " +
                     "WHERE EXTRACT(YEAR FROM ps.start_time) = ? AND EXTRACT(MONTH FROM ps.start_time) = ? " +
                     "GROUP BY s.subscriber_id, s.name " +
                     "ORDER BY total_extensions DESC LIMIT 3";
        List<String> headers = List.of("Subscriber ID", "Subscriber Name", "Total Extensions");
        List<List<String>> rows = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String subId = rs.getString("subscriber_id");
                        String name = rs.getString("name");
                        int totalExtensions = rs.getInt("total_extensions");
                        rows.add(List.of(subId, name, String.valueOf(totalExtensions)));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("generateTopExtendersReport failed: " + e.getMessage());
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return new Report(
            java.time.LocalDateTime.now(),
            java.time.LocalDateTime.now(),
            "Top Extenders",
            headers,
            rows
        );
    }

    // Schema option constants
    public static final int SCHEMA_DB1 = 1;
    public static final int SCHEMA_DB2 = 2;
    public static final int SCHEMA_CLEAR_DB = 3;

    /**
     * Loads a database schema from an SQL file based on the selected option.
     * @param schemaId The schema to load: SCHEMA_DB1, SCHEMA_DB2, or SCHEMA_CLEAR_DB.
     */
    public static void loadSchema(int schemaId) {
        String sqlFileName;
        switch (schemaId) {
            case SCHEMA_DB1:
                sqlFileName = "/db/DB1.sql";
                break;
            case SCHEMA_DB2:
                sqlFileName = "/db/DB2.sql";
                break;
            case SCHEMA_CLEAR_DB:
                sqlFileName = "/db/CLEAR_DB.sql";
                break;
            default:
                System.err.println("Unknown schema option: " + schemaId);
                return;
        }
        try (java.io.InputStream is = DataBaseController.class.getResourceAsStream(sqlFileName)) {
            if (is == null) {
                System.err.println("SQL file not found: " + sqlFileName);
                return;
            }
            String sql = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            try (Connection conn = DBConnection.getInstance().getConnection()) {
                for (String statement : sql.split(";")) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute(trimmed);
                        }
                    }
                }
                System.out.println("Schema loaded: " + schemaId);
            }
        } catch (Exception e) {
            System.err.println("Error loading schema: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates a session duration distribution report for a specific month and year.
     * @param year The year for the report.
     * @param month The month for the report.
     * @return A Report object containing duration ranges and session counts.
     * @throws InterruptedException if the operation is interrupted.
     */
    public static Report generateSessionDurationDistributionReportForMonth(int year, int month) throws InterruptedException {
        int[] bins = {30, 60, 120, 240};
        String[] binLabels = {"0-30", "31-60", "61-120", "121-240", "241+"};
        int[] counts = new int[binLabels.length];
        String sql = "SELECT duration FROM parking_sessions WHERE EXTRACT(YEAR FROM start_time) = ? AND EXTRACT(MONTH FROM start_time) = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        int duration = rs.getInt("duration");
                        int binIndex = 0;
                        while (binIndex < bins.length && duration > bins[binIndex]) {
                            binIndex++;
                        }
                        counts[binIndex]++;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        List<String> headers = List.of("Duration Range (min)", "Session Count");
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < binLabels.length; i++) {
            rows.add(List.of(binLabels[i], String.valueOf(counts[i])));
        }
        return new Report(
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Session Duration Distribution",
            headers,
            rows
        );
    }

    /**
     * Generates a peak usage times report for a specific month and year.
     * @param year The year for the report.
     * @param month The month for the report.
     * @return A Report object containing hours of the day and session counts.
     * @throws InterruptedException if the operation is interrupted.
     */
    public static Report generatePeakUsageTimesReportForMonth(int year, int month) throws InterruptedException {
        int[] hourCounts = new int[24];
        String sql = "SELECT start_time FROM parking_sessions WHERE EXTRACT(YEAR FROM start_time) = ? AND EXTRACT(MONTH FROM start_time) = ?";
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Timestamp ts = rs.getTimestamp("start_time");
                        if (ts != null) {
                            int hour = ts.toLocalDateTime().getHour();
                            hourCounts[hour]++;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        List<String> headers = List.of("Hour of Day", "Session Count");
        List<List<String>> rows = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            rows.add(List.of(String.format("%02d:00", i), String.valueOf(hourCounts[i])));
        }
        return new Report(
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Peak Usage Times",
            headers,
            rows
        );
    }

    /**
     * Generates a spot utilization report for a specific month and year.
     * @param year The year for the report.
     * @param month The month for the report.
     * @return A Report object containing spot IDs and session counts.
     * @throws InterruptedException if the operation is interrupted.
     */
    public static Report generateSpotUtilizationReportForMonth(int year, int month) throws InterruptedException {
        String sql = "SELECT space_id, COUNT(*) as session_count FROM parking_sessions WHERE EXTRACT(YEAR FROM start_time) = ? AND EXTRACT(MONTH FROM start_time) = ? GROUP BY space_id ORDER BY session_count DESC LIMIT 20";
        List<String> headers = List.of("Spot ID", "Session Count");
        List<List<String>> rows = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String spotId = rs.getString("space_id");
                        int count = rs.getInt("session_count");
                        rows.add(List.of(spotId, String.valueOf(count)));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return new Report(
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Spot Utilization",
            headers,
            rows
        );
    }

    /**
     * Generates a late parking report for a specific month and year.
     * @param year The year for the report.
     * @param month The month for the report.
     * @return A Report object containing late parking details for the specified period.
     * @throws InterruptedException if the operation is interrupted.
     */
    public static Report generateLateParkingReportForMonth(int year, int month) throws InterruptedException {
        String sql = "SELECT lp.subId, s.name, lp.parkingCode, lp.lateDuration, ps.start_time " +
                    "FROM late_parkings lp " +
                    "JOIN subscribers s ON lp.subId = s.subscriber_id " +
                    "JOIN parking_sessions ps ON lp.parkingCode = ps.parking_code " +
                    "WHERE EXTRACT(YEAR FROM ps.start_time) = ? AND EXTRACT(MONTH FROM ps.start_time) = ? " +
                    "ORDER BY lp.lateDuration DESC";
        
        List<String> headers = List.of("Subscriber ID", "Subscriber Name", "Parking Code", "Late Duration (min)", "Session Start Time");
        List<List<String>> rows = new ArrayList<>();
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, year);
                stmt.setInt(2, month);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String subId = rs.getString("subId");
                        String name = rs.getString("name");
                        String parkingCode = rs.getString("parkingCode");
                        int lateDuration = rs.getInt("lateDuration");
                        Timestamp startTime = rs.getTimestamp("start_time");
                        
                        rows.add(List.of(
                            subId,
                            name,
                            parkingCode,
                            String.valueOf(lateDuration),
                            startTime != null ? startTime.toString() : "N/A"
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) DBConnection.getInstance().releaseConnection(conn);
        }
        return new Report(
            LocalDateTime.now(),
            LocalDateTime.now(),
            "Late Parking Report",
            headers,
            rows
        );
    }
}


