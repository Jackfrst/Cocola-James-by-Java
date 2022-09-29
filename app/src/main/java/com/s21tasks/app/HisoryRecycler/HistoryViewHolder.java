package com.s21tasks.app.HisoryRecycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s21tasks.app.R;

import org.jetbrains.annotations.NotNull;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    ImageView historyImage , historyStatus ;
    TextView historyTitle , historyshortDesc , historyLongDesc , historyDate , historyLocation ;
    LinearLayout historyLayout ;
    public HistoryViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        historyImage = itemView.findViewById(R.id.history_child_image);
        historyStatus = itemView.findViewById(R.id.history_status_img);
        historyTitle = itemView.findViewById(R.id.history_child_title);
        historyshortDesc = itemView.findViewById(R.id.history_child_short_desc);
        historyDate = itemView.findViewById(R.id.history_child_date);
        historyLocation = itemView.findViewById(R.id.history_child_location);
        historyLayout = itemView.findViewById(R.id.history_child_layout);

    }
}
