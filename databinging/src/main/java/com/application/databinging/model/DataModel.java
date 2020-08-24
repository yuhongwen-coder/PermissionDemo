package com.application.databinging.model;

public class DataModel {
    private String text;

    public DataModel(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "text='" + text + '\'' +
                '}';
    }
}
