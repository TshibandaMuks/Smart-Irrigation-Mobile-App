package com.example.driprocket;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;

public class MoistureChecker implements Runnable{
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference().child("Moisture");
    String moistureValue;

    private Activity v;
    public MoistureChecker(Activity activity){
        this.v = v;
    }

    @Override
    public void run() {
        while(true){
            TextView waterStat = v.findViewById(R.id.waterStatus);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    moistureValue = snapshot.getValue().toString();

                    if(Integer.parseInt(moistureValue)>50 && Integer.parseInt(moistureValue)<100){
                       waterStat.setText("Plants are okayy");
                    }if(Integer.parseInt(moistureValue)<50 && Integer.parseInt(moistureValue)>0) {
                        waterStat.setText("Plants need to be watered..");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
}
