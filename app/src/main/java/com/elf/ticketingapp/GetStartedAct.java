package com.elf.ticketingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GetStartedAct extends AppCompatActivity {

    Button buttonSignIn;
    Button buttonNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSignIn = new Intent(GetStartedAct.this, SignInAct.class);
                startActivity(goSignIn);
                finish();
            }
        });

        buttonNewAccount = findViewById(R.id.buttonNewAccount);
        buttonNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goRegister = new Intent(GetStartedAct.this, RegisterOneAct.class);
                startActivity(goRegister);
                finish();
            }
        });

    }
}
