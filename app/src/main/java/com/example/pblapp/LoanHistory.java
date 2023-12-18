package com.example.pblapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoanHistory extends AppCompatActivity {
    private ListView listView1;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_history);

        listView1 = findViewById(R.id.ListView1);
        ArrayList<Information1> list1 = new ArrayList<>();
        CustomAdapter adapter = new CustomAdapter(list1);
        listView1.setAdapter(adapter);
        Intent intent = getIntent();
        String gcode = intent.getStringExtra("fromAdminPageGrpCode");

        database = FirebaseDatabase.getInstance().getReference("Groups");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot groupSnap : snapshot.getChildren()) {
                    String groupCode = String.valueOf(groupSnap.child("group_code").getValue());
                    if (groupCode.equals(gcode)) {
                        DataSnapshot collectionSnapShot = groupSnap.child("LoanRecord");
                        for (DataSnapshot colSnap : collectionSnapShot.getChildren()) {
                            String nameOfMember = String.valueOf(colSnap.child("nameOfMember").getValue());
                            String loanAmount = String.valueOf(colSnap.child("loanAmount").getValue());
                            String loanDate = String.valueOf(colSnap.child("loanDate").getValue());

                            Information1 info1 = new Information1( nameOfMember, loanAmount, loanDate);

                            list1.add(info1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    Toast.makeText(LoanHistory.this, "Data Retrieved Successfully", Toast.LENGTH_SHORT).show();
                    Log.d("pbl", "hoo");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoanHistory.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CustomAdapter extends ArrayAdapter<Information1> {
        public CustomAdapter(ArrayList<Information1> list1) {
            super(LoanHistory.this, 0, list1);
        }

        @Override
        public View getView(int position1, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.loan_item, parent, false);
            }

            Information1 info1 = getItem(position1);
            TextView nameTextView1 = convertView.findViewById(R.id.nameTextView1);
            TextView amountTextView1 = convertView.findViewById(R.id.amountTextView1);
            TextView dateTextView1 = convertView.findViewById(R.id.dateTextView1);

            if (info1 != null) {
                nameTextView1.setText(info1.getNameOfMember());
                amountTextView1.setText(info1.getloanAmount());
                dateTextView1.setText(info1.getloanDate());
            }

            return convertView;
        }
    }
}