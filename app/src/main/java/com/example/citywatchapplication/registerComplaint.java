package com.example.citywatchapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class registerComplaint extends AppCompatActivity {

    private EditText editTextTitle, editTextDescription, editTextLocation;
    private Spinner spinnerCategory;
    private Button buttonSubmitComplaint;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextLocation = findViewById(R.id.editTextLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSubmitComplaint = findViewById(R.id.buttonSubmitComplaint);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.complaint_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setSelection(0);

        buttonSubmitComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComplaint();
            }
        });
    }

    private void submitComplaint() {
        String title = editTextTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || category.isEmpty()) {
            Toast.makeText(registerComplaint.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new complaint object
        Map<String, Object> complaint = new HashMap<>();
        complaint.put("title", title);
        complaint.put("description", description);
        complaint.put("location", location);
        complaint.put("category", category);
        complaint.put("userID", mAuth.getCurrentUser().getUid());
        // Add status field with default value "Active"
        complaint.put("status", "Pending");

        // Add the complaint to the Firestore database
        db.collection("complaints")
                .add(complaint)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(registerComplaint.this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show();

                            // Create a new instance of ComplaintData
                            complaintData complaintData = new complaintData(title, description, location, category, mAuth.getCurrentUser().getUid());
                            // Start complaintDetails activity and pass complaint data
                            Intent detailsIntent = new Intent(registerComplaint.this, complaintDetails.class);
                            detailsIntent.putExtra("complaintData", complaintData);
                            startActivity(detailsIntent);

                            // Start complaintStatus activity and pass title data
                            Intent statusIntent = new Intent(registerComplaint.this, MainActivity.class);
                            statusIntent.putExtra("complaintTitle", title);
                            startActivity(statusIntent);

                            finish();
                        } else {
                            Toast.makeText(registerComplaint.this, "Failed to submit complaint", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}