package com.s21tasks.app.LocationRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.s21tasks.app.AppClass.OnLocationSelect;
import com.s21tasks.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> {
    private List<LocationItems> locationItems;
    private Context context;
    private OnLocationSelect onLocationSelect;

    public LocationAdapter(List<LocationItems> locationItems, Context context, OnLocationSelect onLocationSelect) {
        this.locationItems = locationItems;
        this.context = context;
        this.onLocationSelect = onLocationSelect;
    }

    @NonNull
    @NotNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(context).inflate(R.layout.location_item_child,parent,false);
        LocationViewHolder locationViewHolder = new LocationViewHolder(v);
        return  locationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull LocationViewHolder holder, int position) {
        final LocationItems selectItem = locationItems.get(position);

        holder.locationName.setText(selectItem.getTypeName());
        holder.locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLocationSelect.onLocationSelectedListener(selectItem.getTypeName(),selectItem.getTypeCode());
            }
        });
    }
    @Override
    public int getItemCount() {
        return locationItems.size();
    }
}
