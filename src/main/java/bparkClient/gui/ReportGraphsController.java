package bparkClient.gui;

import bparkClient.logic.ReportService;
import common.entities.Report;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**

 * Controller for the report graphs and tables in the client GUI.
 * <p>
 * Handles the generation, retrieval, and display of various parking lot reports,
 * including session duration, peak usage, spot utilization, top users, top extenders, and late parking reports.
 * Manages user interactions for selecting report types, months, and years, and updates the GUI accordingly.
 * </p>

 */
public class ReportGraphsController {

    @FXML private BarChart<String, Number> usageBarChart;
    @FXML private ComboBox<String> graphSelector;
    @FXML private Button backBtn;
    @FXML private BarChart<String, Number> topUsersBarChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private ComboBox<Integer> monthComboBox;
    @FXML private ComboBox<Integer> yearComboBox;
    @FXML private Button generateReportBtn;
    @FXML private Button homeBtn;
    @FXML private PieChart durationPieChart;
    @FXML private LineChart<String, Number> peakUsageLineChart;
    @FXML private CategoryAxis peakUsageXAxis;
    @FXML private NumberAxis peakUsageYAxis;
    @FXML private BarChart<String, Number> spotUtilizationBarChart;
    @FXML private CategoryAxis spotUtilizationXAxis;
    @FXML private NumberAxis spotUtilizationYAxis;
    @FXML private TabPane reportTabPane;
    @FXML private ComboBox<Integer> lateParkingMonthCombo;
    @FXML private ComboBox<Integer> lateParkingYearCombo;
    @FXML private TableView<List<String>> lateParkingTable;
    @FXML private TabPane topUsersTabPane;
    @FXML private TableView<List<String>> topExtendersTable;
    @FXML private ComboBox<Integer> extendersMonthComboBox;
    @FXML private ComboBox<Integer> extendersYearComboBox;
    @FXML private Button generateExtendersReportBtn;
    @FXML private BarChart<String, Number> topExtendersBarChart;
    @FXML private CategoryAxis extendersXAxis;
    @FXML private NumberAxis extendersYAxis;

    private final ReportService reportService = new ReportService();

    /**

     * Initializes the controller after its root element has been completely processed.
     * Sets up UI components, event handlers, and default report displays.

     */
    @FXML
    public void initialize() {
        if (graphSelector != null) {
            setupComboBox();
        }
        setupTopUsersReportUI();
        setupTabPaneListener();
        generateReportBtn.setOnAction(e -> handleGenerateReport());
        setupLateParkingReportUI();
        setupTopExtendersReportUI();
        if (generateExtendersReportBtn != null) {
            generateExtendersReportBtn.setOnAction(e -> handleGenerateExtendersReport());
        }
        if (topExtendersBarChart != null && extendersXAxis != null && extendersYAxis != null) {
            topExtendersBarChart.setCategoryGap(10);
            topExtendersBarChart.setBarGap(5);
            topExtendersBarChart.setAnimated(false);
            topExtendersBarChart.setTitle("Top Extenders (Most Extensions)");
            topExtendersBarChart.setLegendVisible(false);
            topExtendersBarChart.setHorizontalGridLinesVisible(true);
            topExtendersBarChart.setVerticalGridLinesVisible(false);
            // Axes are set via FXML, do not call setXAxis/setYAxis
        }
    }

