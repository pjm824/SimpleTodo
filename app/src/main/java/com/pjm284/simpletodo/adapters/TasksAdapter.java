package com.pjm284.simpletodo.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskSubject;
        TextView tvTaskPriority;
        TextView tvDueDate;

        ViewHolder(View itemView) {
            super(itemView);

            tvTaskSubject = (TextView) itemView.findViewById(R.id.tvTaskSubject);
            tvTaskPriority = (TextView) itemView.findViewById(R.id.tvTaskPriority);
            tvDueDate = (TextView) itemView.findViewById(R.id.tvDueDate);
        }
    }

    // Store a member variable for the tasks
    private List<Task> mTasks;

    // queue of tasks to delete
    private List<Task> tasksToDelete;

    // queue of tasks to mark as done
    private List<Task> tasksToDone;

    // Store the context for easy access
    private Context mContext;

    // Pass in the task array into the constructor
    public TasksAdapter(Context context, List<Task> tasks) {
        mTasks = tasks;
        mContext = context;
        tasksToDelete = new ArrayList<>();
        tasksToDone = new ArrayList<>();
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
        // strikethrough tasks that are done
        if (task.getStatus() == Status.Done) {
            tvTaskSubject.setPaintFlags(tvTaskSubject.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tvTaskSubject.setPaintFlags(tvTaskSubject.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        Priority priority = task.getPriority();
        TextView tvTaskPriority = viewHolder.tvTaskPriority;
        tvTaskPriority.setText(priority.getName());
        tvTaskPriority.setTextColor(ContextCompat.getColor(getContext(), priority.getColor()));

        Calendar cal = task.getDueDate();
        DateFormat formatter = SimpleDateFormat.getDateInstance();
        TextView tvDueDate = viewHolder.tvDueDate;
        tvDueDate.setText(formatter.format(cal.getTime()));
    }

    /**
     * When user swipes to delete, queue it up for deletion and put up a toast that includes an undo option
     */
    public void queueToRemove(final RecyclerView.ViewHolder viewHolder) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final Task task = mTasks.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        tasksToDelete.add(task);

        Snackbar.make(((MainActivity) getContext()).findViewById(R.id.rvTasks), R.string.taskDelete, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    public void onClick(View v) {
                        mTasks.add(adapterPosition, task);
                        notifyItemInserted(adapterPosition);
                        tasksToDelete.remove(task);
                    }
                })
                .show();
    }

    /**
     * When user swipes to mark as done, queue it up and put up a toast that includes an undo option
     */
    public void queueToDone(final RecyclerView.ViewHolder viewHolder) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final Task task = mTasks.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);
        tasksToDone.add(task);

        Snackbar.make(((MainActivity) getContext()).findViewById(R.id.rvTasks), R.string.taskDone, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    public void onClick(View v) {
                        mTasks.add(adapterPosition, task);
                        notifyItemInserted(adapterPosition);
                        tasksToDone.remove(task);
                    }
                })
                .show();
    }

    public Context getContext() {
        return this.mContext;
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public List<Task> getTasksToDelete() {
        return tasksToDelete;
    }

    public List<Task> getTasksToDone() {
        return tasksToDone;
    }
}