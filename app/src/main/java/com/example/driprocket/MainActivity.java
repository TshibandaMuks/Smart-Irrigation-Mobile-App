package com.example.driprocket;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager();

    }

    public void launchTempFrag(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, TempFragment.class, null)
                .addToBackStack("name")
                .setReorderingAllowed(true)
                .commit();
    }

    public void launchMoistFrag(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, MoistFragment.class, null)
                .addToBackStack("name")
                .setReorderingAllowed(true)
                .commit();
    }

    public void launchHumidFrag(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, HumidFragment.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();
    }

    public void dbManager() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("SensorData").child("Moisture");
        Toast toastWaterPlants = Toast.makeText(this, "Plants need to be watered...", Toast.LENGTH_SHORT);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String waterLevel = snapshot.getValue().toString();
                TextView waterVal = findViewById(R.id.waterStatus);
                if (Integer.parseInt(waterLevel) >= 17 && Integer.parseInt(waterLevel) <= 50) {
                    waterVal.setText("Plant moisture levels are good...");
                } else if (Integer.parseInt(waterLevel) >= 0 && Integer.parseInt(waterLevel) <= 17) {
                    waterVal.setText("Plants need to be watered check moisture levels...");
                    toastWaterPlants.setGravity(Gravity.BOTTOM, 0, 50);
                    toastWaterPlants.show();

                } else {
                    waterVal.setText("Plant moisture levels are wayy too high kinda...");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //TODO: HANDLE
            }
        });
    }

    public void waterNow(View v) {
        FirebaseDatabase realTimeDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = realTimeDatabase.getReference().child("Watering");
        Toast toastStopWatering = Toast.makeText(this, "Stopped watering...", Toast.LENGTH_SHORT); //cant put it in the addOncCompleteListener
        Toast toastWater = Toast.makeText(this, "Watering Plants...", Toast.LENGTH_SHORT);

        //this is if you want to get the data once use addOnCompleteListener
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            String watering = ""; // we always assuming the plants are not currently being watered

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                watering = task.getResult().getValue().toString();

                //TODO: in the arduino code set after 2+ seconds the watering should automatically stop
                if (Boolean.parseBoolean(watering) == true) {
                    //write change value in database to false
                    databaseReference.setValue(false);

                }
                if (Boolean.parseBoolean(watering) == false) {
                    //write change value in database to false
                    databaseReference.setValue(true);  //if not watering then water the plants
                    updateWateringHistoryDB();

                }
            } //we need the addOnComplete listener so we can change DB values in there, never change values in valueEvent Listener causes infinite loop
        });
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String result = snapshot.getValue().toString();
                if (Boolean.parseBoolean(result) == false) {
                    toastStopWatering.setGravity(Gravity.BOTTOM, 0, 50);
                    toastStopWatering.show();
                } else if (Boolean.parseBoolean(result) == true) {
                    toastWater.setGravity(Gravity.BOTTOM, 0, 50);
                    // Log.d("Watery","watering now");
                    toastWater.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateWateringHistoryDB() {
        FirebaseFirestore waterHistoryDataBase = FirebaseFirestore.getInstance();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("EEEE, dd MMM yyyy HH:mm:ss");
        String todayDate = format.format(date);
        Map<String, String> dateWatered = new HashMap<>();

        dateWatered.put("Last Watered", todayDate);  //so when you retrieve from DB you want to get a string
        waterHistoryDataBase.collection("Watering History").add(dateWatered)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Kidi", "Successfully Written to database");
                    }
                });
    }

    public void openWeather(View v) {
        Intent i = new Intent(this, weatherActivity.class);
        startActivity(i);
    }

    public void waterHistory(View v) {
    Intent i = new Intent(getApplicationContext(), WateringHistory.class);
    startActivity(i);
    }

}



