package com.pjm284.simpletodo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.support.v4.app.DialogFragment;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.models.Priority;
import com.pjm284.simpletodo.models.Task;

import java.util.Calendar;

/**
 * Created by pauljmin on 2/4/17.
 */

abstract public class TaskDialogFragment extends DialogFragment {

    enum TaskDialog {
        EDIT("Edit Task"), NEW("New Task");

        String header;
        TaskDialog(String s) {
            this.header = s;
        }

        String getHeader() {
            return this.header;
        }
    }

    /**
     * task that is being edited
     */
    protected Task task;

    /**
     * subject test field
     */
    protected EditText etSubject;

    /**
     * priority buttons
     */
    protected RadioGroup rgPriority;

    /**
     * Due date DatePicker
     */
    protected DatePicker dpDueDate;

    protected String headerString;

    /**
     * Set up priority radio group toggle listener
     */
    protected RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                view.setChecked(view.getId() == i);
            }
        }
    };

    /**
     * On click listener for priority buttons
     */
    protected View.OnClickListener priorityBtnListener = new View.OnClickListener() {
        public void onClick(View v) {
            ((RadioGroup)v.getParent()).check(0);
            ((RadioGroup)v.getParent()).check(v.getId());
        }
    };

    protected View.OnClickListener cancelBtnListener = new View.OnClickListener() {
        public void onClick(View v) {
            FragmentManager fm = getFragmentManager();
            TaskDialogFragment df = (TaskDialogFragment) fm.findFragmentByTag("fragment_edit_Task");
            df.dismiss();
        }
    };

    protected View.OnClickListener saveBtnListener;

    public TaskDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    abstract void setSaveBtnListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // hide the header
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return inflater.inflate(R.layout.fragment_edit_task, container);
    }

    @Override
    public void onResume() {
        // fill the screen
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // update wording on custom header
        this.updateHeaderString(view);

        // set up priority buttons
        RadioGroup rg = ((RadioGroup) view.findViewById(R.id.rgPriority));
        this.priorityButtonsSetUp(rg);

        // fill fragments fields from task
        this.fillFields(view);

        Button btnSaveTask = (Button) view.findViewById(R.id.btnSaveTask);
        this.setSaveBtnListener();
        btnSaveTask.setOnClickListener(saveBtnListener);

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(cancelBtnListener);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title");
        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        etSubject.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * Populates the fields in the fragment from given Task
     *
     * @param view
     */
    private void fillFields(View view) {
        // update subject field
        etSubject = (EditText) view.findViewById(R.id.etSubject);
        etSubject.setText(this.task.getSubject());

        // select the priority button
        rgPriority = (RadioGroup) view.findViewById(R.id.rgPriority);
        Priority priority = this.task.getPriority();

        // if priority is set, check the correct button
        if (priority != null) {
            if (priority.getName().equals("Low")) {
                rgPriority.check(R.id.btnPriorityLow);
            } else if (priority.getName().equals("Medium")) {
                rgPriority.check(R.id.btnPriorityMedium);
            } else if (priority.getName().equals("High")) {
                rgPriority.check(R.id.btnPriorityHigh);
            }
        }

        // set the datepicker
        Calendar cal = this.task.getDueDate();
        dpDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);
        dpDueDate.updateDate(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Set up priority radio group
     *
     * @param rg
     */
    private void priorityButtonsSetUp(RadioGroup rg) {

        rg.setOnCheckedChangeListener(ToggleListener);

        Button btnPriorityLow = (Button) rg.findViewById(R.id.btnPriorityLow);
        btnPriorityLow.setOnClickListener(priorityBtnListener);
        Button btnPriorityMedium = (Button) rg.findViewById(R.id.btnPriorityMedium);
        btnPriorityMedium.setOnClickListener(priorityBtnListener);
        Button btnPriorityHigh = (Button) rg.findViewById(R.id.btnPriorityHigh);
        btnPriorityHigh.setOnClickListener(priorityBtnListener);
    }

    protected void saveTaskFromFields() {

        Task task = this.getTask();

        // Set Subject
        task.setSubject(this.getSubjectField().getText().toString());

        // Set Priority
        RadioGroup priorityField = this.getPriorityField();
        int buttonId = priorityField.getCheckedRadioButtonId();
        ToggleButton prioritySelected = (ToggleButton) priorityField.findViewById(buttonId);

        if (prioritySelected != null) {
            task.setPriority(Enum.valueOf(Priority.class, prioritySelected.getText().toString()));
        }

        // Set Due Date
        DatePicker dp = this.getDateField();
        int month = dp.getMonth();
        int day = dp.getDayOfMonth();
        int year = dp.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        task.setDueDate(calendar);

        // save task
        task.save();
    }

    protected void updateHeaderString(View view) {
        TextView taskHeader = ((TextView) view.findViewById(R.id.taskHeader));
        taskHeader.setText(this.getHeaderString());
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

    public String getHeaderString() {
        return headerString;
    }

    public void setHeaderString(String headerString) {
        this.headerString = headerString;
    }
}