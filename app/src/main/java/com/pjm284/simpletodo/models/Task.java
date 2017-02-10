package com.pjm284.simpletodo.models;

import android.util.Log;

import com.pjm284.simpletodo.SimpleTodoDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

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

    @Column
    Date dueTimestamp;

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Calendar getDueDate() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(this.dueTimestamp);

        return cal;
    }

    public void setDueDate(Calendar cal) {
        this.dueTimestamp = cal.getTime();
    }
}
