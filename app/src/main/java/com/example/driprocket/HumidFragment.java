package com.example.driprocket;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatCallback;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.common.value.qual.StringVal;
import org.w3c.dom.Text;

public class HumidFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.humid_fragment, container, false);
        ProgressBar progressBar = v.findViewById(R.id.humidProgressBar); //find reference to the progressBar
        TextView humidProgressBarText = (TextView)v.findViewById(R.id.humidProgressBarText);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(); //get the database

        DatabaseReference databaseReference = firebaseDatabase.getReference("SensorData").child("Humidity");//get reference to use realtime database
        databaseReference.addValueEventListener(new ValueEventListener() { //you can add a new ChildEventListener instead of ValueEventListener() , ChildEventListener has more methods
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ObjectAnimator progressAnimator;

                String humidityProgress = snapshot.getValue().toString();       //get it as a string first because its had to convert to int
                Log.d("kandua",humidityProgress);                          //log the value to see if its working
               // progressBar.setProgress((Integer.parseInt(humidityProgress))); //update it automatically

                progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0,Integer.parseInt(humidityProgress)); //object animator for progress bar outline
                progressAnimator.setDuration(500); //duration in milliseconds                                                                                      //last 2 parameters ofInt()method is the start value and the end value
                progressAnimator.start();

                //TODO: FIND A WAY TO ANIMAATE THE PROGRESS BAR TEXT
                humidProgressBarText.setText(humidityProgress+"%");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                    //TODO:HANDLE
            }
        });
        return v;
    }
}
