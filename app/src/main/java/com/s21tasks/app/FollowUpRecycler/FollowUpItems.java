package com.s21tasks.app.FollowUpRecycler;

public class FollowUpItems {
    private String followupTitle , followUpMessage , followUpDate ;

    public FollowUpItems(String followupTitle, String followUpMessage, String followUpDate) {
        this.followupTitle = followupTitle;
        this.followUpMessage = followUpMessage;
        this.followUpDate = followUpDate;
    }

    public String getFollowupTitle() {
        return followupTitle;
    }

    public String getFollowUpMessage() {
        return followUpMessage;
    }

    public String getFollowUpDate() {
        return followUpDate;
    }
}
