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

public class Userlogin extends AppCompatActivity {
    TextView tvMemCode , tvMemName, tvTotalUserAmount,tvIndividualAmount, tvBachatGatRemAmt, currentLoanDistributed, tvtotalamtedituser;
    TextView tvinvestamtedituser;
    private DatabaseReference database;

    Button btnallinfo, profsetting, grpinfo,eng,mar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        tvMemName = findViewById(R.id.groupnameuser);
        tvMemCode = findViewById(R.id.adminnameuser);
        tvTotalUserAmount = findViewById(R.id.totalamtuser);
        btnallinfo = findViewById(R.id.allrecuser);
        tvIndividualAmount = findViewById(R.id.youramtuser);
        tvBachatGatRemAmt = findViewById(R.id.remamtvaluser);
        currentLoanDistributed = findViewById(R.id.cloandisamtuser);
        eng=findViewById(R.id.engbtnprofset1);
        mar=findViewById(R.id.marbtnprofset1);
        tvtotalamtedituser = findViewById(R.id.totalamtedituser);
        tvinvestamtedituser = findViewById(R.id.investamtedituser);

        Intent callableIntent = getIntent();
        String gName = callableIntent.getStringExtra("passGrpName");
        String gCode = callableIntent.getStringExtra("passGrpCode");
        String memberName= callableIntent.getStringExtra("passAdmName");

        tvMemName.append(gName + " [ " + gCode + " ]" );
        tvMemCode.append(" "+memberName);

        database = FirebaseDatabase.getInstance().getReference("Groups").child(gCode);
        //Total bachat gat amount calculation
        database.child("total_amount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long currentTotalAmount = snapshot.getValue(Long.class);
                tvtotalamtedituser.setText(String.valueOf(currentTotalAmount));

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
                tvBachatGatRemAmt.setText(String.valueOf(Rem));
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
                    if(memName.equals(memberName))
                    {
                        Log.d("names matched", "onDataChange: ");
                        Long IndividualTotalAmount = memSnap.child("memberCollectedAmount").getValue(Long.class);
                        tvinvestamtedituser.setText(String.valueOf(IndividualTotalAmount));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LanguageManager lang = new LanguageManager(this);
        eng.setOnClickListener(view -> {
            lang.updateResource("en");
            recreate();
        });

        mar.setOnClickListener(view -> {
            lang.updateResource("mr");
            recreate();
        });

        btnallinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent allinfointent = new Intent(Userlogin.this,AllRecords.class);
                allinfointent.putExtra("passGrpCode", gCode);
                allinfointent.putExtra("passAdmName",memberName);
                startActivity(allinfointent);
            }
        });

        profsetting = findViewById(R.id.btnprofilesettinguser);
        profsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profsettingintent = new Intent(Userlogin.this,userProfileSetting.class);
                profsettingintent.putExtra("passGrpCode", gCode);
                profsettingintent.putExtra("passAdmName",memberName);
                startActivity(profsettingintent);
            }
        });

        grpinfo = findViewById(R.id.btnbachatgatinfouser);
        grpinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grpinfointent = new Intent(Userlogin.this, GroupInformation.class);
                grpinfointent.putExtra("passGrpCode", gCode);
                grpinfointent.putExtra("passGrpName", gName);
                grpinfointent.putExtra("passUserName", memberName);
                startActivity(grpinfointent);
            }
        });

    }
}