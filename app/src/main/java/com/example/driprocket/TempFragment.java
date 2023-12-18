package com.example.driprocket;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class TempFragment extends Fragment {
    View tempFragView;                //this contains all the data which are in the temp_fragment xml
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tempFragView = inflater.inflate(R.layout.temp_fragment, container, false);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();    //get instance of the database
        DatabaseReference databaseReference = firebaseDatabase.getReference("SensorData").child("Temperature");  //create a reference to the database for using realtime db,

        // must add .child and say what child i want a reference of
        TextView temperatureProgressBarText = (TextView) tempFragView.findViewById(R.id.tempProgressBarText);
        ProgressBar tempProgressBar = (ProgressBar) tempFragView.findViewById(R.id.tempProgressBar);

        //you can add a new ChildEventListener instead of ValueEventListener() , ChildEventListener has more methods
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ObjectAnimator progressBarAnimator;
                String temperatureProgress = snapshot.getValue().toString();
                temperatureProgressBarText.setText(temperatureProgress+"°"+"C"); //"\u00B0" Java code for degrees symbol

                //tempProgressBar.setProgress(Integer.parseInt(temperatureProgress)); //convert temperatureProgress to integer then change progress   ...no longer needed because will be animated

                progressBarAnimator = ObjectAnimator.ofInt(tempProgressBar,"progress",0,Integer.parseInt(temperatureProgress));
                progressBarAnimator.setDuration(500);
                progressBarAnimator.start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               //TODO:HANDLE
            }
        });
        return tempFragView; //this contains all the views in the fragment
    }
}