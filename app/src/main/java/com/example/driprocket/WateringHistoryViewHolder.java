package com.example.driprocket;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class WateringHistoryViewHolder extends RecyclerView.ViewHolder {
    TextView wateringHistoryDates;
    public WateringHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        wateringHistoryDates = itemView.findViewById(R.id.individualWateringDates);
    }
}
