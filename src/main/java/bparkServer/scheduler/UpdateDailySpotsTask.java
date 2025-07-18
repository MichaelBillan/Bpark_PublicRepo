package bparkServer.scheduler;

import java.time.LocalDate;
import java.util.TimerTask;
import bparkServer.db.DataBaseController;

/** Runs once a day at 00:01 and refreshes the parking-spot table. */
class UpdateDailySpotsTask extends TimerTask {
    @Override
    public void run() {
        // Always use the server’s current date
        DataBaseController.refreshDailySpotStatuses(LocalDate.now());
    }
}
