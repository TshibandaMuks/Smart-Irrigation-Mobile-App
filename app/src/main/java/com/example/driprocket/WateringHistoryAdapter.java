package com.example.driprocket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WateringHistoryAdapter extends RecyclerView.Adapter<WateringHistoryViewHolder> {
    @NonNull

    Context context;
    List<WateringHistoryItems> items;

    public WateringHistoryAdapter(@NonNull Context context, List<WateringHistoryItems> items) {
        this.context = context;
        this.items = items;
    }

    public WateringHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WateringHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.individual_watering_history,
                parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WateringHistoryViewHolder holder, int position) {
     holder.wateringHistoryDates.setText(items.get(position).getWaterDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
