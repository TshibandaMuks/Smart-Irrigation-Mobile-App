package com.example.driprocket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class weatherActivity extends AppCompatActivity {
    TextView todaysWeatherComment,todaysTemp,todaysRainPercentage,todaysWindSpeed,todaysHumidity;
    ImageView todaysWeatherIcon;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private  final String urlAPI = "https://api.open-meteo.com/v1/forecast?latitude=-33.9258&longitude=18.4232&daily=weathercode,temperature_2m_max,temperature_2m_min,precipitation_probability_max,windspeed_10m_max&timezone=auto";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    jsonHandler(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void jsonHandler(String response) throws JSONException {
                JSONObject jsonResponse = new JSONObject(response);
                //Log.d("response", response);
                JSONObject days = jsonResponse.getJSONObject("daily");
                JSONArray dates = days.getJSONArray("time");
                JSONArray dailyTemperaturesMax = days.getJSONArray("temperature_2m_max");
                JSONArray dailyWind = days.getJSONArray("windspeed_10m_max");
                JSONArray dailyRain = days.getJSONArray("precipitation_probability_max");
                JSONArray dailyTemperatureMin = days.getJSONArray("temperature_2m_min");
                JSONArray dailyWeatherCode = days.getJSONArray("weathercode");
                //JSONObject humidity = jsonResponse.getJSONObject("hourly");
                //JSONArray  hourlyHumidity = = humidity.getJSONArray("relativehumidity_2m");

                attachToScreen(dates,dailyTemperaturesMax,dailyWind,dailyRain,dailyTemperatureMin,dailyWeatherCode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(weatherActivity.this, "Failure connecting to API", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void attachToScreen(JSONArray daysOfWeek,JSONArray dailyTemperaturesMax, JSONArray dailyWind, JSONArray dailyRain, JSONArray dailyTemperatureMin, JSONArray dailyWeatherCode) {

        todaysHumidity = findViewById(R.id.todaysHumidity);
        todaysRainPercentage = findViewById(R.id.todaysRainPercentage);
        todaysWindSpeed = findViewById(R.id.todaysWindSpeed);
        todaysTemp = findViewById(R.id.todaysTemp);
        todaysWeatherComment = findViewById(R.id.todaysWeatherComment);
        todaysWeatherIcon = findViewById(R.id.todaysWeatherIcon);

        try {
        //set today's date weather variables from the API
        todaysTemp.setText(dailyTemperaturesMax.getInt(0)+" °");
        todaysRainPercentage.setText(dailyRain.getInt(0)+" %");
        todaysWindSpeed.setText(dailyWind.getInt(0)+"km/h");

        List<WeeklyForecastItems> items = new ArrayList<>();
            //use for Loop combined with If statements to accurately place weather Icons according to days
        for(int i=0;i<=5;i++){

           if(dailyWeatherCode.getInt(i) <=3){ //these weather codes indicate cloud cover and rain
               if(i==0){todaysWeatherIcon.setImageResource(R.drawable.sunny);
                        todaysWeatherComment.setText("Mostly Sunny");
                        todaysHumidity.setText("7%");} //we want to set todays weather icon only once hence the condition
                        items.add(new WeeklyForecastItems(((Date)simpleDateFormat.parse(daysOfWeek.getString(i+1))).toString().substring(0,3),dailyTemperaturesMax.getString(i).substring(0,2)+" °",R.drawable.sunny));
                }

           if( dailyWeatherCode.getInt(i) >= 45 && dailyWeatherCode.getInt(i) <= 55){
               if(i==0){todaysWeatherIcon.setImageResource(R.drawable.partly_cloudy);
                        todaysWeatherComment.setText("Partly Cloudy");
                        todaysHumidity.setText("33%");} //we want to set todays weather icon only once hence the condition
               items.add(new WeeklyForecastItems(((Date)simpleDateFormat.parse(daysOfWeek.getString(i+1))).toString().substring(0,3),dailyTemperaturesMax.getString(i).substring(0,2)+" °",R.drawable.partly_cloudy));
               }

           if(dailyWeatherCode.getInt(i) >= 56 && dailyWeatherCode.getInt(i)<= 65){
               if(i==0){todaysWeatherIcon.setImageResource(R.drawable.cloudy);
                        todaysWeatherComment.setText("High Cloud Cover");
                        todaysHumidity.setText("57%");} //we want to set todays weather icon only once hence the condition
             items.add(new WeeklyForecastItems(((Date)simpleDateFormat.parse(daysOfWeek.getString(i+1))).toString().substring(0,3),dailyTemperaturesMax.getString(i).substring(0,2)+" °",R.drawable.cloudy));
               }

           if(dailyWeatherCode.getInt(i) >= 66 && dailyWeatherCode.getInt(i)<= 86){
               if(i==0){todaysWeatherIcon.setImageResource(R.drawable.rainy);
                        todaysWeatherComment.setText("Mostly Rainy");
                        todaysHumidity.setText("86&");} //we want to set todays weather icon only once hence the condition
              items.add(new WeeklyForecastItems(((Date)simpleDateFormat.parse(daysOfWeek.getString(i+1))).toString().substring(0,3),dailyTemperaturesMax.getString(i).substring(0,2)+" °",R.drawable.rainy));
               }
            }
            // Add the individual days to recycler View
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(new WeeklyForecastAdapter(getApplicationContext(),items));
        } catch (JSONException | ParseException e) {
          Toast.makeText(weatherActivity.this, "Error in attachToDays Function", Toast.LENGTH_SHORT).show();
        }
    }
}