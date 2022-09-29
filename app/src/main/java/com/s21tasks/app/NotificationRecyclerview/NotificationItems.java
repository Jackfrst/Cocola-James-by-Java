package com.s21tasks.app.NotificationRecyclerview;

public class NotificationItems {
    private String notificationTitle , notificationDesc , notificationDate ;

    public NotificationItems(String notificationTitle, String notificationDesc, String notificationDate) {
        this.notificationTitle = notificationTitle;
        this.notificationDesc = notificationDesc;
        this.notificationDate = notificationDate;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationDesc() {
        return notificationDesc;
    }

    public String getNotificationDate() {
        return notificationDate;
    }
}
