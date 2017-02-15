package com.pjm284.simpletodo.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.fragments.DeleteConfirmationAlertDialogFragment;
import com.pjm284.simpletodo.fragments.EditTaskDialogFragment;
import com.pjm284.simpletodo.fragments.NewTaskDialogFragment;
import com.pjm284.simpletodo.models.Task;
import com.pjm284.simpletodo.adapters.TasksAdapter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditTaskDialogFragment.EditTaskDialogListener, NewTaskDialogFragment.NewTaskDialogListener {

    ArrayList<Task> tasks;
    TasksAdapter tasksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Construct the data source
        this.tasks = (ArrayList<Task>) SQLite.select().from(Task.class).queryList();

        // Create the adapter to convert the array to views
        this.tasksAdapter = new TasksAdapter(this, tasks);

        // Attach the adapter to a ListView
        ListView lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(this.tasksAdapter);

        // Set listview listeners
        lvItems.setOnItemClickListener(taskItemClickListener);
        lvItems.setOnItemLongClickListener(taskItemLongClickListener);

        // Set button listener
        Button btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(addTaskBtnListener);
    }

    /**
     * onItemLongClickListener for deleting tasks
     */
    private AdapterView.OnItemLongClickListener taskItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
            DeleteConfirmationAlertDialogFragment deleteConfirm = DeleteConfirmationAlertDialogFragment.newInstance(pos);
            deleteConfirm.show(getFragmentManager(), "dialog");
            return true;
        }
    };

    /**
     * onItemClickListener for editing tasks
     */
    private AdapterView.OnItemClickListener taskItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
            //get the task that was selected
            Task task = tasks.get(pos);

            FragmentManager fm = getSupportFragmentManager();
            EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance("Edit Task", task);
            editTaskDialogFragment.show(fm, "fragment_edit_Task");
        }
    };

    /**
     * onClickListener for add task button
     */
    private View.OnClickListener addTaskBtnListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentManager fm = getSupportFragmentManager();
            NewTaskDialogFragment newTaskDialogFragment = NewTaskDialogFragment.newInstance("Add Task");
            newTaskDialogFragment.show(fm, "fragment_edit_Task");
        }
    };

    @Override
    public void onFinishEditTaskDialog() {
        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishNewTaskDialog(Task task) {
        this.tasks.add(task);
        tasksAdapter.notifyDataSetChanged();
    }

    public void deleteTask(int pos) {
        Task task = tasks.remove(pos);
        task.delete();
        tasksAdapter.notifyDataSetChanged();
    }
}
