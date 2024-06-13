package com.example.citywatchapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class complaintDetails extends AppCompatActivity {

    private TextView detailsTitle, detailsCategory, detailsDescription, detailsLocation, detailsStatus;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_details);

        // Initialize TextViews
        detailsTitle = findViewById(R.id.detailsTitle);
        detailsCategory = findViewById(R.id.detailsCategory);
        detailsDescription = findViewById(R.id.detailsDescription);
        detailsLocation = findViewById(R.id.detailsLocation);
        detailsStatus = findViewById(R.id.detailsStatus);

        // Get complaint title passed from previous activity
        String complaintTitle = getIntent().getStringExtra("complaintTitle");

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Retrieve complaint details from Firestore based on title
        db.collection("complaints")
                .whereEqualTo("title", complaintTitle)
                .limit(1) // Limit to 1 document (assuming titles are unique)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Get the document snapshot
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);

                            // Get complaint details from document
                            String category = document.getString("category");
                            String description = document.getString("description");
                            String location = document.getString("location");
                            String status; // Declare status variable

                            // Check if "status" field exists before accessing it
                            if (document.contains("status")) {
                                status = document.getString("status");
                            } else {
                                status = "Data unavailable"; // Handle missing data (optional)
                            }

                            // Set complaint details to TextViews
                            detailsTitle.setText("Title: " + complaintTitle);
                            detailsCategory.setText("Category: " + category);
                            detailsDescription.setText("Description: " + description);
                            detailsLocation.setText("Location: " + location);
                            detailsStatus.setText("Status: " + status);
                        } else {
                            // Handle error or complaint not found
                        }
                    }
                });
    }
}