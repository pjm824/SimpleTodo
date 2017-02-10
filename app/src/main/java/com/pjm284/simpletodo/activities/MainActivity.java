package com.pjm284.simpletodo.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.models.Task;
import com.pjm284.simpletodo.fragments.EditTaskDialogFragment;
import com.pjm284.simpletodo.adapters.TasksAdapter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Calendar;

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
                    EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance("Edit Task", task);
                    editTaskDialogFragment.show(fm, "fragment_edit_Task");
                }
            }
        );
    }

    /**
     * Save button on editTaskDialogFragment
     *
     * @param v
     */
    public void onSaveTask(View v) {
        FragmentManager fm = getSupportFragmentManager();
        EditTaskDialogFragment df = (EditTaskDialogFragment) fm.findFragmentByTag("fragment_edit_Task");

        Task task = df.getTask();

        // Set Subject
        task.setSubject(df.getSubjectField().getText().toString());

        // Set Priority
        RadioGroup priorityField = df.getPriorityField();
        int buttonId = priorityField.getCheckedRadioButtonId();
        RadioButton prioritySelected = (RadioButton) priorityField.findViewById(buttonId);

        if (prioritySelected != null) {
            task.setPriority(prioritySelected.getText().toString());
        }

        // Set Due Date
        DatePicker dp = df.getDateField();
        int month = dp.getMonth();
        int day = dp.getDayOfMonth();
        int year = dp.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        task.setDueDate(calendar);

        // save task
        task.save();

        // update the view to reflect changes
        tasksAdapter.notifyDataSetChanged();

        // close the fragment
        df.dismiss();
    }

    /**
     * Create new task
     * @param v
     */
    public void onAddItem(View v) {
        Task task = new Task();
        this.tasks.add(task);

        FragmentManager fm = getSupportFragmentManager();
        EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance("Add Task", task);
        editTaskDialogFragment.show(fm, "fragment_edit_Task");
    }

}
