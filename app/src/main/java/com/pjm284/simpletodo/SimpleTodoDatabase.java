package com.pjm284.simpletodo;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by pauljmin on 2/4/17.
 */

@Database(name = SimpleTodoDatabase.NAME, version = SimpleTodoDatabase.VERSION)
public class SimpleTodoDatabase {
    public static final String NAME = "SimpleTodoDatabase";

    public static final int VERSION = 8;
}