package com.pjm284.simpletodo.models;

import com.pjm284.simpletodo.SimpleTodoDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by pauljmin on 1/31/17.
 */

@Table(database = SimpleTodoDatabase.class)
public class Task extends BaseModel {

    @PrimaryKey(autoincrement = true)
    @Column
    int id;

    @Column
    String subject;

    @Column
    String priority;

    public String getSubject() {
        return this.subject;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
