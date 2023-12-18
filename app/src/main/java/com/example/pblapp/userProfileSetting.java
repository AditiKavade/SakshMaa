package com.example.pblapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class userProfileSetting extends AppCompatActivity {
    private DatabaseReference database;

    private EditText editName, editMobileNum, editAdhar, editPassword;
    private TextView userDisplay;
    private String currentGroupCode;

    private String grpCode;
    private String memberName;
    Button btnsave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_setting);

        editName = findViewById(R.id.userEditName);
        editMobileNum = findViewById(R.id.userEditMobileNum);
        editAdhar = findViewById(R.id.userEditAdhar);
        editPassword = findViewById(R.id.userEditPassword);
        userDisplay=findViewById(R.id.username);
        btnsave = findViewById(R.id.usersavebtn);

        Intent intent = getIntent();
        grpCode = intent.getStringExtra("passGrpCode");
        memberName=intent.getStringExtra("passAdmName");

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

                        // Set the retrieved data to EditText views
                        editName.setText(memberName);
                        editMobileNum.setText(memberMobileNum);
                        editAdhar.setText(memberAadhar);
                        editPassword.setText(memberPassword);
                        userDisplay.setText(memberName);

                        // Update member details
                        btnsave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = editName.getText().toString();
                                String mobileNum = editMobileNum.getText().toString();
                                String aadhar = editAdhar.getText().toString();
                                String password = editPassword.getText().toString();
                                String UserDisplayName=userDisplay.getText().toString();

                                DatabaseReference memberRef = database.child(memberID);
                                memberRef.child("memberName").setValue(name);
                                memberRef.child("memberMobileNumber").setValue(mobileNum);
                                memberRef.child("memberAadhaar").setValue(aadhar);
                                memberRef.child("memberPassword").setValue(password)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(userProfileSetting.this, "Member details updated successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(userProfileSetting.this, "Failed to update member details", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });

                        break; // Exit the loop after finding the member
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(userProfileSetting.this, "Database error!", Toast.LENGTH_SHORT).show();
            }
        });


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform input validation here before updating the values

                database.child("memberName").setValue(editName.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(profilesettings.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(userProfileSetting.this, "Failed to update name", Toast.LENGTH_SHORT).show();
                            }
                        });

                database.child("memberPassword").setValue(editPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(userProfileSetting.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(userProfileSetting.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        });

                database.child("memberMobileNumber").setValue(editMobileNum.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(profilesettings.this, "Mobile number updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(userProfileSetting.this, "Failed to update Mobile number", Toast.LENGTH_SHORT).show();
                            }
                        });

                database.child("memberAadhaar").setValue(editMobileNum.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toast.makeText(profilesettings.this, "Mobile number updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(userProfileSetting.this, "Failed to update Aadhar Number", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }
}