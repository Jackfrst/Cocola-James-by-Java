package com.s21tasks.app.FollowUpRecycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s21tasks.app.R;

import org.jetbrains.annotations.NotNull;

public class FollowUpViewHolder extends RecyclerView.ViewHolder {
    public TextView followTitle , followDate , followDesc ;
    public FollowUpViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        followTitle = itemView.findViewById(R.id.follow_up_child_title);
        followDate = itemView.findViewById(R.id.follow_up_child_date);
        followDesc = itemView.findViewById(R.id.follow_up_child_comment);
    }
}
