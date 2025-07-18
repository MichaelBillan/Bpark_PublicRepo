package common.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Report implements Serializable {
    private final LocalDateTime reportDate;
    private final LocalDateTime generatedOn;
    private final String title;
    private final List<String> columnHeaders;
    private final List<List<String>> rows;

    public Report(LocalDateTime reportDate, LocalDateTime generatedOn,
                  String title, List<String> columnHeaders, List<List<String>> rows) {
        this.reportDate = reportDate;
        this.generatedOn = generatedOn;
        this.title = title;
        this.columnHeaders = columnHeaders;
        this.rows = rows;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public LocalDateTime getGeneratedOn() {
        return generatedOn;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return "Report [title=" + title +
               ", reportDate=" + reportDate +
               ", generatedOn=" + generatedOn +
               ", columns=" + columnHeaders +
               ", rows=" + rows.size() + "]";
    }
}
