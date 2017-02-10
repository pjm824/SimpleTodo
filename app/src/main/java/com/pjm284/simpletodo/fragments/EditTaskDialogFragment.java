package com.pjm284.simpletodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import android.support.v4.app.DialogFragment;
import android.widget.RadioGroup;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.models.Task;

import java.util.Date;


/**
 * Created by pauljmin on 2/4/17.
 */

public class EditTaskDialogFragment extends DialogFragment {

    /**
     * task that is being edited
     */
    private Task task;

    /**
     * subject test field
     */
    private EditText etSubject;

    /**
     * priority buttons
     */
    private RadioGroup rgPriority;

    /**
     * Due date DatePicker
     */
    private DatePicker dpDueDate;

    public EditTaskDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditTaskDialogFragment newInstance(String title, Task task) {
        EditTaskDialogFragment frag = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setTask(task);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_task, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get field from view
        etSubject = (EditText) view.findViewById(R.id.etSubject);

        // fill in field with current value from task
        etSubject.setText(this.task.getSubject());

        rgPriority = (RadioGroup) view.findViewById(R.id.rgPriority);

        String priority = this.task.getPriority();

        // if priority is set, check the correct button
        if (priority != null) {
            if (priority.equals("Low")) {
                rgPriority.check(R.id.rbPriorityLow);
            } else if (priority.equals("Medium")) {
                rgPriority.check(R.id.rbPriorityMedium);
            } else if (priority.equals("High")) {
                rgPriority.check(R.id.rbPriorityHigh);
            }
        }

        dpDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        etSubject.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public EditText getSubjectField() {
        return this.etSubject;
    }

    public RadioGroup getPriorityField() {
        return this.rgPriority;
    }

    public DatePicker getDateField() {
        return this.dpDueDate;
    }
}