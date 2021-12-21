package com.lucasteo.runningtracker.viewHelper;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemDecorator extends RecyclerView.ItemDecoration {

    private final int spaceSize;

    public SpacingItemDecorator(int spaceSize) {
        this.spaceSize = spaceSize;
    }

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
