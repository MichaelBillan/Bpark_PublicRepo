package bparkClient.logic;

import common.entities.Report;
import common.RequestType;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart;

import java.util.function.Consumer;
import java.util.List;

/**

 * Service class for fetching and processing parking lot reports from the server.
 * Provides asynchronous methods for retrieving various reports and utility methods for chart data and tooltips.
 */
public class ReportService {
    /**
     * Fetches the session duration distribution report asynchronously (all-time).
     * @param onSuccess Callback to handle the Report result.
     * @param onError Callback to handle errors.

     */
    public void fetchSessionDurationDistributionReportAsync(Consumer<Report> onSuccess, Consumer<Throwable> onError) {
        Task<Report> task = new Task<>() {
            @Override
            protected Report call() {
                Object response = Bparkclient.getInstance().sendAndWait(RequestType.GENERATE_SESSION_DURATION_DISTRIBUTION, null);
                if (response instanceof Report) {
                    return (Report) response;
                } else {
                    throw new RuntimeException("Unexpected response for session duration distribution: " + response);
                }
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept(task.getException()));
        new Thread(task).start();
    }

    /**

     * Fetches the peak usage times report asynchronously (all-time).
     * @param onSuccess Callback to handle the Report result.
     * @param onError Callback to handle errors.

     */
    public void fetchPeakUsageTimesReportAsync(Consumer<Report> onSuccess, Consumer<Throwable> onError) {
        Task<Report> task = new Task<>() {
            @Override
            protected Report call() {
                Object response = Bparkclient.getInstance().sendAndWait(RequestType.GENERATE_PEAK_USAGE_TIMES, null);
                if (response instanceof Report) {
                    return (Report) response;
                } else {
                    throw new RuntimeException("Unexpected response for peak usage times: " + response);
                }
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept(task.getException()));
        new Thread(task).start();
    }

    /**

     * Fetches the spot utilization report asynchronously (all-time).
     * @param onSuccess Callback to handle the Report result.
     * @param onError Callback to handle errors.

     */
    public void fetchSpotUtilizationReportAsync(Consumer<Report> onSuccess, Consumer<Throwable> onError) {
        Task<Report> task = new Task<>() {
            @Override
            protected Report call() {
                Object response = Bparkclient.getInstance().sendAndWait(RequestType.GENERATE_SPOT_UTILIZATION, null);
                if (response instanceof Report) {
                    return (Report) response;
                } else {
                    throw new RuntimeException("Unexpected response for spot utilization: " + response);
                }
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept(task.getException()));
        new Thread(task).start();
    }

    /**
     * Fetches the top users (subscribers with most parking sessions) for a given month asynchronously.
     * @param year The year for the report.
     * @param month The month for the report.
     * @param onSuccess Callback to handle the List<Object[]> result, where each Object[] is [Subscriber, Integer sessionCount].
     * @param onError Callback to handle errors.

     */
    public void fetchTopUsersForMonthAsync(int year, int month, Consumer<List<Object[]>> onSuccess, Consumer<Throwable> onError) {
        Task<List<Object[]>> task = new Task<>() {
            @Override
            protected List<Object[]> call() {
                Object response = Bparkclient.getInstance().sendAndWait(RequestType.GENERATE_SUBSCRIBER_REPORT, new int[]{year, month});
                if (response instanceof List<?>) {
                    return (List<Object[]>) response;
                } else {
                    throw new RuntimeException("Unexpected response for top users report: " + response);
                }
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept(task.getException()));
        new Thread(task).start();
    }

    /**
     * Converts the raw result from fetchTopUsersForMonthAsync into chart data for a BarChart.
     * @param rawResult List<Object[]> where each Object[] is [Subscriber, Integer sessionCount].
     * @return List of XYChart.Data<String, Number> representing subscriber name and session count
     */
    public List<XYChart.Data<String, Number>> toChartData(List<Object[]> rawResult) {
        List<XYChart.Data<String, Number>> chartData = new java.util.ArrayList<>();
        for (Object[] entry : rawResult) {
            common.entities.Subscriber sub = (common.entities.Subscriber) entry[0];
            int count = (Integer) entry[1];
            chartData.add(new XYChart.Data<>(sub.getName(), count));
        }
        return chartData;
    }

    /**
     * Generates a tooltip string for a session duration pie chart data point.
     * 
     * @param label The duration range label (e.g., "31-60")
     * @param count The session count
     * @return Tooltip string
     */
    public static String getSessionDurationTooltip(String label, int count) {
        return "Duration: " + label + " min\nSessions: " + count;
    }

    /**
     * Generates a tooltip string for a peak usage line chart data point.
     * 
     * @param hour The hour label (e.g., "14:00")
     * @param count The session count
     * @return Tooltip string
     */
    public static String getPeakUsageTooltip(String hour, int count) {
        return "Hour: " + hour + "\nSessions: " + count;
    }

    /**
     * Generates a tooltip string for a spot utilization bar chart data point.
     * 
     * @param spotId The spot ID
     * @param count The session count
     * @return Tooltip string
     */
    public static String getSpotUtilizationTooltip(String spotId, int count) {
        return "Spot ID: " + spotId + "\nSessions: " + count;
    }

    /**
     * Fetches the session duration distribution report for a specific month asynchronously.
     * @param year The year for the report.
     * @param month The month for the report.
     * @param onSuccess Callback to handle the Report result.
     * @param onError Callback to handle errors.

     */
    public void fetchSessionDurationDistributionReportForMonthAsync(int year, int month, Consumer<Report> onSuccess, Consumer<Throwable> onError) {
        Task<Report> task = new Task<>() {
            @Override
            protected Report call() {
                Object response = Bparkclient.getInstance().sendAndWait(RequestType.GENERATE_SESSION_DURATION_DISTRIBUTION, new int[]{year, month});
                if (response instanceof Report) {
                    return (Report) response;
                } else {
                    throw new RuntimeException("Unexpected response for session duration distribution: " + response);
                }
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept(task.getException()));
        new Thread(task).start();
    }

    /**
     * Fetches the peak usage times report for a specific month asynchronously.
     * @param year The year for the report.
     * @param month The month for the report.
     * @param onSuccess Callback to handle the Report result.
     * @param onError Callback to handle errors.
     */
    public void fetchPeakUsageTimesReportForMonthAsync(int year, int month, Consumer<Report> onSuccess, Consumer<Throwable> onError) {
        Task<Report> task = new Task<>() {
            @Override
            protected Report call() {
                Object response = Bparkclient.getInstance().sendAndWait(RequestType.GENERATE_PEAK_USAGE_TIMES, new int[]{year, month});
                if (response instanceof Report) {
                    return (Report) response;
                } else {
                    throw new RuntimeException("Unexpected response for peak usage times: " + response);
                }
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept(task.getException()));
        new Thread(task).start();
    }

    /**
     * Fetches the spot utilization report for a specific month asynchronously.
     * @param year The year for the report.
     * @param month The month for the report.
     * @param onSuccess Callback to handle the Report result.
     * @param onError Callback to handle errors.
     */
    public void fetchSpotUtilizationReportForMonthAsync(int year, int month, Consumer<Report> onSuccess, Consumer<Throwable> onError) {
        Task<Report> task = new Task<>() {
            @Override
            protected Report call() {
                Object response = Bparkclient.getInstance().sendAndWait(RequestType.GENERATE_SPOT_UTILIZATION, new int[]{year, month});
                if (response instanceof Report) {
                    return (Report) response;
                } else {
                    throw new RuntimeException("Unexpected response for spot utilization: " + response);
                }
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept(task.getException()));
        new Thread(task).start();
    }


    /**
     * Fetches the top extenders (subscribers with most session extensions) for a given month asynchronously.
     * @param year The year for the report.
     * @param month The month for the report.
     * @param onSuccess Callback to handle the Report result.
     * @param onError Callback to handle errors.
     */
    public void fetchTopExtendersReportAsync(int year, int month, Consumer<Report> onSuccess, Consumer<Throwable> onError) {
        Task<Report> task = new Task<>() {
            @Override
            protected Report call() {
                Object response = Bparkclient.getInstance().sendAndWait(RequestType.GENERATE_TOP_EXTENDERS_REPORT, new int[]{year, month});
                if (response instanceof Report) {
                    return (Report) response;
                } else {
                    throw new RuntimeException("Unexpected response for top extenders report: " + response);
                }
            }
        };
        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        task.setOnFailed(e -> onError.accept(task.getException()));
        new Thread(task).start();
    }
} 

