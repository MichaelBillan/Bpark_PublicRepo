package bparkServer.scheduler;

import bparkServer.notifications.NotificationController;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Controls scheduling of periodic server tasks 
 * (like sending notifications and generating monthly reports).
 */
public class SchedulerController {

    private final Timer timer;
    private final NotificationController notificationController;

    /**
     * Constructs a SchedulerController with a reference to the notification controller.
     *
     * @param notificationController the controller responsible for sending notifications
     */
    public SchedulerController(NotificationController notificationController) {
        this.timer = new Timer("BparkScheduler", true); // daemon thread
        this.notificationController = notificationController;
    }

    /**
     * Starts the periodic notification task.
     *
     * @param periodMillis How often to run the task (in milliseconds)
     */
    public void startNotificationScheduler(long periodMillis) {
        SendNotificationTask task = new SendNotificationTask(notificationController);
        timer.scheduleAtFixedRate(task, 0, periodMillis);
        System.out.println("Notification scheduler started, period: " + periodMillis + " ms");
    }

    /**
     * Schedules the daily task that refreshes the parking-spot statuses.
     * It is set to fire at 00:01 every calendar day.
     */
    public void startDailySpotStatusRefresh() {
        UpdateDailySpotsTask task = new UpdateDailySpotsTask();
        long period = 24 * 60 * 60 * 1000L;

        // Compute first run = today/tomorrow at 00:01
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE,      1);
        cal.set(java.util.Calendar.SECOND,      0);
        cal.set(java.util.Calendar.MILLISECOND, 0);

        long firstRun = cal.getTimeInMillis();
        
        if (firstRun <= System.currentTimeMillis()) {
            firstRun += period;
        }

        timer.scheduleAtFixedRate(task, firstRun, period);
        System.out.println("Daily spot-status refresh scheduled: first run " +
                new java.util.Date(firstRun));
    }

    /**
     * Stops all scheduled tasks.
     */
    public void stopScheduler() {
        timer.cancel();
        System.out.println("Scheduler stopped.");
    }

    /**
     * Starts the periodic monthly report generation task.
     * This task is scheduled to run on the 1st of each month at 00:05.
     */
    public void startMonthlyReportScheduler() {
        java.util.Calendar nextRun = java.util.Calendar.getInstance();
        nextRun.set(java.util.Calendar.DAY_OF_MONTH, 1);
        nextRun.set(java.util.Calendar.HOUR_OF_DAY, 0);
        nextRun.set(java.util.Calendar.MINUTE, 5);
        nextRun.set(java.util.Calendar.SECOND, 0);
        nextRun.set(java.util.Calendar.MILLISECOND, 0);
        
        // If today is the first, but time has passed, schedule for next month
        if (nextRun.getTime().before(new java.util.Date())) {
            nextRun.add(java.util.Calendar.MONTH, 1);
        }

        long periodMillis = 1000L * 60 * 60 * 24 * 30; // 30 days (approx)
        timer.scheduleAtFixedRate(new MonthlyReportTask(), nextRun.getTime(), periodMillis);
        System.out.println("Monthly report scheduler started. First run: " + nextRun.getTime());
    }
}
