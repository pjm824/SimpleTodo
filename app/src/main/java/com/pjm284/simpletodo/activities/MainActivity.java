package com.pjm284.simpletodo.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.fragments.DeleteConfirmationAlertDialogFragment;
import com.pjm284.simpletodo.fragments.EditTaskDialogFragment;
import com.pjm284.simpletodo.fragments.NewTaskDialogFragment;
import com.pjm284.simpletodo.models.Status;
import com.pjm284.simpletodo.models.Task;
import com.pjm284.simpletodo.adapters.TasksAdapter;
import com.pjm284.simpletodo.models.Task_Table;
import com.pjm284.simpletodo.util.ItemClickSupport;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements EditTaskDialogFragment.EditTaskDialogListener, NewTaskDialogFragment.NewTaskDialogListener {

    ArrayList<Task> tasks;
    TasksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get tasks that aren't marked as done
        this.tasks = (ArrayList<Task>) SQLite.select()
                .from(Task.class)
                .where(Task_Table.status.is(Status.Todo.getDbValue()))
                .queryList();

        // Lookup the recyclerview in activity layout
        RecyclerView rvTasks = (RecyclerView) findViewById(R.id.rvTasks);

        // Create adapter for tasks
        adapter = new TasksAdapter(this, tasks);

        // Attach the adapter to the recyclerview to populate items
        rvTasks.setAdapter(adapter);

        // Set layout manager to position the items
        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        // set tasks click listeners
        ItemClickSupport.addTo(rvTasks).setOnItemClickListener(taskItemClickListener);
        ItemClickSupport.addTo(rvTasks).setOnItemLongClickListener(taskItemLongClickListener);

        // add button listener for the new task button
        Button btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(addTaskBtnListener);
    }

    /**
     * onItemLongClickListener for deleting tasks
     */
    private ItemClickSupport.OnItemLongClickListener taskItemLongClickListener = new ItemClickSupport.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
            DeleteConfirmationAlertDialogFragment deleteConfirm = DeleteConfirmationAlertDialogFragment.newInstance(position);
            deleteConfirm.show(getFragmentManager(), "dialog");
            return true;
        }
    };

    /**
     * onItemClickListener for editing tasks
     */
    private ItemClickSupport.OnItemClickListener taskItemClickListener = new ItemClickSupport.OnItemClickListener() {
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            //get the task that was selected
            Task task = tasks.get(position);

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
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishNewTaskDialog(Task task) {
        this.tasks.add(task);
        adapter.notifyDataSetChanged();
    }

    public void deleteTask(int pos) {
        Task task = tasks.remove(pos);
        task.delete();
        adapter.notifyDataSetChanged();
    }
}
