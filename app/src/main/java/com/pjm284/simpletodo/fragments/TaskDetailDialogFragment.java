package com.pjm284.simpletodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import android.support.v4.app.DialogFragment;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.models.Task;


/**
 * Created by pauljmin on 2/4/17.
 */

public class TaskDetailDialogFragment extends DialogFragment {

    /**
     * task that is being edited
     */
    private Task task;

    /**
     * subject test field
     */
    private EditText etSubject;

    public TaskDetailDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static TaskDetailDialogFragment newInstance(String title, Task task) {
        TaskDetailDialogFragment frag = new TaskDetailDialogFragment();
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

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        etSubject.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public EditText getEtSubject() {
        return this.etSubject;
    }
}