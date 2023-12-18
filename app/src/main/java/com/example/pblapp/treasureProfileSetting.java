package com.example.pblapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class treasureProfileSetting extends AppCompatActivity {
    private DatabaseReference database;

    private EditText editName, editMobileNum, editAdhar, editPassword;
    private TextView userDisplay;
    private String currentGroupCode;

    private String grpCode;
    private String memberName;
    private boolean isSecretary = false;
    Button btnsave;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_profile_setting);
        editName = findViewById(R.id.treName);
        editMobileNum = findViewById(R.id.treMobileNum);
        editAdhar = findViewById(R.id.treAdhar);
        editPassword = findViewById(R.id.trePassword);
        userDisplay = findViewById(R.id.treUsername);
        btnsave = findViewById(R.id.treSavebtn);

        Intent intent = getIntent();
        grpCode = ((Intent) intent).getStringExtra("passGrpCode");
        memberName = intent.getStringExtra("passTreName");

        database = FirebaseDatabase.getInstance().getReference().child("Groups").child(grpCode).child("Members");

        database.orderByChild("memberName").equalTo(memberName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                        String memberID = memberSnapshot.getKey();
                        String memberName = memberSnapshot.child("memberName").getValue(String.class);
                        String memberMobileNum = memberSnapshot.child("memberMobileNumber").getValue(String.class);
                        String memberAadhar = memberSnapshot.child("memberAadhaar").getValue(String.class);
                        String memberPassword = memberSnapshot.child("memberPassword").getValue(String.class);

                        // ...
                        if (memberSnapshot.child("isSecretary").exists()) {
                            Object isSecretaryObj = memberSnapshot.child("isSecretary").getValue();
                            if (isSecretaryObj != null) {
                                isSecretary = Boolean.parseBoolean(isSecretaryObj.toString());
                            }
                        }
// ...



                        editName.setText(memberName);
                        editMobileNum.setText(memberMobileNum);
                        editAdhar.setText(memberAadhar);
                        editPassword.setText(memberPassword);
                        userDisplay.setText(memberName);

                        if (isSecretary) {
                            editName.setVisibility(View.VISIBLE);
                            editMobileNum.setVisibility(View.VISIBLE);
                            editAdhar.setVisibility(View.VISIBLE);
                            userDisplay.setVisibility(View.VISIBLE);
                        } else {
                            editName.setVisibility(View.GONE);
                            editMobileNum.setVisibility(View.GONE);
                            editAdhar.setVisibility(View.GONE);
                            userDisplay.setVisibility(View.GONE);
                        }

                        btnsave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = editName.getText().toString();
                                String mobileNum = editMobileNum.getText().toString();
                                String aadhar = editAdhar.getText().toString();
                                String password = editPassword.getText().toString();
                                String UserDisplayName = userDisplay.getText().toString();

                                DatabaseReference memberRef = database.child(memberID);
                                memberRef.child("memberName").setValue(name);
                                memberRef.child("memberMobileNumber").setValue(mobileNum);
                                memberRef.child("memberAadhaar").setValue(aadhar);
                                memberRef.child("memberPassword").setValue(password)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(treasureProfileSetting.this, "Member details updated successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(treasureProfileSetting.this, "Failed to update member details", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                if (isSecretary) {
                                    memberRef.child("isSecretary").setValue(true);  // Set or update the secretary flag
                                }
                            }
                        });

                        break; // Exit the loop after finding the member
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(treasureProfileSetting.this, "Database error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

