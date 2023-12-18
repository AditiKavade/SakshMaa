package com.example.pblapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupInformation extends AppCompatActivity {
    private DatabaseReference database;
    ImageButton arrow;

    private TextView GroupName, DiplayGrpCode, Tmembers, StartMon, AdminName, AdmMobile;
    private String grpCode;
    private String grpName;
    private String userName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_information);
        arrow = findViewById(R.id.infoarrow);

        GroupName = findViewById(R.id.Edit_Bachat_gat_name);
        DiplayGrpCode = findViewById(R.id.Edit_Bachat_gat_code);
        Tmembers = findViewById(R.id.Edit_Total_member);
        StartMon = findViewById(R.id.Edit_start_month);
        AdminName = findViewById(R.id.Edit_Admin_info_name);
        AdmMobile = findViewById(R.id.Edit_Mobile_number);

        Intent intent = getIntent();
        grpCode = intent.getStringExtra("passGrpCode");
        grpName = intent.getStringExtra("passGrpName");
        userName = intent.getStringExtra("passUserName");

        database = FirebaseDatabase.getInstance().getReference().child("Groups").child(grpCode);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(GroupInformation.this, viewinformation.class);
                startActivity(back);
            }
        });

        // Read the group information from the database and display it
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String groupName = dataSnapshot.child("group_name").getValue(String.class);
                    String groupCode = dataSnapshot.child("group_code").getValue(String.class);
                    String startMonth = dataSnapshot.child("start_month").getValue(String.class);
                    long totalMembers = dataSnapshot.child("Members").getChildrenCount();
                    String adminName = dataSnapshot.child("admin_name").getValue(String.class);
                    String adminMobile = dataSnapshot.child("mobile_no").getValue(String.class);

                    GroupName.setText(groupName);
                    DiplayGrpCode.setText(groupCode);
                    Tmembers.setText(String.valueOf(totalMembers));
                    StartMon.setText(startMonth);
                    AdminName.setText(adminName);
                    AdmMobile.setText(adminMobile);
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}