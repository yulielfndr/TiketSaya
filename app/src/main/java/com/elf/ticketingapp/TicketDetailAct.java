package com.elf.ticketingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetailAct extends AppCompatActivity {

    LinearLayout btnBack;
    Button buttonBuy;
    TextView tittle_ticket, location, photo_spot_ticket,
            wifi_available, festivals_ticket, short_description;
    ImageView header_ticket;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        buttonBuy = findViewById(R.id.buttonBuy);
        btnBack = findViewById(R.id.btnBack);
        tittle_ticket = findViewById(R.id.tittle_ticket);
        location = findViewById(R.id.location);
        photo_spot_ticket = findViewById(R.id.photo_spot_ticket);
        wifi_available = findViewById(R.id.wifi_available);
        festivals_ticket = findViewById(R.id.festivals_ticket);
        short_description = findViewById(R.id.short_description);
        header_ticket = findViewById(R.id.header_ticket);

        //mengambil data dari intent
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        //mengambil data dari firebase berdasarkan intent
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tittle_ticket.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                location.setText(dataSnapshot.child("lokasi").getValue().toString());
                photo_spot_ticket.setText(dataSnapshot.child("is_photo_spot").getValue().toString());
                wifi_available.setText(dataSnapshot.child("is_wifi").getValue().toString());
                festivals_ticket.setText(dataSnapshot.child("is_festival").getValue().toString());
                short_description.setText(dataSnapshot.child("short_desc").getValue().toString());

                Picasso.with(TicketDetailAct.this).load(dataSnapshot.child("url_thumbnail")
                        .getValue().toString()).centerCrop().fit().into(header_ticket);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoCheckOut = new Intent(TicketDetailAct.this,TicketCheckoutAct.class);
                gotoCheckOut.putExtra("jenis_tiket", jenis_tiket_baru);
                startActivity(gotoCheckOut);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
