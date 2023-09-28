package com.example.driprocket;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


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

     public void dbManager(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("SensorData").child("Moisture");
        Toast toastWaterPlants = Toast.makeText(this, "Plants need to be watered...", Toast.LENGTH_SHORT);
         databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String waterLevel = snapshot.getValue().toString();
                TextView waterVal = findViewById(R.id.waterStatus);
                if(Integer.parseInt(waterLevel)>= 41 && Integer.parseInt(waterLevel) <= 80){
                    waterVal.setText("Plant moisture levels are good...");
                }else if(Integer.parseInt(waterLevel)>=0 && Integer.parseInt(waterLevel) <=41){
                    waterVal.setText("Plants need to be watered check moisture levels...");
                    toastWaterPlants.setGravity(Gravity.BOTTOM,0,50);
                    toastWaterPlants.show();

                } else{
                    waterVal.setText("Plant moisture levels are wayy too high kinda...");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
              //TODO: HANDLE
            }
        });
    }

    public void waterNow(View v){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Watering");

        Toast toastStopWatering = Toast.makeText(this, "Stopped watering...", Toast.LENGTH_SHORT); //cant put it in the addOncCompleteListener
        Toast toastWater = Toast.makeText(this, "Watering Plants...", Toast.LENGTH_SHORT);
        //this is if you want to get the data once
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            String watering;

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
               watering = task.getResult().getValue().toString();
                Log.d("kamva",watering);

                //TODO: in the arduino code set after 2+ seconds the watering should automatiically stop
                if(Boolean.parseBoolean(watering) == true){
                    //write change value in database to false
                    databaseReference.setValue(false);
                    Log.d("kamva","changed watering to false");
                    toastStopWatering.setGravity(Gravity.BOTTOM,0,50);
                    toastStopWatering.show();
                }if(Boolean.parseBoolean(watering) == false){
                    //write change value in database to false
                    databaseReference.setValue(true);
                    Log.d("kamva","changed watering to true");
                    toastWater.setGravity(Gravity.BOTTOM,0,50);
                    toastWater.show();
                }
            }
        });}

        public void read(View v){}

        public void openWeather(View v){

        Intent i = new Intent(this, weatherActivity.class);
        startActivity(i);
        }
}



