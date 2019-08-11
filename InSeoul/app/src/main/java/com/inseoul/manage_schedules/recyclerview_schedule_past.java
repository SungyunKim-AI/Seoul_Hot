package com.inseoul.manage_schedules;

public class recyclerview_schedule_past {
    private String schedule_title_past;
    private String schedule_date_past;

    public String getSchedule_title_past() {
        return schedule_title_past;
    }

    public void setSchedule_title_past(String schedule_title_past) {
        this.schedule_title_past = schedule_title_past;
    }

    public String getSchedule_date_past() {
        return schedule_date_past;
    }

    public void setSchedule_date_past(String schedule_date_past) {
        this.schedule_date_past = schedule_date_past;
    }

    public recyclerview_schedule_past(String schedule_title_past, String schedule_date_past) {
        this.schedule_title_past = schedule_title_past;
        this.schedule_date_past = schedule_date_past;
    }
}
