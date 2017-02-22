package com.pjm284.simpletodo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.fragments.TaskDialogFragment;
import com.pjm284.simpletodo.models.Priority;
import com.pjm284.simpletodo.models.Task;


public class NewTaskActivity extends AppCompatActivity implements TaskDialogFragment.TaskDialogListener {

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // create a new task
        task = new Task();

        // set default priority to medium
        task.setPriority(Priority.Medium);

        // set up the task fragment
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        TaskDialogFragment taskDialogFragment = TaskDialogFragment.newInstance(task);
        ft.replace(R.id.flFragmentPlaceholder, taskDialogFragment, "fragment_edit_Task");
        ft.commit();
    }

    public void onFinishTaskDialog() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("task", task);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}