package com.s21tasks.app.FollowUpRecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s21tasks.app.AppClass.DateTimeConversion;
import com.s21tasks.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FollowUpAdapter extends RecyclerView.Adapter<FollowUpViewHolder> {
    private List<FollowUpItems> followUpItems;
    private Context context;

    public FollowUpAdapter(List<FollowUpItems> followUpItems, Context context) {
        this.followUpItems = followUpItems;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public FollowUpViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.followup_child,parent,false);
        FollowUpViewHolder followUpViewHolder = new FollowUpViewHolder(v);
        return followUpViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FollowUpViewHolder holder, int position) {
        FollowUpItems items = followUpItems.get(position);

        holder.followTitle.setText("Task No :"+items.getFollowupTitle());
        holder.followDesc.setText(items.getFollowUpMessage());
        holder.followDate.setText(DateTimeConversion.convertDatetime(items.getFollowUpDate().trim()));
    }

    @Override
    public int getItemCount() {
        return followUpItems.size();
    }
}
