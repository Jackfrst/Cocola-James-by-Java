package com.s21tasks.app.NotificationRecyclerview;

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

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private Context context ;
    private List<NotificationItems> notificationItems ;

    public NotificationAdapter(Context context, List<NotificationItems> notificationItems) {
        this.context = context;
        this.notificationItems = notificationItems;
    }

    @NonNull
    @NotNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notification_child,parent,false);
        NotificationViewHolder notificationViewHolder = new NotificationViewHolder(v);
        return notificationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationViewHolder holder, int position) {
        NotificationItems items = notificationItems.get(position);

        holder.notiTitle.setText(items.getNotificationTitle());
        holder.notiDate.setText(DateTimeConversion.convertDatetime(items.getNotificationDate().trim()));
        holder.notiDesc.setText(items.getNotificationDesc());
    }

    @Override
    public int getItemCount() {
        return notificationItems.size();
    }
}
