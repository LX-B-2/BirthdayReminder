package com.lxb2.birthdayreminder;

import java.util.Calendar;

public class Reminder {
    private Calendar calendar;
    private String name;

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Reminder(Calendar calendar, String name) {
        this.calendar = calendar;
        this.name = name;
    }
}
