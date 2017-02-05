package com.pjm284.simpletodo.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.models.Task;
import com.pjm284.simpletodo.fragments.TaskDetailDialogFragment;
import com.pjm284.simpletodo.adapters.TasksAdapter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;

    ArrayList<Task> tasks;
    TasksAdapter tasksAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Construct the data source
        this.tasks = (ArrayList<Task>) SQLite.select().from(Task.class).queryList();

        // Create the adapter to convert the array to views
        this.tasksAdapter = new TasksAdapter(this, tasks);

        // Attach the adapter to a ListView
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(this.tasksAdapter);

        setUpListViewListener();
    }

    private void setUpListViewListener() {

        // delete
        lvItems.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener () {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                    Task task = tasks.remove(pos);
                    task.delete();
                    tasksAdapter.notifyDataSetChanged();
                    return true;
                }
            });

        // open up edit fragment
        lvItems.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                    //get the task that was selected
                    Task task = tasks.get(pos);

                    FragmentManager fm = getSupportFragmentManager();
                    TaskDetailDialogFragment taskDetailDialogFragment = TaskDetailDialogFragment.newInstance("Edit Task", task);
                    taskDetailDialogFragment.show(fm, "fragment_edit_Task");
                }
            }
        );
    }

    /**
     * Save button on editTaskDialogFragment
     *
     * @param v
     */
    public void onSaveEditTask(View v) {
        FragmentManager fm = getSupportFragmentManager();
        TaskDetailDialogFragment df = (TaskDetailDialogFragment) fm.findFragmentByTag("fragment_edit_Task");

        Task task = df.getTask();
        task.setSubject(df.getEtSubject().getText().toString());
        task.save();
        tasksAdapter.notifyDataSetChanged();
        df.dismiss();
    }

    /**
     * Create new task
     * @param v
     */
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Task task = new Task();
        task.setSubject(itemText);
        task.save();
        this.tasksAdapter.add(task);

        // empty out input box
        etNewItem.setText("");
    }

}
