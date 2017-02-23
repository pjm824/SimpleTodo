package com.pjm284.simpletodo.models;

import com.pjm284.simpletodo.SimpleTodoDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Table(database = SimpleTodoDatabase.class)
public class Task extends BaseModel implements Serializable {

    @PrimaryKey(autoincrement = true)
    @Column
    int id;

    @Column
    String subject;

    @Column
    String priority;

    @Column
    Date dueTimestamp;

    @Column(defaultValue = "0")
    int status;

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Priority getPriority() {
        return Enum.valueOf(Priority.class, this.priority);
    }

    public void setPriority(Priority priority) {
        this.priority = priority.getName();
    }

    public Calendar getDueDate() {
        Calendar cal = new GregorianCalendar();
        if (this.dueTimestamp != null) {
            cal.setTime(this.dueTimestamp);
        }

        return cal;
    }

    public void setDueDate(Calendar cal) {
        this.dueTimestamp = cal.getTime();
    }

    public Status getStatus() {
        return Status.findByDbValue(this.status);
    }

    public void setStatus(Status status) {
        this.status = status.getDbValue();
    }
}
