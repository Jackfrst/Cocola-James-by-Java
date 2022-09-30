package james.cocola.app.HisoryRecycler;

public class HistoryItems {
    private String historyImage , historyTitle , historyDate , historyShortDesc , location , issueNo , status ;

    public HistoryItems(String historyImage, String historyTitle, String historyDate, String historyShortDesc, String location, String issueNo, String status) {
        this.historyImage = historyImage;
        this.historyTitle = historyTitle;
        this.historyDate = historyDate;
        this.historyShortDesc = historyShortDesc;
        this.location = location;
        this.issueNo = issueNo;
        this.status = status;
    }

    public String getHistoryImage() {
        return historyImage;
    }

    public String getHistoryTitle() {
        return historyTitle;
    }

    public String getHistoryDate() {
        return historyDate;
    }

    public String getHistoryShortDesc() {
        return historyShortDesc;
    }

    public String getLocation() {
        return location;
    }

    public String getIssueNo() {
        return issueNo;
    }

    public String getStatus() {
        return status;
    }
}
