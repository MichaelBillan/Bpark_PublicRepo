package bparkServer.notifications;

import common.entities.ParkingSession;
import common.entities.Subscriber;
import bparkClient.logic.Bparkclient;
import bparkClient.logic.ParkingSessionController;
import common.RequestType;
import java.util.List;

/**
 * Orchestrates notifications (email and SMS) to users who are late for picking up their vehicle.
 */
public class NotificationController {

    private final NotifyDelays smsNotifier;
    private final NotifyDelays emailNotifier;

    /**
     * Constructs a NotificationController with SMS and Email notifiers.
     */
    public NotificationController() {
        this.smsNotifier = new NotifySms();
        this.emailNotifier = new NotifyEmail();
    }

    private static final NotificationController instance = new NotificationController();

    /**
     * Gets the singleton instance of the NotificationController.
     *
     * @return the singleton instance
     */
    public static NotificationController getInstance() {
        return instance;
    }

    /**
     * Checks for late parking sessions and sends notifications to affected users.
     * This method should be called periodically (e.g., by the scheduler).
     */
    public void checkAndNotifyDelays() {
        List<ParkingSession> lateSessions = ParkingSessionController.getInstance().getLateSessions();
        for (ParkingSession session : lateSessions) {
            Object response = Bparkclient.getInstance().sendAndWait(RequestType.GET_SUBSCRIBER_BY_ID, session.getSubId());
            Subscriber subscriber = (response instanceof Subscriber) ? (Subscriber) response : null;
            if (subscriber == null) {
                System.err.println("Subscriber not found for session: " + session.getParkingCode());
                continue;
            }

            String email = subscriber.getEmail();
            String phone = subscriber.getPhoneNumber();
            String subject = "Parking Delay Notification";
            String message = "Dear " + subscriber.getName() + ",\n"
                           + "You are late to pick up your vehicle. Please take action as soon as possible.\n"
                           + "Parking Code: " + session.getParkingCode();

            if (email != null && !email.isEmpty()) {
                emailNotifier.sendNotification(email, subject, message);
            }
            if (phone != null && !phone.isEmpty()) {
                smsNotifier.sendNotification(phone, "", message);
            }
        }
    }

    /**
     * Sends a late pickup notification immediately for a given subscriber and session.
     * Can be used outside the scheduler, e.g., at the moment of ending a session.
     *
     * @param subscriber the subscriber to notify
     * @param session the parking session
     */
    public void notifyImmediateLatePickup(Subscriber subscriber, ParkingSession session) {

        String email = subscriber.getEmail();
        String phone = subscriber.getPhoneNumber();
        String subject = "Parking Delay Notification";
        String message = "Dear " + subscriber.getName() + ",\n"
                       + "You were late picking up your vehicle. The delay was recorded in the system!.\n"
                       + "Parking Code: " + session.getParkingCode();

        if (email != null && !email.isEmpty()) {
            emailNotifier.sendNotification(email, subject, message);
        }
        if (phone != null && !phone.isEmpty()) {
            smsNotifier.sendNotification(phone, "", message);
        }
    }

    /**
     * Sends the parking code via email to the given subscriber.
     *
     * @param subscriber the target subscriber
     * @param session the active parking session
     * @return true if the email was sent successfully, false otherwise
     */
    public boolean sendParkingCodeByEmail(Subscriber subscriber, ParkingSession session) {
        if (subscriber == null || session == null || subscriber.getEmail() == null || subscriber.getEmail().isEmpty())
            return false;

        String subject = "Your Parking Code";
        String message = "Dear " + subscriber.getName() + ",\n\nYour current parking code is: "
                       + session.getParkingCode() + "\n\nThank you,\nBpark Team";

        return emailNotifier.sendNotification(subscriber.getEmail(), subject, message);
    }
}
