package bparkServer.notifications;

/**
 * Notification implementation for sending SMS notifications to users (mock).
 */
public class NotifySms implements NotifyDelays {

    @Override
    public boolean sendNotification(String recipientAddress, String subject, String message) {
        // Simulate SMS sending (print to console)
        System.out.println("Mock SMS sent to " + recipientAddress + ": " + message);
        return true;
    }
}
