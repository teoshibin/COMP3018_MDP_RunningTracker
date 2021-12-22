package com.lucasteo.runningtracker.view_helper;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Item decorator that is added into recycler view for extra spacing between items
 */
public class SpacingItemDecorator extends RecyclerView.ItemDecoration {

    private final int spaceSize;

    /**
     * constructor
     *
     * @param spaceSize specify space dimensions for left, right, top and bottom item margin
     */
    public SpacingItemDecorator(int spaceSize) {
        this.spaceSize = spaceSize;
    }

    /**
     * adjust item offsets
     *
     * @param outRect to set offset
     * @param view item
     * @param parent recycler view
     * @param state recycler view state
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0){
            outRect.top = spaceSize;
        }
        outRect.left = spaceSize;
        outRect.right = spaceSize;
        outRect.bottom = spaceSize;
    }
}
