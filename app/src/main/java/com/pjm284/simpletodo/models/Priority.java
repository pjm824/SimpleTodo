package com.pjm284.simpletodo.models;

import com.pjm284.simpletodo.R;

public enum Priority {
    Low("Low", R.color.priorityBlue),
    Medium("Medium", R.color.priorityYellow),
    High("High", R.color.priorityRed);

    private final String name;
    private final int color;

    private Priority(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public int getColor() {
        return this.color;
    }
}