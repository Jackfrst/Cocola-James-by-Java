package com.s21tasks.app.HisoryRecycler;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s21tasks.app.AppClass.DateTimeConversion;
import com.s21tasks.app.HistoryDetailsActivity;
import com.s21tasks.app.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private Context context ;
    private List<HistoryItems> historyItems ;

    public HistoryAdapter(Context context, List<HistoryItems> historyItems) {
        this.context = context;
        this.historyItems = historyItems;
    }

    @NonNull
    @NotNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history_child,parent,false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(v);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HistoryViewHolder holder, int position) {
        HistoryItems items = historyItems.get(position);

        Picasso.get().load(items.getHistoryImage()).placeholder(R.drawable.ic_baseline_image_24)
                .error(R.drawable.ic_baseline_image_24).into(holder.historyImage);
        holder.historyTitle.setText(items.getHistoryTitle());
        holder.historyshortDesc.setText(items.getHistoryShortDesc());
        holder.historyDate.setText(DateTimeConversion.convertDate(items.getHistoryDate().split(" ")[0].trim()));
        holder.historyLocation.setText(items.getLocation());

        if (items.getStatus().equals("3")||items.getStatus().equals("4")){
            holder.historyStatus.setImageResource(R.drawable.ic_tick);
        }else if(items.getStatus().equals("2")){
            holder.historyStatus.setImageResource(R.drawable.ic_tick_red);
        }else{
            holder.historyStatus.setImageResource(R.drawable.ic_round_check_uncomplete_circle_24);
        }



        holder.historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, HistoryDetailsActivity.class);
                i.putExtra("issueKey",items.getIssueNo());
                i.putExtra("statusKey",items.getStatus());
                i.putExtra("locatoinKey",items.getLocation());
                i.putExtra("dateKey",items.getHistoryDate());
                i.putExtra("dateKey",items.getHistoryDate());
                i.putExtra("titleKey",items.getHistoryTitle());
                i.putExtra("descKey",items.getHistoryShortDesc());
                i.putExtra("imageKey",items.getHistoryImage());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }
}
