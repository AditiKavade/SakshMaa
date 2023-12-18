package com.example.pblapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class admin_page extends AppCompatActivity {
    Button btnAddUser, btnCollection, btnLoan,eng,mar;
    Button viewbtn;
    TextView tvNameCode;
    TextView tvAdName;
    TextView bachatGatTotalAmt, bachatGatIndTotalAmt, bachatGatRemAmt, currentLoanDistributed;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        btnAddUser = findViewById(R.id.btnadduser);
        btnLoan = findViewById(R.id.loanbtn);
        btnCollection= findViewById(R.id.recordbtn);
        viewbtn =findViewById(R.id.viewbtn);
        tvNameCode =  findViewById(R.id.groupname);
        tvAdName = findViewById(R.id.adminname);
        bachatGatTotalAmt = findViewById(R.id.totalamtedit);
        bachatGatIndTotalAmt = findViewById(R.id.investamtedit);
        bachatGatRemAmt = findViewById(R.id.remamtval);
        currentLoanDistributed = findViewById(R.id.cloandisamt);

        Intent callableIntent = getIntent();
        Log.d("admin_page", "adminlogin ");
        String gName = callableIntent.getStringExtra("passGrpName");
        String gCode = callableIntent.getStringExtra("passGrpCode");
        String admName= callableIntent.getStringExtra("passAdmName");

        tvNameCode.append(gName + " [ " + gCode + " ]" );
        tvAdName.append(" "+admName);

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
                    if(memName.equals(admName))
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



        eng = findViewById(R.id.engbtnprofset);
        mar = findViewById(R.id.marbtnprofset);
        LanguageManager lang = new LanguageManager(this);
        eng.setOnClickListener(view -> {
            lang.updateResource("en");
            recreate();
        });

        mar.setOnClickListener(view -> {
            lang.updateResource("mr");
            recreate();
        });

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gCode.isEmpty()) {
                    Toast.makeText(admin_page.this, "Please enter a group code", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent AddU = new Intent(admin_page.this, AddUser.class);
                AddU.putExtra("fromAdminPageGrpCode", gCode);
                startActivity(AddU);
            }
        });

        btnCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gCode.isEmpty()) {
                    Toast.makeText(admin_page.this, "Please enter a group code", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent collection = new Intent(admin_page.this, MemberCollection.class);
                collection.putExtra("fromAdminPageuserName",admName);
                collection.putExtra("fromAdminPageGrpCode", gCode);
                startActivity(collection);
            }
        });

        btnLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (gCode.isEmpty()) {
                    Toast.makeText(admin_page.this, "Please enter a group code", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent loanI = new Intent(admin_page.this, loan.class);
                loanI.putExtra("fromAdminPageGrpCode", gCode);
                startActivity(loanI);
            }
        });
        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_page.this, viewinformation.class);

                i.putExtra("passGrpCode", gCode); // Pass group code
                i.putExtra("passGrpName", gName); // Pass group name
                i.putExtra("passUserName", admName); // Pass user name
                startActivity(i);
            }
        });
    }


}