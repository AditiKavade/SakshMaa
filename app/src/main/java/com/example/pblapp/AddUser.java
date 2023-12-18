package com.example.pblapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class AddUser extends AppCompatActivity {
    TextInputEditText etAddUser;
    TextInputEditText etMob;
    TextInputEditText etAadhaar;
    TextInputEditText etMemberPass;
    Button btnAdd;

    RadioGroup Secreatry;
    RadioButton btnYes;
    RadioButton btnNo;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        database = FirebaseDatabase.getInstance().getReference("Groups");
        Intent adduserCAllable = getIntent();
        String gcode = adduserCAllable.getStringExtra("fromAdminPageGrpCode");

        etAddUser = findViewById(R.id.etusername);
        etMob = findViewById(R.id.etmob);
        etAadhaar = findViewById(R.id.etadhar);
        etMemberPass = findViewById(R.id.etmempass);
        Secreatry = findViewById(R.id.secretary);
        btnYes = findViewById(R.id.radioButtonYes);
        btnNo = findViewById(R.id.radioButtonNo);
        btnAdd = findViewById(R.id.addbtn);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String add_user = etAddUser.getText().toString();
                String user_mob = etMob.getText().toString();
                String user_aadhaar = etAadhaar.getText().toString();
                String user_password = etMemberPass.getText().toString();

                if (add_user.isEmpty() || user_mob.isEmpty() || user_aadhaar.isEmpty() || user_password.isEmpty() || (!btnYes.isChecked() && !btnNo.isChecked())) {
                    if (add_user.isEmpty()) {
                        etAddUser.setError("Please enter member name");
                    }
                    if (user_mob.isEmpty()) {
                        etMob.setError("Please enter member's mobile number");
                    }
                    if (user_aadhaar.isEmpty()) {
                        etAadhaar.setError("Please enter Aadhaar number");
                    }
                    if (user_password.isEmpty()) {
                        etMemberPass.setError("Please enter member's password");
                    }
                    if (!btnYes.isChecked() && !btnNo.isChecked()) {
                        Toast.makeText(AddUser.this, "Please select an option", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String secretary = btnYes.isChecked() ? "true" : "false";
                    String memberID = generateRandomCode(4);
                    Member member = new Member(gcode, memberID, add_user, user_mob, user_aadhaar, user_password, 0, 0, secretary, "No");

                    database.child(gcode).child("Members").child(memberID).setValue(member)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddUser.this, "Member added successfully", Toast.LENGTH_SHORT).show();
                                    etAddUser.setText("");
                                    etMob.setText("");
                                    etAadhaar.setText("");
                                    etMemberPass.setText("");
                                    Secreatry.clearCheck();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddUser.this, "Failed to add member", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    public String generateRandomCode(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }
}
