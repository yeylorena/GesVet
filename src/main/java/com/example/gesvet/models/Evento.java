package com.example.gesvet.models;

import java.time.LocalDateTime;

public class Evento {

    private String title;
    private LocalDateTime start;
    private LocalDateTime end;

    public Evento() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Evento{" + "title=" + title + ", start=" + start + ", end=" + end + '}';
    }

}
