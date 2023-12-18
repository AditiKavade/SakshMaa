package com.example.pblapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class treasurer extends AppCompatActivity {
    Button btnallinfo, profsetting, grpinfo, loan, eng, mar;
    TextView groupnamev;
    TextView adminnamev;
    private String gCode;
    private String grpName;
    private String userName;
    TextView bachatGatTotalAmt, bachatGatIndTotalAmt, bachatGatRemAmt, currentLoanDistributed;
    private DatabaseReference database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasurer);

        groupnamev = findViewById(R.id.groupnameuser);
        adminnamev = findViewById(R.id.astresurer);
        bachatGatTotalAmt = findViewById(R.id.totalamtedituser);
        bachatGatIndTotalAmt = findViewById(R.id.investamtedituser);
        bachatGatRemAmt = findViewById(R.id.remamtvaluser);
        currentLoanDistributed = findViewById(R.id.cloandisamtuser);

        Intent intent = getIntent();
        gCode = intent.getStringExtra("passGrpCode");
        grpName = intent.getStringExtra("passGrpName");
        userName = intent.getStringExtra("passAdmName");


        groupnamev.append(grpName + " [ " + gCode + " ]");
        adminnamev.append(" " + userName);

        database = FirebaseDatabase.getInstance().getReference("Groups").child(gCode);

        //Total bachat gat amount calculation
        database.child("total_amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long currentTotalAmount = snapshot.getValue(Long.class);
                bachatGatTotalAmt.setText(String.valueOf(currentTotalAmount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //current loan distribued
        database.child("total_given_loan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long currentLoanGivenAmount = snapshot.getValue(Long.class);
                currentLoanDistributed.setText(String.valueOf(currentLoanGivenAmount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Remaining total amount of bachat gat calculation
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long TotalAmount = snapshot.child("total_amount").getValue(Long.class);
                Long TotalLoan = snapshot.child("total_given_loan").getValue(Long.class);
                long Rem = TotalAmount - TotalLoan;
                bachatGatRemAmt.setText(String.valueOf(Rem));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //Individual amount display
        database.child("Members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot memSnap : snapshot.getChildren())
                {
                    String memName = memSnap.child("memberName").getValue(String.class);
                    if(memName.equals(userName))
                    {
                        Log.d("names matched", "onDataChange: ");
                        Long IndividualTotalAmount = memSnap.child("memberCollectedAmount").getValue(Long.class);
                        bachatGatIndTotalAmt.setText(String.valueOf(IndividualTotalAmount));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        eng = findViewById(R.id.engbtnprofset2);
        mar = findViewById(R.id.marbtnprofset2);
        LanguageManager lang = new LanguageManager(this);
        eng.setOnClickListener(view -> {
            lang.updateResource("en");
            recreate();
        });

        mar.setOnClickListener(view -> {
            lang.updateResource("mr");
            recreate();
        });

        profsetting = findViewById(R.id.btnprofilesettinguser);
        profsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent treasurerProfile = new Intent(treasurer.this,treasureProfileSetting.class);
                treasurerProfile.putExtra("passGrpCode", gCode);
                treasurerProfile.putExtra("passTreName", userName);
                startActivity(treasurerProfile);
            }
        });

        grpinfo = findViewById(R.id.btnbachatgatinfouser);
        grpinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grpinfointent = new Intent(treasurer.this, GroupInformation.class);
                grpinfointent.putExtra("passGrpCode", gCode);
                grpinfointent.putExtra("passGrpName", grpName);
                grpinfointent.putExtra("passUserName", userName);
                startActivity(grpinfointent);
            }
        });

        loan = findViewById(R.id.btnloan);
        loan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loan = new Intent(treasurer.this, loan.class);
                startActivity(loan);
            }
        });

    }
}