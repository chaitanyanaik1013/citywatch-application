package com.example.citywatchapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class complaintStatus extends AppCompatActivity {

    private static final String TAG = "complaintStatus";

    private ArrayList<String> complaintTitles;
    private ArrayAdapter<String> adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_status);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize ListView and adapter
        ListView listViewComplaints = findViewById(R.id.listViewComplaints);
        complaintTitles = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, complaintTitles);
        listViewComplaints.setAdapter(adapter);

        // Retrieve complaint titles with status "Active" from Firestore and update adapter
        db.collection("complaints")
                .whereEqualTo("status", "Pending")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            complaintTitles.clear(); // Clear previous data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                complaintTitles.add(title); // Add title to list
                            }
                            adapter.notifyDataSetChanged(); // Notify adapter of data change
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(complaintStatus.this, "Error getting complaints", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Handle item click in ListView
        listViewComplaints.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected complaint title
                String selectedTitle = complaintTitles.get(position);

                // Open complaintDetails activity and pass the selected complaint title
                Intent intent = new Intent(complaintStatus.this, complaintDetails.class);
                intent.putExtra("complaintTitle", selectedTitle);
                startActivity(intent);
            }
        });
    }
}