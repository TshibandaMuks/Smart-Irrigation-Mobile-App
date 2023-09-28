package com.example.driprocket;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeeklyForecastViewHolder extends RecyclerView.ViewHolder {

    TextView recyclerWeatherDay;
    TextView recyclerWeatherTemp;
    ImageView recyclerWeatherIcon;
    public WeeklyForecastViewHolder(@NonNull View itemView) {
        super(itemView);
        recyclerWeatherDay = itemView.findViewById(R.id.recyclerWeatherDay);
        recyclerWeatherIcon = itemView.findViewById(R.id.recycerWeatherIcon);
        recyclerWeatherTemp = itemView.findViewById(R.id.recycerWeatherTemp);
    }
}
