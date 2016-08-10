package com.paragonfervour.charactersheet.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;


public abstract class RecycleViewHolder extends RecyclerView.ViewHolder {
    public RecycleViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void recycle();
}
