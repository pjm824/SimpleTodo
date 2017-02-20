package com.pjm284.simpletodo.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.pjm284.simpletodo.models.Priority;
import com.pjm284.simpletodo.models.Task;

public class NewTaskDialogFragment extends TaskDialogFragment {
    
    public interface NewTaskDialogListener {
        void onFinishNewTaskDialog(Task task);
    }

    public static NewTaskDialogFragment newInstance(String title) {
        NewTaskDialogFragment frag = new NewTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);

        Task task = new Task();
        task.setPriority(Priority.Medium);

        frag.setArguments(args);
        frag.setTask(task);
        frag.setHeaderString(TaskDialog.NEW.getHeader());
        return frag;
    }

    protected void setSaveBtnListener() {
        this.saveBtnListener = new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                NewTaskDialogFragment df = (NewTaskDialogFragment) fm.findFragmentByTag("fragment_edit_Task");
                df.saveTaskFromFields();

                // call into the activity to update the adapter
                NewTaskDialogListener listener = (NewTaskDialogListener) getActivity();
                listener.onFinishNewTaskDialog(df.getTask());

                // close the fragment
                df.dismiss();
            }
        };
    }
}