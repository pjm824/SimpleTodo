package com.pjm284.simpletodo.models;

import android.util.SparseArray;

public enum Status {
    Todo("Todo", 0),
    Done("Done", 1);

    private final String name;
    private final int dbValue;
    private static final SparseArray<Status> map;

    private Status(String name, int dbValue) {
        this.name = name;
        this.dbValue = dbValue;
    }

    static {
        map = new SparseArray<Status>();
        for (Status v : Status.values()) {
            map.put(v.dbValue, v);
        }
    }

    public static Status findByDbValue(int i) {
        return map.get(i);
    }

    public String getName() {
        return this.name;
    }

    public int getDbValue() {
        return this.dbValue;
    }

}
