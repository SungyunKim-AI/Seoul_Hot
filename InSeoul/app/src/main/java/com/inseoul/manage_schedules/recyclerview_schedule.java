package com.inseoul.manage_schedules;

public class recyclerview_schedule {
    private String schedule_title;
    private String schedule_date;

    public String getSchedule_title() {
        return schedule_title;
    }

    public void setSchedule_title(String schedule_title) {
        this.schedule_title = schedule_title;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public recyclerview_schedule(String schedule_title, String schedule_date) {
        this.schedule_title = schedule_title;
        this.schedule_date = schedule_date;
    }
}
