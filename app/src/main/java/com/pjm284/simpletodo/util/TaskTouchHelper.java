package com.pjm284.simpletodo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.View;

import com.pjm284.simpletodo.R;
import com.pjm284.simpletodo.adapters.TasksAdapter;


public class TaskTouchHelper extends ItemTouchHelper.SimpleCallback {
    private static final int LEFT = 4;
    private static final int RIGHT = 8;

    private TasksAdapter mtaskAdapter;
    private Context mcontext;

    public TaskTouchHelper(TasksAdapter taskAdapter, Context context){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mtaskAdapter = taskAdapter;
        this.mcontext = context;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //TODO: add for reordering
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Remove task
        if (direction == RIGHT) {
            mtaskAdapter.queueToRemove(viewHolder);
        } else if (direction == LEFT) { // mark task as done
            mtaskAdapter.queueToDone(viewHolder);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;

            Paint p = new Paint();
            Bitmap icon;

            // swipe right
            if (dX > 0) {
                p.setARGB(255,237,73,71);

                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
                        (float) itemView.getBottom(), p);

                icon = BitmapFactory.decodeResource(
                        mtaskAdapter.getContext().getResources(), R.drawable.delete);

                c.drawBitmap(icon,
                        (float) itemView.getLeft() + convertDpToPx(16),
                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
                        p);
            } else {    // swipe left
                p.setARGB(255, 66, 134, 244);

                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                        (float) itemView.getRight(), (float) itemView.getBottom(), p);

                icon = BitmapFactory.decodeResource(
                        mtaskAdapter.getContext().getResources(), R.drawable.check);

                c.drawBitmap(icon,
                        (float) itemView.getRight() - convertDpToPx(16) - icon.getWidth(),
                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
                        p);
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    private int convertDpToPx(int dp){
        return Math.round(dp * (mtaskAdapter.getContext().getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
