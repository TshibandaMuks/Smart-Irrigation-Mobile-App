package com.example.driprocket;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WateringHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watering_history);
        getData(); //display the data immediately when the activity is launched
    }

    private void getData() {
        FirebaseFirestore waterHistoryDB = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.wateringHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<WateringHistoryItems> list = new ArrayList<>();

        //set your toast
        Toast toast = Toast.makeText(this,"Data received successfully!", LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,50);

        TextView waterHistoryCount = findViewById(R.id.waterHistoryCount);

        //if database is empty means there is no watering history therefore display theres nothing
        TextView noWaterHistoryText = findViewById(R.id.noWaterHistoryText);
        waterHistoryDB.collection("Watering History").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String name = document.getString("Last Watered");
                    //Log.d("Check",name); //log the data to screen for debugging
                    list.add(new WateringHistoryItems(name));
                }
                if(list.isEmpty()){
                    //then display that is no watering history
                    noWaterHistoryText.setText("Watering History is empty");
                    toast.show(); //say you got the data
                    waterHistoryCount.setText("Number of times watered: "+ list.size()); //display number of times watered
                    //you want to display the empty recycle view again so the previous list it removed from screen
                    recyclerView.setAdapter(new WateringHistoryAdapter(WateringHistory.this,list));
                }else{
                    recyclerView.setAdapter(new WateringHistoryAdapter(WateringHistory.this,list));
                    waterHistoryCount.setText("Number of times watered: "+ list.size()); //display number of times watered
                    toast.show(); //say you got the data
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Firestore", "Error getting documents.", e);
                toast.setText("Failed to get Data from database!");
                toast.show(); //say you got the data
            }
        });
    }
    public void clearWateringHistory(View view) {

        Toast toast = Toast.makeText(this,"Successfully deleted history!", LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,50);
        FirebaseFirestore waterHistoryDB = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = waterHistoryDB.collection("Watering History");

        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(QueryDocumentSnapshot result: task.getResult()){
                            result.getReference().delete(); //delete each document
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //TODO : add a toast
                           toast.show();
                           //after successfully deleting documents delete entire collection
                           getData();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //TODO : add a toast
                        toast.setText("Failed to delete history");
                        toast.show();
                    }
                });
    }
}