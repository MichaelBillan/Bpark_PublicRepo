package bparkServer.scheduler;

import bparkServer.db.DataBaseController;
import common.entities.Report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.TimerTask;

/**
 * A task that runs monthly to generate and save parking-related reports as CSV files.
 * The reports include session duration distribution, peak usage times, and spot utilization.
 */
public class MonthlyReportTask extends TimerTask {
    private static final String REPORTS_DIR = "src/main/resources/bparkServer/reports/";

    /**
     * Executes the task. Calculates the previous month and triggers report generation and saving.
     * This method is automatically called by the timer scheduler.
     */
    @Override
    public void run() {
        // Get previous month and year
        LocalDate now = LocalDate.now();
        LocalDate prevMonth = now.minusMonths(1);
        int year = prevMonth.getYear();
        int month = prevMonth.getMonthValue();
        System.out.println("Generating monthly reports for: " + year + "-" + month);
        try {
            saveReportAsCsv(DataBaseController.generateSessionDurationDistributionReportForMonth(year, month), "SessionDuration", year, month);
            saveReportAsCsv(DataBaseController.generatePeakUsageTimesReportForMonth(year, month), "PeakUsage", year, month);
            saveReportAsCsv(DataBaseController.generateSpotUtilizationReportForMonth(year, month), "SpotUtilization", year, month);
            saveReportAsCsv(DataBaseController.generateLateParkingReportForMonth(year, month), "LateParking", year, month);
            System.out.println("Monthly reports generated and saved.");
        } catch (Exception e) {
            System.err.println("Failed to generate monthly reports: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saves a given report as a CSV file in the reports directory.
     *
     * @param report     the report data to save
     * @param reportType the type of the report (e.g., "SessionDuration")
     * @param year       the report year
     * @param month      the report month
     * @throws IOException if writing to the file fails
     */
    private void saveReportAsCsv(Report report, String reportType, int year, int month) throws IOException {
        String fileName = String.format("%s_%d_%02d.csv", reportType, year, month);
        File dir = new File(REPORTS_DIR);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, fileName);
        try (FileWriter writer = new FileWriter(file)) {
            // Write headers
            writer.write(String.join(",", report.getColumnHeaders()));
            writer.write("\n");
            // Write rows
            for (var row : report.getRows()) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
        }
    }
}
