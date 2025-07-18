package bparkServer.notifications;

/**
 * An interface for sending notifications to users.
 * Implementations may provide email, SMS, or other notification methods.
 */
public interface NotifyDelays {
    /**
     * Sends a notification message to the specified recipient.
     *
     * @param recipientAddress the destination address (email or phone number)
     * @param subject the subject of the notification (useful for email, may be ignored for SMS)
     * @param message the notification content/body
     * @return true if the notification was sent successfully, otherwise false
     */
    boolean sendNotification(String recipientAddress, String subject, String message);
}
