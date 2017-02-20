package com.pjm284.simpletodo.models;

public enum Priority {
    Low("Low", "BLUE"),
    Medium("Medium", "#E5E500"),
    High("High", "#E50000");

    private final String name;
    private final String color;

    private Priority(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }
}