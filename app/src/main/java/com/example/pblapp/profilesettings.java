package com.example.pblapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pblapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profilesettings extends AppCompatActivity {

    private DatabaseReference database;

    private EditText editName, editMobileNum, editAdhar, editPassword;
    TextView AdminDisplay;
    private String currentGroupCode; // Replace with your group code
    //private String currentMemberId = "FITp"; // Replace with the ID of the currently logged in member
    private String grpCode;
    private String grpName;
    private String userName;

    Button btnsave;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        editName = findViewById(R.id.EditName);
        editMobileNum = findViewById(R.id.EditMobileNum);
        editAdhar = findViewById(R.id.EditAdhar);
        editPassword = findViewById(R.id.EditPassword);
        AdminDisplay=findViewById(R.id.name);
        btnsave = findViewById(R.id.savebtn);


        Intent intent = getIntent();
        grpCode = intent.getStringExtra("passGrpCode");
        grpName = intent.getStringExtra("passGrpName");
        userName = intent.getStringExtra("passUserName");

        if (grpCode != null) {
            // Initialize Firebase
            database = FirebaseDatabase.getInstance().getReference().child("Groups").child(grpCode);

            // Rest of the code...
        } else {
            Toast.makeText(profilesettings.this, "Group code not found", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the group code is null
        }

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference().child("Groups").child(grpCode);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String name = snapshot.child("admin_name").getValue(String.class);
                    String password = snapshot.child("admin_password").getValue(String.class);
                    String mobileNum = snapshot.child("mobile_no").getValue(String.class);

                    // Setting the retrieved data to EditText views
                    editName.setText(name);
                    editPassword.setText(password);
                    editMobileNum.setText(mobileNum);
                    AdminDisplay.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profilesettings.this, "Database error!", Toast.LENGTH_SHORT).show();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform input validation here before updating the values

                database.child("admin_name").setValue(editName.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(profilesettings.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profilesettings.this, "Failed to update name", Toast.LENGTH_SHORT).show();
                            }
                        });

                database.child("admin_password").setValue(editPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(profilesettings.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profilesettings.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        });

                database.child("mobile_no").setValue(editMobileNum.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(profilesettings.this, "Mobile number updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(profilesettings.this, "Failed to Mobile number", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}