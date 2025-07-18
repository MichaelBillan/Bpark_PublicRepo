package bparkServer.scheduler;

import bparkServer.notifications.NotificationController;
import java.util.TimerTask;

/**
 * TimerTask to check and notify users who are late for picking up their vehicles.
 */
public class SendNotificationTask extends TimerTask {

    /**
     * The controller responsible for managing and sending notifications.
     */
    private final NotificationController notificationController;

    /**
     * Constructs a SendNotificationTask with the given notification controller.
     *
     * @param notificationController the controller used to send notifications
     */
    public SendNotificationTask(NotificationController notificationController) {
        this.notificationController = notificationController;
    }

    /**
     * Executes the task: checks for delayed parking sessions
     * and sends notifications if needed.
     */
    @Override
    public void run() {
        notificationController.checkAndNotifyDelays();
    }
}
