package com.pjm284.simpletodo.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.activities.MainActivity;

public class DeleteConfirmationAlertDialogFragment extends DialogFragment {

    private int pos;

    public static DeleteConfirmationAlertDialogFragment newInstance(int pos) {
        DeleteConfirmationAlertDialogFragment frag = new DeleteConfirmationAlertDialogFragment();
        frag.setPos(pos);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_confirmation)
                .setPositiveButton(R.string.okButtonText,
                        new deleteConfirmationListener((MainActivity)getActivity(), pos)
                )
                .setNegativeButton(R.string.cancelButtonText,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    private static class deleteConfirmationListener implements DialogInterface.OnClickListener {

        private MainActivity activity;
        private int pos;

        public deleteConfirmationListener(MainActivity activity, int pos){
            this.activity = activity;
            this.pos = pos;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            this.activity.deleteTask(pos);
        }

    }
}