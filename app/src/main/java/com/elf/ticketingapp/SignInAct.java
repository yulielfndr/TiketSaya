package com.elf.ticketingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInAct extends AppCompatActivity {

    TextView txtRegister;
    Button buttonSignIn;
    EditText xusername,xpassword;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtRegister = findViewById(R.id.txtRegister);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);


        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goRegister = new Intent(SignInAct.this, RegisterOneAct.class);
                startActivity(goRegister);
                finish();
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                buttonSignIn.setEnabled(false);
                buttonSignIn.setText("Loading...");

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if(username.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please fill out this field", Toast.LENGTH_SHORT).show();
                    xusername.requestFocus();
                    xusername.setFocusable(true);
                    buttonSignIn.setEnabled(true);
                    buttonSignIn.setText("SIGN IN");
                }
                else{
                    if(password.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please fill out this field", Toast.LENGTH_SHORT).show();
                        xpassword.requestFocus();
                        xpassword.setFocusable(true);
                        buttonSignIn.setEnabled(true);
                        buttonSignIn.setText("SIGN IN");
                    }
                    else{
                        reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(username);
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    String passwordFirebase = dataSnapshot.child("password").getValue().toString();

                                    if(password.equals(passwordFirebase)){
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, xusername.getText().toString());
                                        editor.apply();

                                        Intent gohome = new Intent(SignInAct.this, HomeAct.class);
                                        startActivity(gohome);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Password Salah", Toast.LENGTH_SHORT).show();
                                        buttonSignIn.setEnabled(true);
                                        buttonSignIn.setText("SIGN IN");
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Username Tidak Ada !", Toast.LENGTH_SHORT).show();
                                    buttonSignIn.setEnabled(true);
                                    buttonSignIn.setText("SIGN IN");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "DATABASE ERROR", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

    }
}
