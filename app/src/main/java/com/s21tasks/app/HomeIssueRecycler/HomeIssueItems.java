package com.s21tasks.app.HomeIssueRecycler;

public class HomeIssueItems {
    private String issueCode , issueName , issueImage;

    public HomeIssueItems(String issueCode, String issueName, String issueImage) {
        this.issueCode = issueCode;
        this.issueName = issueName;
        this.issueImage = issueImage;
    }

    public String getIssueCode() {
        return issueCode;
    }

    public String getIssueName() {
        return issueName;
    }

    public String getIssueImage() {
        return issueImage;
    }
}
