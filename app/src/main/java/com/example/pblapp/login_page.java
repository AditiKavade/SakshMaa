package com.example.pblapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

interface AdminValidationCallback
{
    void onAdminValidationResult(boolean isValid);
}
interface UserValidationCallback
{
    void onUserValidationResult(boolean isValid);
}
interface SecretaryValidationCallback
{
    void onSecretaryValidationResult(boolean isValid);
}
public class login_page extends AppCompatActivity {
    TextInputEditText etGroupCode;
    TextInputEditText tvGroupName;
    TextInputEditText etUserName;
    TextInputEditText etPassword;
    Button btnSearch;
    Button btnLogin;

    RadioGroup RadioGroupRoles;
    RadioButton radioButtonAdmin;
    RadioButton radioButtonSecreatary;
    RadioButton radioButtonMember;

    private DatabaseReference database;
    public static final String SHARED_PREFS ="sharedPrefs";
    private static final String PREF_GROUP_NAME = "group_name";
    private static final String PREF_GROUP_CODE = "group_code";
    private static final String PREF_USER_NAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        etGroupCode = findViewById(R.id.group_code);
        tvGroupName = findViewById(R.id.group_name_login);
        btnSearch = findViewById(R.id.btn_search);
        etUserName = findViewById(R.id.uname);
        etPassword = findViewById(R.id.passw);
        btnLogin = findViewById(R.id.btnLogin);
        RadioGroupRoles = findViewById(R.id.radioGroupRoles);
        radioButtonAdmin = findViewById(R.id.radioButtonAdmin);
        radioButtonSecreatary = findViewById(R.id.radioButtonTreasurer);
        radioButtonMember = findViewById(R.id.radioButtonMember);

        //getting reference to root node in database (initializing database)
        database = FirebaseDatabase.getInstance().getReference("Groups");
        // Retrieve stored data
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String storedGroupName = sharedPreferences.getString(PREF_GROUP_NAME, "");
        String storedGroupCode = sharedPreferences.getString(PREF_GROUP_CODE, "");
        String storedUserName = sharedPreferences.getString(PREF_USER_NAME, "");

        // Set values to the corresponding fields
        tvGroupName.setText(storedGroupName);
        etGroupCode.setText(storedGroupCode);
        etUserName.setText(storedUserName);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String group_code = etGroupCode.getText().toString();

