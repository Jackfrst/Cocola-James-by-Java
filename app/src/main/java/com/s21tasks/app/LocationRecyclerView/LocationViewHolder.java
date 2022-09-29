package com.s21tasks.app.LocationRecyclerView;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.s21tasks.app.R;

import org.jetbrains.annotations.NotNull;

public class LocationViewHolder extends RecyclerView.ViewHolder {
    public TextView locationName;
    public RelativeLayout locationLayout;
    public LocationViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        //finding
        locationName = itemView.findViewById(R.id.location_name_id);
        locationLayout = itemView.findViewById(R.id.location_layout_id);
    }
}
