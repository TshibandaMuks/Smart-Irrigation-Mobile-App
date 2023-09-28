package com.example.driprocket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeeklyForecastAdapter extends RecyclerView.Adapter<WeeklyForecastViewHolder> {

    Context context;
    List<WeeklyForecastItems> weeklyForecastItems;

    public WeeklyForecastAdapter(Context context, List<WeeklyForecastItems> weeklyForecastItems) {
        this.context = context;
        this.weeklyForecastItems = weeklyForecastItems;
    }

    @NonNull
    @Override
    public WeeklyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new WeeklyForecastViewHolder(LayoutInflater.from(context).inflate(R.layout.daily_weather_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyForecastViewHolder holder, int position) {
      holder.recyclerWeatherDay.setText(weeklyForecastItems.get(position).getRecyclerWeatherDay());
      holder.recyclerWeatherIcon.setImageResource(weeklyForecastItems.get(position).getRecyclerWeatherIcon());
      holder.recyclerWeatherTemp.setText(weeklyForecastItems.get(position).getRecyclerWeatherTemp());
    }

    @Override
    public int getItemCount() {
        return weeklyForecastItems.size();
    }
}