                if(!group_code.isEmpty())
                {
                    readDataCode(group_code);
                }
                else
                {
                    Toast.makeText(login_page.this, "Group not found!", Toast.LENGTH_SHORT).show();
                }
            }//onClick
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String group_name = tvGroupName.getText().toString();
                String group_code = etGroupCode.getText().toString();
                String user_name = etUserName.getText().toString();
                String password = etPassword.getText().toString();

                if(validateInputs(group_code, user_name, password))
                {
                    //Action based on selected user role
                    if(radioButtonAdmin.isChecked())
                    {
                        //check if admin is valid or not
                        isAdminValid(group_code, user_name, password, new AdminValidationCallback()
                        {
                            @Override
                            public void onAdminValidationResult(boolean isValid) {
                                if (isValid)
                                {
                                    //valid admin
                                    Toast.makeText(login_page.this, "Admin login successful", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(PREF_GROUP_NAME, group_name);
                                    editor.putString(PREF_GROUP_CODE, group_code);
                                    editor.putString(PREF_USER_NAME, user_name);
                                    editor.apply();

                                    Intent intent = new Intent(login_page.this, admin_page.class);

                                    intent.putExtra("passGrpName", group_name);
                                    intent.putExtra("passGrpCode", group_code);
                                    intent.putExtra("passAdmName", user_name);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(login_page.this, "Invalid admin credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });//isAdminValid
                    }//if
                    else if (radioButtonMember.isChecked()) {
                        //To check if member is valid or not
                        isUserValid(group_code, user_name, password, new UserValidationCallback()
                        {
                            @Override
                            public void onUserValidationResult(boolean isValid) {
                                if (isValid)
                                {
                                    //valid admin
                                    Toast.makeText(login_page.this, "Member login successful", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(PREF_GROUP_NAME, group_name);
                                    editor.putString(PREF_GROUP_CODE, group_code);
                                    editor.putString(PREF_USER_NAME, user_name);
                                    editor.apply();
                                    Intent intent = new Intent(login_page.this, Userlogin.class);

                                    intent.putExtra("passGrpName", group_name);
                                    intent.putExtra("passGrpCode", group_code);
                                    intent.putExtra("passAdmName", user_name);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(login_page.this, "Invalid member credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });//isUserValid
                    } //else if
                    else if (radioButtonSecreatary.isChecked()) {
                        //To check if Secreatary is valid or not
                        isSecretaryValid(group_code, user_name, password, new SecretaryValidationCallback()
                        {
                            @Override
                            public void onSecretaryValidationResult(boolean isValid) {
                                if (isValid)
                                {
                                    //valid admin
                                    Toast.makeText(login_page.this, "Secretary login successful", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(PREF_GROUP_NAME, group_name);
                                    editor.putString(PREF_GROUP_CODE, group_code);
                                    editor.putString(PREF_USER_NAME, user_name);
                                    editor.apply();
                                    Intent intent = new Intent(login_page.this,treasurer.class);

                                    intent.putExtra("passGrpName", group_name);
                                    intent.putExtra("passGrpCode", group_code);
                                    intent.putExtra("passAdmName", user_name);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(login_page.this, "Invalid Secretary credentials", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });//isSecreataryValid
                    }
                }//outer if

            }//onClick


            //Function to validate all inputs
            private boolean validateInputs(String group_code, String user_name, String password)
            {
                if(group_code.isEmpty())
                {
                    etGroupCode.setError("Please enter your group code");
                    return false;
                }
                if(user_name.isEmpty())
                {
                    etUserName.setError("Please enter your group code");
                    return false;
                }
                if(password.isEmpty())
                {
                    etPassword.setError("Please enter your group code");
                    return false;
                }
                return true;
            }//validateInputs

            private void isAdminValid(String group_code, String user_name, String password, AdminValidationCallback callback)
            {
                database.child(group_code).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        //If dataSnapshot exists
                        if(dataSnapshot.exists())
                        {
                            String databaseUserName = String.valueOf(dataSnapshot.child("admin_name").getValue());
                            String databasePassword = String.valueOf(dataSnapshot.child("admin_password").getValue());

                            if (databaseUserName.equals(user_name))
                            {
                                if (databasePassword.equals(password))
                                {
                                    //Username and password match
                                    callback.onAdminValidationResult(true);
                                }
                                else
                                {
                                    //password does not match
                                    Toast.makeText(login_page.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
                                    etPassword.setError("Please enter correct password");
                                    callback.onAdminValidationResult(false);
                                }
                            }
                            else
                            {
                                //Username does not match
                                Toast.makeText(login_page.this, "Your name doesn't match!", Toast.LENGTH_SHORT).show();
                                etUserName.setError("Please enter correct name");
                                callback.onAdminValidationResult(false);
                            }
                        }
                        //If dataSnapshot doesn't exist
                        else
                        {
                            Toast.makeText(login_page.this, "Group doesn't exist", Toast.LENGTH_SHORT).show();
                            callback.onAdminValidationResult(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_page.this, "Failed, error in DB", Toast.LENGTH_SHORT).show();
                        callback.onAdminValidationResult(false);
                    }
                });
            }//isAdminValid

            private void isUserValid(String group_code, String user_name, String password, UserValidationCallback callback)
            {
                database.child(group_code).child("Members").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        boolean isValidUserFound = false;
                        //Iterate through each unique memberID
                        for (DataSnapshot memberSnapShot: dataSnapshot.getChildren()){
                            String databaseMemberName = String.valueOf(memberSnapShot.child("memberName").getValue());
                            String databaseMemberPassword = String.valueOf(memberSnapShot.child("memberPassword").getValue());
                            //If username matches
                            if (databaseMemberName.equals(user_name))
                            {
                                isValidUserFound  = true;
                                //If password matches
                                if (databaseMemberPassword.equals(password))
                                {
                                    //Username and password match
                                    callback.onUserValidationResult(true);
                                    return;
                                }
                                else
                                {
                                    //password does not match
                                    Toast.makeText(login_page.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
                                    etPassword.setError("Please enter correct password");
                                }
                            }//inner if
                        }//for
                        if(!isValidUserFound )
                        {
                            //Username does dot exist
                            Toast.makeText(login_page.this, "Invalid member Name!", Toast.LENGTH_SHORT).show();
                            etUserName.setError("Please enter valid member name");
                        }
                        callback.onUserValidationResult(false);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_page.this, "Failed, error in DB", Toast.LENGTH_SHORT).show();
                        callback.onUserValidationResult(false);
                    }
                });
            }//isUserValid

            private void isSecretaryValid(String group_code, String user_name, String password, SecretaryValidationCallback callback)
            {
                database.child(group_code).child("Members").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        boolean isValidUserFound = false;
                        //Iterate through each unique memberID
                        for (DataSnapshot memberSnapShot: dataSnapshot.getChildren()){
                            String isSec = String.valueOf(memberSnapShot.child("isSecretary").getValue());
                            Boolean booleanValue = Boolean.valueOf(isSec);
                            if(booleanValue)
                            {
                                String databaseMemberName = String.valueOf(memberSnapShot.child("memberName").getValue());
                                String databaseMemberPassword = String.valueOf(memberSnapShot.child("memberPassword").getValue());
                                //If username matches
                                if (databaseMemberName.equals(user_name))
                                {
                                    isValidUserFound  = true;
                                    //If password matches
                                    if (databaseMemberPassword.equals(password))
                                    {
                                        //Username and password match
                                        callback.onSecretaryValidationResult(true);
                                        return;
                                    }
                                    else
                                    {
                                        //password does not match
                                        Toast.makeText(login_page.this, "Password doesn't match!", Toast.LENGTH_SHORT).show();
                                        etPassword.setError("Please enter correct password");
                                    }
                                }//inner if
                            }

                        }//for
                        if(!isValidUserFound )
                        {
                            //Username does dot exist
                            Toast.makeText(login_page.this, "Invalid Secretary Name!", Toast.LENGTH_SHORT).show();
                            etUserName.setError("Please enter valid Secretary name");
                        }
                        callback.onSecretaryValidationResult(false);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(login_page.this, "Failed, error in DB", Toast.LENGTH_SHORT).show();
                        callback.onSecretaryValidationResult(false);
                    }
                });
            }//isSecretaryValid

        });
    }//onCreate
    private void readDataCode(String group_code) {
        database.child(group_code).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                String group_name = String.valueOf(dataSnapshot.child("group_name").getValue());

                Log.d("loginActivity","Group Code" + group_name);

                tvGroupName.setText(group_name);

            }//on success
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(login_page.this, "Group name not found", Toast.LENGTH_SHORT).show();
            }
        });
    }// readDataCode
}