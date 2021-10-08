package com.lxb2.birthdayreminder;

import java.time.LocalDateTime;

public class Reminder {
    private LocalDateTime DateTime;

    public LocalDateTime getDateTime() {
        return DateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        DateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public Reminder(LocalDateTime dateTime, String name) {
        DateTime = dateTime;
        this.name = name;
    }

    //todo: remove this constructor
    public Reminder(String name) {
        this.name = name;
    }
}
