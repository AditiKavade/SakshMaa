package com.example.pblapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AllRecords extends AppCompatActivity {


    private LinearLayout linear;
    private ListView listView2;
    private Button cButton;
    private Bitmap bitmap;
    private String gCode;
    private String memberName;
    private DatabaseReference database;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_records);
        listView2 = findViewById(R.id.ListView2);
        ArrayList<Information2> list2 = new ArrayList<>();
        CustomAdapter adapter = new CustomAdapter(list2);
        listView2.setAdapter(adapter);

        Intent intent = getIntent();
        gCode = intent.getStringExtra("passGrpCode");
        memberName=intent.getStringExtra("passAdmName");

        database = FirebaseDatabase.getInstance().getReference("Groups");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot groupSnap : snapshot.getChildren()) {
                    String groupCode = String.valueOf(groupSnap.child("group_code").getValue());
                    if (groupCode.equals(gCode)) {
                        DataSnapshot collectionSnapShot = groupSnap.child("Collection");
                        for (DataSnapshot colSnap : collectionSnapShot.getChildren()) {
                            for (DataSnapshot dateSnap : colSnap.getChildren()) {
                                String nameOfMember = String.valueOf(dateSnap.child("nameOfMember").getValue());
                                if (nameOfMember.equals(memberName)) {
                                    String collectedAmount = String.valueOf(dateSnap.child("collected_amount").getValue());
                                    String collectedDate = String.valueOf(dateSnap.child("collected_date").getValue());

                                    Information2 info2 = new Information2(nameOfMember, collectedAmount, collectedDate);
                                    list2.add(info2);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            Toast.makeText(AllRecords.this, "Data Retrieved Successfully", Toast.LENGTH_SHORT).show();
                            Log.d("pbl", "hoo");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllRecords.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
            }
        });

        linear = findViewById(R.id.linearLayout);
        cButton = findViewById(R.id.createBtn);
        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("size", linear.getWidth() + " " + linear.getWidth());
                bitmap = LoadBitmap(linear, linear.getWidth(), linear.getHeight());

                createPDF();
            }
        });

    }
    private class CustomAdapter extends ArrayAdapter<Information2> {
        public CustomAdapter(ArrayList<Information2> list2) {
            super(AllRecords.this, 0, list2);
        }

        @Override
        public View getView(int position2, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.allrec_item, parent, false);
            }

            Information2 info2 = getItem(position2);
            TextView nameTextView2 = convertView.findViewById(R.id.nameTextView2);
            TextView amountTextView2 = convertView.findViewById(R.id.amountTextView2);
            TextView dateTextView2 = convertView.findViewById(R.id.dateTextView2);

            if (info2 != null) {
                nameTextView2.setText(info2.getNameOfMember());
                amountTextView2.setText(info2.getCollected_amount());
                dateTextView2.setText(info2.getCollected_date());
            }

            return convertView;
        }
    }
    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;

    }

    private void createPDF() {
        // Create a new PdfDocument instance
        PdfDocument document = new PdfDocument();
        LinearLayout linearLayout = findViewById(R.id.linearLayout);

        // Define the dimensions of the PDF page based on the layout's dimensions
        int width = linearLayout.getWidth();
        int height = linearLayout.getHeight();

        // Create a PageInfo object for the PDF page
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();

        // Start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the canvas of the page
        Canvas canvas = page.getCanvas();

        // Create a paint object for styling
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        // Draw a white background on the canvas
        canvas.drawRect(0, 0, width, height, paint);

        // Draw the linear layout on the canvas
        linearLayout.draw(canvas);

        // Finish the page
        document.finishPage(page);

        String fileName = "gat.pdf";

        // Get the root directory of the external storage
        File rootDir = Environment.getExternalStorageDirectory();

        // Create a new directory for the PDF file
        File pdfDir = new File(rootDir, "BachatGat");
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        // Create the file object
        File pdfFile = new File(pdfDir, fileName);

        try {
            // Create a FileOutputStream for writing the PDF
            FileOutputStream outputStream = new FileOutputStream(pdfFile);

            // Write the PDF document to the output stream
            document.writeTo(outputStream);

            // Close the output stream
            outputStream.close();

            // Show a success message
            Toast.makeText(this, "PDF generated successfully and saved to " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();

            // Show an error message
            Toast.makeText(this, "Allow Permission for Storage from settings", Toast.LENGTH_SHORT).show();
        }

        // Close the PDF document
        document.close();
    }
}