    /**

     * Sets up the graph selector ComboBox for switching between different report types.

     */
    private void setupComboBox() {
        if (graphSelector == null) return;
        ObservableList<String> options = FXCollections.observableArrayList(
                "Session Duration Distribution",
                "Peak Usage Times",
                "Spot Utilization"
        );
        graphSelector.setItems(options);
        graphSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadAndShowReport(newVal);
            }
        });
        graphSelector.getSelectionModel().selectFirst();
    }

    /**

     * Sets up the month/year ComboBoxes for the Top Users report tab.

     */
    private void setupTopUsersReportUI() {
        if (monthComboBox != null && yearComboBox != null && generateReportBtn != null) {
            monthComboBox.setItems(FXCollections.observableArrayList(java.util.stream.IntStream.rangeClosed(1, 12).boxed().toList()));
            int currentYear = java.time.Year.now().getValue();
            yearComboBox.setItems(FXCollections.observableArrayList(currentYear - 1, currentYear, currentYear + 1));
        }
    }

    /**

     * Sets up a listener for the reportTabPane to load the appropriate report when the tab changes.

     */
    private void setupTabPaneListener() {
        if (reportTabPane != null) {
            reportTabPane.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
                switch (newVal.intValue()) {
                    case 0 -> reportService.fetchSessionDurationDistributionReportAsync(this::showSessionDurationDistributionChart, this::showError);
                    case 1 -> reportService.fetchPeakUsageTimesReportAsync(this::showPeakUsageTimesChart, this::showError);
                    case 2 -> reportService.fetchSpotUtilizationReportAsync(this::showSpotUtilizationChart, this::showError);
                }
            });
            reportService.fetchSessionDurationDistributionReportAsync(this::showSessionDurationDistributionChart, this::showError);
        }
    }

    /**

     * Loads and displays the report corresponding to the selected type in the graph selector.
     * @param type The type of report to display (e.g., "Session Duration Distribution").

     */
    private void loadAndShowReport(String type) {
        switch (type) {
            case "Session Duration Distribution" ->
                reportService.fetchSessionDurationDistributionReportAsync(this::showSessionDurationDistributionChart, this::showError);
            case "Peak Usage Times" ->
                reportService.fetchPeakUsageTimesReportAsync(this::showPeakUsageTimesChart, this::showError);
            case "Spot Utilization" ->
                reportService.fetchSpotUtilizationReportAsync(this::showSpotUtilizationChart, this::showError);
        }
    }

    /**

     * Displays the session duration distribution report as a pie chart.
     * @param report The report data to display.

     */
    private void showSessionDurationDistributionChart(Report report) {
        if (durationPieChart == null) return;
        durationPieChart.getData().clear();
        List<List<String>> sortedRows = report.getRows().stream()
            .filter(row -> Integer.parseInt(row.get(1)) > 0)
            .sorted((a, b) -> Integer.parseInt(b.get(1)) - Integer.parseInt(a.get(1)))
            .limit(5)
            .toList();
        for (List<String> row : sortedRows) {
            PieChart.Data slice = new PieChart.Data(row.get(0), Integer.parseInt(row.get(1)));
            durationPieChart.getData().add(slice);
        }
        durationPieChart.setTitle("Session Duration Distribution");
        durationPieChart.setLabelsVisible(true);
        durationPieChart.setClockwise(true);
        durationPieChart.setStartAngle(90);

        javafx.application.Platform.runLater(() -> {
            durationPieChart.lookupAll(".chart-pie-label").forEach(label ->
                label.setStyle("-fx-text-fill: #f0f0f0; -fx-font-weight: bold;")
            );
            durationPieChart.lookupAll(".chart-pie-label, Text").forEach(node -> {
                if (node instanceof javafx.scene.text.Text textNode) {
                    textNode.setFill(javafx.scene.paint.Color.web("#f0f0f0"));
                    textNode.setStyle("-fx-font-weight: bold;");
                }
            });
            for (PieChart.Data data : durationPieChart.getData()) {
                String tooltipText = ReportService.getSessionDurationTooltip(data.getName(), (int) data.getPieValue());
                Tooltip.install(data.getNode(), new Tooltip(tooltipText));
            }
        });
        if (reportTabPane != null) reportTabPane.getSelectionModel().select(0);
    }

    /**

     * Displays the peak usage times report as a line chart.
     * @param report The report data to display.

     */
    private void showPeakUsageTimesChart(Report report) {
        if (peakUsageLineChart == null) return;
        peakUsageLineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<List<String>> sortedRows = report.getRows().stream()
            .sorted((a, b) -> Integer.parseInt(b.get(1)) - Integer.parseInt(a.get(1)))
            .limit(5)
            .toList();
        List<String> categories = sortedRows.stream().map(row -> row.get(0)).toList();
        peakUsageXAxis.setCategories(FXCollections.observableArrayList(categories));
        for (List<String> row : sortedRows) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(row.get(0), Integer.parseInt(row.get(1)));
            series.getData().add(data);
        }
        peakUsageLineChart.getData().add(series);
        peakUsageLineChart.setTitle("Peak Usage Times");
        peakUsageXAxis.setLabel("Hour of Day");
        peakUsageYAxis.setLabel("Session Count");

        javafx.application.Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : series.getData()) {
                String tooltipText = ReportService.getPeakUsageTooltip(data.getXValue(), data.getYValue().intValue());
                Tooltip.install(data.getNode(), new Tooltip(tooltipText));
            }
        });
        if (reportTabPane != null) reportTabPane.getSelectionModel().select(1);
    }

    /**
     * Displays the spot utilization report as a bar chart.
     * @param report The report data to display.

     */
    private void showSpotUtilizationChart(Report report) {
        if (spotUtilizationBarChart == null) return;
        spotUtilizationBarChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<List<String>> sortedRows = report.getRows().stream()
            .sorted((a, b) -> Integer.parseInt(b.get(1)) - Integer.parseInt(a.get(1)))
            .limit(5)
            .toList();
        List<String> categories = sortedRows.stream().map(row -> row.get(0)).toList();
        spotUtilizationXAxis.setCategories(FXCollections.observableArrayList(categories));
        for (List<String> row : sortedRows) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(row.get(0), Integer.parseInt(row.get(1)));
            series.getData().add(data);
        }
        spotUtilizationBarChart.getData().add(series);
        spotUtilizationBarChart.setTitle("Spot Utilization (Top 5)");
        spotUtilizationXAxis.setLabel("Spot ID");
        spotUtilizationYAxis.setLabel("Session Count");

        javafx.application.Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : series.getData()) {
                String tooltipText = ReportService.getSpotUtilizationTooltip(data.getXValue(), data.getYValue().intValue());
                Tooltip.install(data.getNode(), new Tooltip(tooltipText));
            }
        });
        if (reportTabPane != null) reportTabPane.getSelectionModel().select(2);
    }

    /**

     * Handles the click event for the "Generate Report" button in the Top Users tab.
     * Fetches and displays the selected report for the chosen month and year.

     */
    @FXML
    private void handleGenerateReport() {
        Integer month = monthComboBox.getValue();
        Integer year = yearComboBox.getValue();
        if (month == null || year == null) {
            showAlert("Please select both month and year.");
            return;
        }

        if (reportTabPane == null) {
            System.out.println("[TopUsersReport] Fetching top users for year=" + year + ", month=" + month);
            topUsersBarChart.getData().clear();
            reportService.fetchTopUsersForMonthAsync(year, month, rawResult -> {
                System.out.println("[TopUsersReport] Received " + rawResult.size() + " results");
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                int count = 0;
                for (XYChart.Data<String, Number> data : reportService.toChartData(rawResult)) {
                    if (count++ >= 3) break;
                    series.getData().add(data);
                }
                topUsersBarChart.getData().clear();
                topUsersBarChart.getData().add(series);
                topUsersBarChart.setCategoryGap(20);

                javafx.application.Platform.runLater(() -> {
                    for (XYChart.Data<String, Number> data : series.getData()) {
                        String tooltipText = data.getXValue() + ": " + data.getYValue().intValue() + " sessions";
                        Tooltip.install(data.getNode(), new Tooltip(tooltipText));
                    }
                });
            }, error -> {
                System.err.println("[TopUsersReport] Error: " + error.getMessage());
                showAlert("Failed to load top users report: " + error.getMessage());
            });
            return;
        }

        int selectedTab = reportTabPane.getSelectionModel().getSelectedIndex();
        switch (selectedTab) {
            case 0 -> reportService.fetchSessionDurationDistributionReportForMonthAsync(year, month, this::showSessionDurationDistributionChart, this::showError);
            case 1 -> reportService.fetchPeakUsageTimesReportForMonthAsync(year, month, this::showPeakUsageTimesChart, this::showError);
            case 2 -> reportService.fetchSpotUtilizationReportForMonthAsync(year, month, this::showSpotUtilizationChart, this::showError);
        }
    }

    /**

     * Sets up the month/year ComboBoxes for the Late Parking report tab.
     */
    private void setupLateParkingReportUI() {
        if (lateParkingMonthCombo != null && lateParkingYearCombo != null) {
            lateParkingMonthCombo.setItems(FXCollections.observableArrayList(
                java.util.stream.IntStream.rangeClosed(1, 12).boxed().toList()
            ));
            int currentYear = java.time.Year.now().getValue();
            lateParkingYearCombo.setItems(FXCollections.observableArrayList(
                currentYear - 1, currentYear, currentYear + 1
            ));
            lateParkingMonthCombo.getSelectionModel().select(Integer.valueOf(java.time.LocalDate.now().getMonthValue()) - 1);
            lateParkingYearCombo.getSelectionModel().select(1); // current year
        }
    }

    /**
     * Handles the click event for the "Generate Report" button in the Late Parking tab.
     * Fetches and displays the late parking report for the chosen month and year.
     */
    @FXML
    private void handleGenerateLateParkingReport() {
        Integer month = lateParkingMonthCombo.getValue();
        Integer year = lateParkingYearCombo.getValue();
        if (month == null || year == null) return;
        generateLateParkingReport(year, month);
    }

    /**
     * Fetches and displays the late parking report for the given year and month.
     * @param year The year for the report.
     * @param month The month for the report.
     */
    private void generateLateParkingReport(int year, int month) {
        Report report = (Report) bparkClient.logic.Bparkclient.getInstance().sendAndWait(
            common.RequestType.GENERATE_LATE_PARKING_REPORT, new int[]{year, month}
        );
        lateParkingTable.getColumns().clear();
        lateParkingTable.getItems().clear();
        if (report != null) {
            List<String> headers = report.getColumnHeaders();
            for (int i = 0; i < headers.size(); i++) {
                final int colIndex = i;
                TableColumn<List<String>, String> col = new TableColumn<>(headers.get(i));
                col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(colIndex)));
                lateParkingTable.getColumns().add(col);
            }
            for (List<String> row : report.getRows()) {
                lateParkingTable.getItems().add(row);
            }
        } else {
            lateParkingTable.setPlaceholder(new Label("No data available for this month."));
        }
    }

    /**
     * Switches to the Late Parking report tab in the reportTabPane.
     */
    public void showLateParkingReportTab() {
        if (reportTabPane != null) {
            reportTabPane.getSelectionModel().select(3); // Assuming it's the 4th tab (index 3)
        }
    }

    /**
     * Sets up the month/year ComboBoxes for the Top Extenders report tab.
     */
    private void setupTopExtendersReportUI() {
        if (extendersMonthComboBox != null && extendersYearComboBox != null) {
            extendersMonthComboBox.setItems(FXCollections.observableArrayList(java.util.stream.IntStream.rangeClosed(1, 12).boxed().toList()));
            int currentYear = java.time.Year.now().getValue();
            extendersYearComboBox.setItems(FXCollections.observableArrayList(currentYear - 1, currentYear, currentYear + 1));
            extendersMonthComboBox.getSelectionModel().select(java.time.LocalDate.now().getMonthValue() - 1);
            extendersYearComboBox.getSelectionModel().select(1);
        }
    }

    /**
     * Displays the Top Extenders report in a table.
     * @param report The report data to display.
     */
    private void showTopExtendersTable(Report report) {
        topExtendersTable.getColumns().clear();
        topExtendersTable.getItems().clear();
        if (report != null) {
            List<String> headers = report.getColumnHeaders();
            for (int i = 0; i < headers.size(); i++) {
                final int colIndex = i;
                TableColumn<List<String>, String> col = new TableColumn<>(headers.get(i));
                col.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(colIndex)));
                topExtendersTable.getColumns().add(col);
            }
            for (List<String> row : report.getRows()) {
                topExtendersTable.getItems().add(row);
            }
        } else {
            topExtendersTable.setPlaceholder(new Label("No data available for this month."));
        }
        // Ensure the Top Extenders tab is selected
        if (topUsersTabPane != null) {
            topUsersTabPane.getSelectionModel().select(1); // 1 = Top Extenders tab
        }
    }

    /**
     * Handles the click event for the "Generate Report" button in the Top Extenders tab.
     * Fetches and displays the Top Extenders report for the chosen month and year.
     */
    @FXML
    private void handleGenerateExtendersReport() {
        Integer month = extendersMonthComboBox.getValue();
        Integer year = extendersYearComboBox.getValue();
        if (month == null || year == null) return;
        reportService.fetchTopExtendersReportAsync(year, month, this::showTopExtendersTable, this::showError);
    }

    /**
     * Displays an error message if a report fails to load.
     * @param error The error that occurred.

     */
    private void showError(Throwable error) {
        boolean allEmpty = (durationPieChart == null || durationPieChart.getData().isEmpty()) &&
                           (peakUsageLineChart == null || peakUsageLineChart.getData().isEmpty()) &&
                           (spotUtilizationBarChart == null || spotUtilizationBarChart.getData().isEmpty());
        if (allEmpty) {
            showAlert("Failed to load report: " + error.getMessage());
        }
    }

    /**
     * Handles the click event for the "Back" button.
     * Navigates the user to the previous screen.
     */
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/ParkingLotManagerReportsPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) (backBtn != null ? backBtn.getScene().getWindow() : topUsersBarChart.getScene().getWindow());
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to return to previous screen.");
        }
    }

    /**

     * Handles the click event for the "Home" button.
     * Navigates the user to the home screen.

     */
    @FXML
    private void handleHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bparkClient/fxml/AccessSelection.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) (homeBtn != null ? homeBtn.getScene().getWindow() : topUsersBarChart.getScene().getWindow());
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Failed to return to home screen.");
        }
    }

    /**
     * Shows an informational alert dialog with the given message.
     * @param message The message to display.

     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Report Viewer");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}