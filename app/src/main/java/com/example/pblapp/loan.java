package com.example.pblapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class loan extends AppCompatActivity {
    TextInputLayout etlCollName, etlDate, etlLoanAmt, etlInterest, etCalLoan;
    Button btn, btn_add, btnCalAmtHint;
    double calculatedAmountToReturn;
    String nameOfMember, orignalLoanAmt;
    Spinner spn;
    ArrayList<String> spinnerList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        etlCollName = findViewById(R.id.LA);
        etlDate = findViewById(R.id.LoanDate);
        etlLoanAmt = findViewById(R.id.Lamnt);
        etlInterest = findViewById(R.id.intrest);
        btn = findViewById(R.id.history);
        spn = findViewById(R.id.loanspin);
        btn_add = findViewById(R.id.Submitt);
        btnCalAmtHint = findViewById(R.id.LoanWithIInterest);
        etCalLoan = findViewById(R.id.FetchedDetails);

        database = FirebaseDatabase.getInstance().getReference("Groups");
        Intent CollCAllable = getIntent();
        String gcode = CollCAllable.getStringExtra("fromAdminPageGrpCode");

        adapter = new ArrayAdapter<>(loan.this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedMember = spinnerList.get(position);
                etlCollName.getEditText().setText(selectedMember);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Nothing
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    // Clear the spinnerList before populating it
                    spinnerList.clear();

                    // Iterate each group
                    for (DataSnapshot groupSnap : snapshot.getChildren()) {
                        // Check if the group code matches
                        String groupCode = String.valueOf(groupSnap.child("group_code").getValue());
                        if (groupCode.equals(gcode)) {
                            // Take "Members" child node saving members
                            DataSnapshot memberSnapShot = groupSnap.child("Members");
                            // Iterate each member
                            for (DataSnapshot memSnap : memberSnapShot.getChildren()) {
                                String memName = memSnap.child("memberName").getValue(String.class);
                                spinnerList.add(memName);
                            }
                            break; // Break the loop once the group is found
                        }
                    }

                    adapter.notifyDataSetChanged(); // Notify the adapter about the data change
                } catch (Exception e) {
                    Toast.makeText(loan.this, "Database error! Try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(loan.this, "Database error! Try again", Toast.LENGTH_SHORT).show();
            }
        });

        // Calculation of Loan
        btnCalAmtHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = etlLoanAmt.getEditText().getText().toString();
                int result = Integer.parseInt(amount);
                calculatedAmountToReturn = result + (result * 0.02);
                etCalLoan.getEditText().setText(String.valueOf(calculatedAmountToReturn));

            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameOfMember = spn.getSelectedItem().toString();
                String loan_date = etlDate.getEditText().getText().toString();
                String loan_amount = etlLoanAmt.getEditText().getText().toString();
                // String loan_interest = etlInterest.getEditText().getText().toString();

                if (loan_date.isEmpty() || loan_amount.isEmpty() || nameOfMember.isEmpty()) {
                    if (nameOfMember.isEmpty()) {
                        etlCollName.setError("Choose member from list");
                    }
                    if (loan_date.isEmpty()) {
                        etlDate.setError("Please enter collection date");
                    }
                    if (loan_amount.isEmpty()) {
                        etlLoanAmt.setError("Please enter collected amount");
                    }
                } else {
                    database = FirebaseDatabase.getInstance().getReference("Groups").child(gcode);
                    int la = Integer.parseInt(loan_amount);
                    orignalLoanAmt = loan_amount;

                    database.child("total_amount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                int ta = snapshot.getValue(Integer.class);
                                if (la > ta) {
                                    etlLoanAmt.setError("Loan amount exceeding total amount of group");
                                    Toast.makeText(loan.this, "Loan amount exceeding total amount of group", Toast.LENGTH_SHORT).show();
                                } else {
                                    ClassLoan classLoan = new ClassLoan(nameOfMember, loan_date, loan_amount, "0.02", calculatedAmountToReturn);
                                    DatabaseReference database_ref = FirebaseDatabase.getInstance().getReference("Groups").child(gcode);

                                    //total given amount calculation
                                    database_ref.child("total_given_loan").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Long currentTotalGivenLoan = snapshot.getValue(Long.class);
                                            long newTotalAmountLoan = currentTotalGivenLoan != null ? currentTotalGivenLoan : 0L;
                                            newTotalAmountLoan += Long.parseLong(orignalLoanAmt);
                                            database.child("total_given_loan").setValue(newTotalAmountLoan);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(loan.this, "Database error! Try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    database_ref.child("LoanRecord").child(nameOfMember).setValue(classLoan).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(loan.this, "Loan Record added successfully", Toast.LENGTH_SHORT).show();
                                            etlCollName.getEditText().setText("");
                                            etlDate.getEditText().setText("");
                                            etlLoanAmt.getEditText().setText("");
                                            // Setting takenLoan field for the member
                                            DatabaseReference membersRef = FirebaseDatabase.getInstance().getReference("Groups").child(gcode).child("Members");
                                            membersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot memberSnap : snapshot.getChildren()) {
                                                        String memberName = memberSnap.child("memberName").getValue(String.class);
                                                        if (memberName.equals(nameOfMember)) {
                                                            String memberID = memberSnap.getKey(); // Use getKey() to get the member ID
                                                            double currentCollectedAmount = memberSnap.child("takenLoan").getValue(Double.class);
                                                            currentCollectedAmount += calculatedAmountToReturn;
                                                            membersRef.child(memberID).child("takenLoan").setValue(currentCollectedAmount);
                                                            break;
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(loan.this, "Database error! Try again", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(loan.this, "Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(loan.this, "Database error! Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent His = new Intent(loan.this, LoanHistory.class);
                His.putExtra("fromAdminPageGrpCode", gcode);
                startActivity(His);
            }
        });
    }
}
