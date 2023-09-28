package com.example.driprocket;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MoistFragment extends Fragment {
    View moistFragView;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("SensorData").child("Moisture");
    String moistureProgress; //used in onDataChange function . defined here to when value changed it can be used anyway

  public MoistFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        moistFragView = inflater.inflate(R.layout.moist_fragment, container, false);
        TextView waterStatus =  moistFragView.findViewById(R.id.waterStatus); //get water status to update when moisture below certain point
        TextView moistureProgressText =  moistFragView.findViewById(R.id.moistProgressBarText);
        ProgressBar progressBar = moistFragView.findViewById(R.id.moistProgressBar);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                ObjectAnimator progressAnimator; //used for progress bar animation
                moistureProgress = snapshot.getValue().toString();
                //TODO: FIND A WAY TO ANIMATE THE PROGRESS BAR TEXT
                moistureProgressText.setText(moistureProgress+"%");
                // progressBar.setProgress(Integer.parseInt(moistureProgress)); //no longer needed as animator animates it for use
                progressAnimator = ObjectAnimator.ofInt(progressBar,"progress",0,Integer.parseInt(moistureProgress));
                progressAnimator.setDuration(500);
                progressAnimator.start();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return moistFragView;
    }
}