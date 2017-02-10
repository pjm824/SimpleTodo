package com.pjm284.simpletodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import android.support.v4.app.DialogFragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.models.Task;


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

    private RadioGroup rgPriority;

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

        if (priority != null) {
            if (priority.equals("Low")) {
                rgPriority.check(R.id.rbPriorityLow);
            } else if (priority.equals("Medium")) {
                rgPriority.check(R.id.rbPriorityMedium);
            } else if (priority.equals("High")) {
                rgPriority.check(R.id.rbPriorityHigh);
            }
        }
        

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

    public RadioGroup getRgPriority() { return this.rgPriority; }
}