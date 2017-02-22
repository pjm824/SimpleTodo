package com.pjm284.simpletodo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.activities.MainActivity;
import com.pjm284.simpletodo.models.Priority;
import com.pjm284.simpletodo.models.Status;
import com.pjm284.simpletodo.models.Task;
import com.pjm284.simpletodo.models.Task_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.DateFormat;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTaskSubject;
        public TextView tvTaskPriority;
        public TextView tvDueDate;

        public ViewHolder(View itemView) {
            super(itemView);

            tvTaskSubject = (TextView) itemView.findViewById(R.id.tvTaskSubject);
            tvTaskPriority = (TextView) itemView.findViewById(R.id.tvTaskPriority);
            tvDueDate = (TextView) itemView.findViewById(R.id.tvDueDate);
        }
    }

    // Store a member variable for the tasks
    private List<Task> mTasks;

    // Store the context for easy access
    private Context mContext;

    // Pass in the task array into the constructor
    public TasksAdapter(Context context, List<Task> tasks) {
        mTasks = tasks;
        mContext = context;
    }

    @Override
    public TasksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View taskView = inflater.inflate(R.layout.item_task, parent, false);

        // Return a new holder instance
        return new ViewHolder(taskView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TasksAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Task task = mTasks.get(position);

        // Set item views based on your views and data model
        TextView tvTaskSubject = viewHolder.tvTaskSubject;
        tvTaskSubject.setText(task.getSubject());

        Priority priority = task.getPriority();
        TextView tvTaskPriority = viewHolder.tvTaskPriority;
        tvTaskPriority.setText(priority.getName());
        tvTaskPriority.setTextColor(Color.parseColor(priority.getColor()));

        Calendar cal = task.getDueDate();
        DateFormat formatter = SimpleDateFormat.getDateInstance();
        TextView tvDueDate = viewHolder.tvDueDate;
        tvDueDate.setText(formatter.format(cal.getTime()));
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public void remove(int position) {
        Task task = mTasks.remove(position);
        task.delete();
        notifyItemRemoved(position);
//        DeleteConfirmationAlertDialogFragment deleteConfirm = DeleteConfirmationAlertDialogFragment.newInstance(position);
//        deleteConfirm.show(getFragmentManager(), "dialog");
    }

    public void setAsDone(int position) {
        Task task = mTasks.get(position);
        task.setStatus(Status.Done);
        task.save();
        // TODO see I can use notifyItemRemoved instead of notifyDataSetChanged
        mTasks.clear();
        mTasks.addAll(SQLite.select().from(Task.class).where(Task_Table.status.is(0)).queryList());
        notifyDataSetChanged();

        Snackbar.make(((MainActivity)getContext()).findViewById(R.id.rvTasks), R.string.markedDone, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, ((MainActivity)getContext()).undoDoneBtnListener)
                .show();
    }



    public Context getContext() {
        return this.mContext;
    }
}