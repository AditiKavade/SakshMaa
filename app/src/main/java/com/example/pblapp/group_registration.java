package com.example.pblapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class group_registration extends AppCompatActivity {
    Button btnCreate;
    TextInputEditText etGroupName;
    TextInputEditText etAdminName;
    TextInputEditText etMobileNo;

    TextInputEditText etAadhaarNo;
    TextInputEditText etStartYear;
    TextInputEditText etStartMonth;
    TextInputEditText etMonthlySaving;

    TextInputEditText etAdminPass;
    private DatabaseReference database;


    //Random code generation
    public String generateRandomCode(int length)
    {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder(length);

        for(int i = 0; i < length; i++)
        {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }
        return code.toString();
    }
    private String generateUniqueCode()
    {
        String groupCode = generateRandomCode(6);
        //To check if the generated code already exists in the database
        database.child(groupCode).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    generateUniqueCode();
                }
            }
        });
        return groupCode;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_registration);
        btnCreate = findViewById(R.id.create_btn);
        etGroupName = findViewById(R.id.group_name);
        etAdminName = findViewById(R.id.admin_name);
        etMobileNo = findViewById(R.id.mobile_no);
        etAadhaarNo = findViewById(R.id.adhaar_no);
        etStartYear = findViewById(R.id.start_year);
        etStartMonth = findViewById(R.id.start_month);
        etMonthlySaving = findViewById(R.id.monthly_saving);
        etAdminPass = findViewById(R.id.admin_pass);
        database = FirebaseDatabase.getInstance().getReference("Groups");

        btnCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String groupName = etGroupName.getText().toString();
                String adminName = etAdminName.getText().toString();
                String mobileNo = etMobileNo.getText().toString();
                String aadhaarno = etAadhaarNo.getText().toString();
                String startYear = etStartYear.getText().toString();
                String startMonth = etStartMonth.getText().toString();
                String monthlySaving = etMonthlySaving.getText().toString();
                String adminPassword = etAdminPass.getText().toString();
                String groupCode = generateUniqueCode();
                String memberID = generateRandomCode(4);
                if(groupName.isEmpty() || adminName.isEmpty() || mobileNo.isEmpty() || aadhaarno.isEmpty() || startYear.isEmpty() || startMonth.isEmpty() ||monthlySaving.isEmpty() || adminPassword.isEmpty())
                {
                    if(groupName.isEmpty())
                    {
                        etGroupName.setError("Please enter Group name");
                    }
                    if(adminName.isEmpty())
                    {
                        etAdminName.setError("Please enter Admin name");
                    }
                    if(mobileNo.isEmpty())
                    {
                        etMobileNo.setError("Please enter Mobile number");
                    }
                    if(aadhaarno.isEmpty())
                    {
                        etAadhaarNo.setError("Please enter Aadhaar number");
                    }

                    if(startYear.isEmpty())
                    {
                        etStartYear.setError("Please enter Start Year");
                    }
                    if(startMonth.isEmpty())
                    {
                        etStartMonth.setError("Please enter Start Month");
                    }
                    if(monthlySaving.isEmpty())
                    {
                        etMonthlySaving.setError("Please enter Monthly saving");
                    }

                    if(adminPassword.isEmpty())
                    {
                        etAdminPass.setError("Please enter Password ");
                    }


                }
                else {

                    Group group = new Group(groupName, adminName, mobileNo, startYear, startMonth, monthlySaving, groupCode, adminPassword, 0, 0);

                    database.child(groupCode).setValue(group).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(group_registration.this, "Group created successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(group_registration.this, "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });


                    Member member = new Member(groupCode, memberID, adminName, mobileNo, aadhaarno, adminPassword, 0, 0,"No", "Yes");
                    database.child(groupCode).child("Members").child(memberID).setValue(member).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(group_registration.this, "Member added successfully", Toast.LENGTH_SHORT).show();
                            Intent grp_int = new Intent(group_registration.this, login_page.class);
                            startActivity(grp_int);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(group_registration.this, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    });
                }

                // Create a notification channel
                String channelId = "registration_channel";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence channelName = "Registration Channel";
                    String channelDescription = "Channel for registration notifications";
                    int importance = NotificationManager.IMPORTANCE_HIGH;

                    NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
                    channel.setDescription(channelDescription);
                    channel.enableLights(true);
                    channel.setLightColor(Color.RED);
                    channel.enableVibration(true);

                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                // Create the notification
                String message = "your group code is '"+groupCode+"' please make note this group code";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(group_registration.this, channelId)
                        .setSmallIcon(R.drawable.login_woman)
                        .setContentTitle("Thank You for registration "+groupCode)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .setFullScreenIntent(getFullScreenIntent(), true);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, builder.build());
            }// On click
        });
    }//onCreate

    private PendingIntent getFullScreenIntent() {
        // Create an explicit Intent for the target activity
        Intent intent = new Intent(group_registration.this, createGrp.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Create the PendingIntent with FLAG_IMMUTABLE flag
        return PendingIntent.getActivity(group_registration.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}