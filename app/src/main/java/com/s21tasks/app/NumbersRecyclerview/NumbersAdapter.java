package com.s21tasks.app.NumbersRecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.s21tasks.app.AppClass.OnCallItemClick;
import com.s21tasks.app.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersViewHolder> {
    private Context context ;
    private List<NumbersItems> numbersItems ;
    private OnCallItemClick onCallItemClick ;

    public NumbersAdapter(Context context, List<NumbersItems> numbersItems, OnCallItemClick onCallItemClick) {
        this.context = context;
        this.numbersItems = numbersItems;
        this.onCallItemClick = onCallItemClick;
    }

    @NonNull
    @NotNull
    @Override
    public NumbersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.numbers_child,parent,false);
        NumbersViewHolder numbersViewHolder = new NumbersViewHolder(v);
        return numbersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NumbersViewHolder holder, int position) {
        NumbersItems numList = numbersItems.get(position);
        holder.name.setText(numList.getName());
        holder.number.setText(numList.getNumber());
        holder.callImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCallItemClick.onCallClick(numList.getNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return numbersItems.size();
    }
}
