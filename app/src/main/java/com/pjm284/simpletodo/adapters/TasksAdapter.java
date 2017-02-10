package com.pjm284.simpletodo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.models.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Created by pauljmin on 2/4/17.
 */

public class TasksAdapter extends ArrayAdapter<Task> {
    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, 0, tasks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
        }

        // Set subject text view
        TextView tvTaskSubject = (TextView) convertView.findViewById(R.id.tvTaskSubject);
        tvTaskSubject.setText(task.getSubject());

        // Set priority text view
        TextView tvTaskPriority = (TextView) convertView.findViewById(R.id.tvTaskPriority);
        tvTaskPriority.setText(task.getPriority());

        // Set due date text view
        Calendar cal = task.getDueDate();
        SimpleDateFormat formatter = new SimpleDateFormat("MMM d yyyy");
        TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
        tvDueDate.setText(formatter.format(cal.getTime()));

        // Return the completed view to render on screen
        return convertView;
    }
}