package bparkClient.gui;

import common.RequestType;
import common.entities.Report;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import java.util.List;

/**
 * Controller for the Late Parking Report screen.
 * Displays a pie chart summarizing total late parking durations per subscriber.
 */
public class LateParkingReportController {

    /** Pie chart used to visualize total late minutes by subscriber. */
    @FXML 
    private PieChart lateParkingPieChart;

    /**
     * Initializes the controller and generates the report for the current month.
     * Called automatically by JavaFX after the FXML is loaded.
     */
    @FXML
    public void initialize() {
        int year = java.time.LocalDate.now().getYear();
        int month = java.time.LocalDate.now().getMonthValue();
        generateLateParkingReport(year, month);
    }

    /**
     * Sends a request to generate a late parking report and displays the data in a pie chart.
     * Each slice represents the total late minutes for a specific subscriber.
     *
     * @param year  the year to generate the report for
     * @param month the month to generate the report for
     */
    private void generateLateParkingReport(int year, int month) {
        Report report = (Report) bparkClient.logic.Bparkclient.getInstance().sendAndWait(
            RequestType.GENERATE_LATE_PARKING_REPORT, new int[]{year, month}
        );

        lateParkingPieChart.getData().clear();

        if (report != null && !report.getRows().isEmpty()) {
            // Group by subscriber name and sum late durations
            java.util.Map<String, Integer> lateTotals = new java.util.HashMap<>();
            for (List<String> row : report.getRows()) {
                String name = row.get(1); // 2nd column is Subscriber Name
                int late = Integer.parseInt(row.get(3)); // 4th column is Late Duration
                lateTotals.put(name, lateTotals.getOrDefault(name, 0) + late);
            }

            for (var entry : lateTotals.entrySet()) {
                lateParkingPieChart.getData().add(
                    new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue())
                );
            }

            lateParkingPieChart.setTitle("Total Late Minutes by Subscriber");

            // Add tooltips to each pie chart slice
            for (PieChart.Data data : lateParkingPieChart.getData()) {
                String tooltipText = data.getName() + ": " + (int) data.getPieValue() + " min";
                Tooltip.install(data.getNode(), new Tooltip(tooltipText));
            }
        } else {
            lateParkingPieChart.setTitle("No data available for this month.");
        }
    }
}
