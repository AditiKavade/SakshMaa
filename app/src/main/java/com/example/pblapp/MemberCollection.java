package com.example.pblapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MemberCollection extends AppCompatActivity {

    TextInputLayout etCollName, etDate, etAmt, etLoan, etFine;
    Button btn, btn_save;
    Spinner spn;
    ArrayList<String> spinnerList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_collection);
        btn = findViewById(R.id.rec);
        etCollName = findViewById(R.id.ced);
        etDate = findViewById(R.id.collectionDate);
        etAmt = findViewById(R.id.amount);
        etLoan = findViewById(R.id.amountLoan);
        etFine = findViewById(R.id.fine);
        spn = findViewById(R.id.spinner);
        btn_save = findViewById(R.id.save);

        database = FirebaseDatabase.getInstance().getReference("Groups");

        Intent CollCAllable = getIntent();
        String gcode = CollCAllable.getStringExtra("fromAdminPageGrpCode");
        String admName = CollCAllable.getStringExtra("fromAdminPageuserName");

        adapter = new ArrayAdapter<>(MemberCollection.this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedMember = spinnerList.get(position);
                etCollName.getEditText().setText(selectedMember);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    //Clear the spinnerList before populating it
                    spinnerList.clear();

                    // Iterate each group
                    for (DataSnapshot groupSnap : snapshot.getChildren()) {
                        // Check if the group code matches
                        String groupCode = String.valueOf(groupSnap.child("group_code").getValue());
                        if (groupCode.equals(gcode)) {
                            // Take member child node saving members
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
                    Toast.makeText(MemberCollection.this, "Database error! Try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MemberCollection.this, "Database error! Try again", Toast.LENGTH_SHORT).show();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameOfMember = spn.getSelectedItem().toString();
                String collected_date = etDate.getEditText().getText().toString();
                String collected_amount = etAmt.getEditText().getText().toString();
                String collected_loanamount = etLoan.getEditText().getText().toString();
                String collected_loanfine = etFine.getEditText().getText().toString();

                if (collected_date.isEmpty() || collected_amount.isEmpty() || nameOfMember.isEmpty()) {
                    if (nameOfMember.isEmpty()) {
                        etCollName.setError("Choose member from list");
                    }
                    if (collected_date.isEmpty()) {
                        etDate.setError("Please enter collection date");
                    }
                    if (collected_amount.isEmpty()) {
                        etAmt.setError("Please enter collected amount");
                    }

                } else
                {

                    Collection collection = new Collection(nameOfMember, collected_date,  collected_amount, collected_loanamount, collected_loanfine);
                    database = FirebaseDatabase.getInstance().getReference("Groups").child(gcode);
                    //Total bachat gat amount calculation
                    database.child("total_amount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Long currentTotalAmount = snapshot.getValue(Long.class);
                            int newTotalAmount = currentTotalAmount != null ? currentTotalAmount.intValue() : 0;
                            newTotalAmount += Integer.parseInt(collected_amount);
                            database.child("total_amount").setValue(newTotalAmount);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    // To update individual collected amount
                    database.child("Members").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot memberSnap : snapshot.getChildren()) {
                                String memberName = memberSnap.child("memberName").getValue(String.class);
                                if (memberName.equals(nameOfMember)) {
                                    String memberID = memberSnap.child("memberID").getValue(String.class);
                                    int currentCollectedAmount = memberSnap.child("memberCollectedAmount").getValue(Integer.class);
                                    currentCollectedAmount += Integer.parseInt(collected_amount);
                                    database.child("Members").child(memberID).child("memberCollectedAmount").setValue(currentCollectedAmount);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MemberCollection.this, "Database error! Try again", Toast.LENGTH_SHORT).show();
                        }
                    });



                    //To check if date already exists
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //Date exists add the collection under that date
                                database.child("Collection").child(collected_date).child(nameOfMember).setValue(collection).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(MemberCollection.this, "Collection added successfully", Toast.LENGTH_SHORT).show();

                                                etCollName.getEditText().setText(" ");
                                                etDate.getEditText().setText(" ");
                                                etDate.getEditText().setText(" ");
                                                etAmt.getEditText().setText(" ");
                                                etLoan.getEditText().setText(" ");
                                                etFine.getEditText().setText(" ");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MemberCollection.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                //Date does not exists add date and then add the collection under that date
                                database.child("Collection").child(collected_date).child(nameOfMember).setValue(collection).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                                Toast.makeText(MemberCollection.this, "Collection added successfully", Toast.LENGTH_SHORT).show();
                                                etCollName.getEditText().setText(" ");
                                                etDate.getEditText().setText(" ");
                                                etAmt.getEditText().setText(" ");
                                                etLoan.getEditText().setText(" ");
                                                etFine.getEditText().setText(" ");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(MemberCollection.this, "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MemberCollection.this, "Database error! Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });//btn_save event

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intt = new Intent(MemberCollection.this, seeRecord.class);
                intt.putExtra("fromAdminPageGrpCode", gcode);
                startActivity(intt);
            }
        });
    }
}


