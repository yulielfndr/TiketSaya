package com.elf.ticketingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeAct extends AppCompatActivity {

    LinearLayout btnPisa, btnPagoda, btnTorri,
            btnCandi, btnSphinx, btnMonas;
    CircleView btn_to_profile;
    ImageView photo_home_user;
    TextView nama_lengkap, bio, user_balance;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        btnPisa = findViewById(R.id.btnPisa);
        btnTorri = findViewById(R.id.btnTorri);
        btnPagoda = findViewById(R.id.btnPagoda);
        btnCandi = findViewById(R.id.btnCandi);
        btnSphinx = findViewById(R.id.btnSphinx);
        btnMonas = findViewById(R.id.btnMonas);
        btn_to_profile = findViewById(R.id.btn_to_profile);
        photo_home_user = findViewById(R.id.photo_home_user);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        user_balance = findViewById(R.id.user_balance);

        reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(username_key_new);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("full_name").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                user_balance.setText("US$ " + dataSnapshot.child("user_balance").getValue().toString());
                Picasso.with(HomeAct.this).load(dataSnapshot.child("url_photo_profile")
                        .getValue().toString()).centerCrop().fit().into(photo_home_user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(HomeAct.this, ProfileAct.class);
                startActivity(gotoprofile);
                finish();
            }

    });
        btnPisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goPisaTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                goPisaTicket.putExtra("jenis_tiket", "Pisa");
                startActivity(goPisaTicket);
            }
        });
        btnTorri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goTorriTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                goTorriTicket.putExtra("jenis_tiket", "Torri");
                startActivity(goTorriTicket);
            }
        });
        btnPagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goPagodaTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                goPagodaTicket.putExtra("jenis_tiket", "Pagoda");
                startActivity(goPagodaTicket);
            }
        });
        btnCandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCandiTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                goCandiTicket.putExtra("jenis_tiket", "Candi");
                startActivity(goCandiTicket);
            }
        });
        btnSphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goSphinxTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                goSphinxTicket.putExtra("jenis_tiket", "Sphinx");
                startActivity(goSphinxTicket);
            }
        });
        btnMonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goMonasTicket = new Intent(HomeAct.this, TicketDetailAct.class);
                goMonasTicket.putExtra("jenis_tiket", "Monas");
                startActivity(goMonasTicket);
            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
