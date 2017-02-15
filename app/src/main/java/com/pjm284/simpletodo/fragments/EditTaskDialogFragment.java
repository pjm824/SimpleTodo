package com.pjm284.simpletodo.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.pjm284.simpletodo.models.Task;

/**
 * Created by pauljmin on 2/4/17.
 */

public class EditTaskDialogFragment extends TaskDialogFragment {

    public interface EditTaskDialogListener {
        void onFinishEditTaskDialog();
    }

    public static EditTaskDialogFragment newInstance(String title, Task task) {
        EditTaskDialogFragment frag = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setTask(task);
        frag.setHeaderString(TaskDialog.EDIT.getHeader());
        return frag;
    }

    protected void setSaveBtnListener() {
        this.saveBtnListener = new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                EditTaskDialogFragment df = (EditTaskDialogFragment) fm.findFragmentByTag("fragment_edit_Task");
                df.saveTaskFromFields();

                // call into the activity to update the adapter
                EditTaskDialogListener listener = (EditTaskDialogListener) getActivity();
                listener.onFinishEditTaskDialog();

                // close the fragment
                df.dismiss();
            }
        };
    }
}