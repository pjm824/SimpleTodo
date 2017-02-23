package com.pjm284.simpletodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.models.Status;
import com.pjm284.simpletodo.models.Task;
import com.pjm284.simpletodo.adapters.TasksAdapter;
import com.pjm284.simpletodo.models.Task_Table;
import com.pjm284.simpletodo.util.ItemClickSupport;
import com.pjm284.simpletodo.util.TaskTouchHelper;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int NEW_TASK_REQUEST = 1;
    final int EDIT_TASK_REQUEST = 2;

    ArrayList<Task> tasks;
    TasksAdapter adapter;
    Button btnAddItem;

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

        // set up touch helper for swipe to delete/done
        ItemTouchHelper.Callback callback = new TaskTouchHelper(adapter, this);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rvTasks);

        // set tasks click listeners
        ItemClickSupport.addTo(rvTasks).setOnItemClickListener(taskItemClickListener);

        // add button listener for the new task button
        btnAddItem = (Button) findViewById(R.id.btnAddItem);
        btnAddItem.setOnClickListener(addTaskBtnListener);
    }

    /**
     * onItemClickListener for editing tasks
     */
    private ItemClickSupport.OnItemClickListener taskItemClickListener = new ItemClickSupport.OnItemClickListener() {
        @Override
        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
            Intent i = new Intent(MainActivity.this, EditTaskActivity.class);
            Task task = tasks.get(position);
            i.putExtra("position", position);
            i.putExtra("task", task);
            startActivityForResult(i, EDIT_TASK_REQUEST);
        }
    };

    /**
     * onClickListener for add task button
     */
    private View.OnClickListener addTaskBtnListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, NewTaskActivity.class);
            startActivityForResult(i, NEW_TASK_REQUEST);
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            Task task = (Task) data.getSerializableExtra("task");
            this.tasks.set(position, task);
            this.adapter.notifyItemChanged(position);
        } else if (requestCode == NEW_TASK_REQUEST && resultCode == RESULT_OK) {
            Task task = (Task) data.getSerializableExtra("task");
            this.tasks.add(task);
            this.adapter.notifyItemInserted(this.tasks.indexOf(task));
        }
    }

    @Override
    public void onPause() {
        if (adapter != null) {
            // delete any tasks in tasksToDelete
            if (adapter.getTasksToDelete() != null) {
                for (Task task: adapter.getTasksToDelete()){
                    task.delete();
                }
            }

            // mark any tasks in tasksToDone as done
            if (adapter.getTasksToDone() != null) {
                for (Task task: adapter.getTasksToDone()){
                    task.setStatus(Status.Done);
                    task.save();
                }
            }
        }

        super.onPause();
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.viewDone) {
            if (item.isChecked()) {
                this.tasks.clear();
                this.tasks.addAll(
                        SQLite.select()
                                .from(Task.class)
                                .where(Task_Table.status.is(Status.Todo.getDbValue()))
                                .queryList());
                adapter.notifyDataSetChanged();
                item.setChecked(false);
                btnAddItem.setVisibility(View.VISIBLE);
            } else {
                this.tasks.clear();
                this.tasks.addAll(
                        SQLite.select()
                                .from(Task.class)
                                .where(Task_Table.status.is(Status.Done.getDbValue()))
                                .queryList());
                adapter.notifyDataSetChanged();
                item.setChecked(true);
                btnAddItem.setVisibility(View.GONE);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
