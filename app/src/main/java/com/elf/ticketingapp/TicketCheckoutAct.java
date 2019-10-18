package com.elf.ticketingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btnBuyTicket, btnPles, btnMines;
    TextView txtJumlahTicket, txtmyBalance, txtTotalTicket, namaWisata, lokasi, ketentuan;
    LinearLayout btnBack;
    ImageView imgNoticeUang;

    Integer valueJumlahTicket = 1;
    Integer myBalance = 0;
    Integer valueTotalTicket = 0;
    Integer valueHargaTicket= 75;

    Integer sisa_balance = 0;

    DatabaseReference reference,reference2, reference3, reference4;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = " ";
    String time_wisata = " ";

    //membuat nomor transaksi secara unique
    Integer nomor_transaksi = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        btnBuyTicket = findViewById(R.id.btn_buy_ticket);
        btnPles = findViewById(R.id.btn_ples);
        btnMines = findViewById(R.id.btn_mines);
        btnBack = findViewById(R.id.btn_back);

        txtJumlahTicket = findViewById(R.id.txt_jumlah_ticket);
        txtmyBalance = findViewById(R.id.txt_my_balance);
        txtTotalTicket = findViewById(R.id.txt_total_harga);

        namaWisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        ketentuan = findViewById(R.id.ketentuan);

        imgNoticeUang = findViewById(R.id.img_notice_uang);

        Bundle bundle = getIntent().getExtras();
        String jenis_tiket_baru = bundle.getString("jenis_tiket");

        //setting valeu baru untuk komponen
        txtJumlahTicket.setText(valueJumlahTicket.toString());

        txtTotalTicket.setText("US$ "+valueTotalTicket+"");


        btnMines.animate().alpha(0).setDuration(300).start();
        btnMines.setEnabled(false);
        imgNoticeUang.setVisibility(View.GONE);


        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myBalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                txtmyBalance.setText("US$ " +myBalance+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //menimpa data yang ada dengan data baru
                namaWisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());

                date_wisata = dataSnapshot.child("data_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                valueHargaTicket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());
                valueTotalTicket = valueHargaTicket * valueJumlahTicket;
                txtTotalTicket.setText("US$ "+valueTotalTicket+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //mengambil data user
        btnMines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueJumlahTicket = valueJumlahTicket - 1;
                txtJumlahTicket.setText(valueJumlahTicket.toString());
                if (valueJumlahTicket <=1){
                    btnMines.animate().alpha(0).setDuration(300).start();
                    btnMines.setEnabled(false);
                }
                valueTotalTicket = valueHargaTicket * valueJumlahTicket;
                txtTotalTicket.setText("US$ "+valueTotalTicket+"");
                if(valueTotalTicket < myBalance){
                    btnBuyTicket.animate().translationY(0).alpha(1).setDuration(350).start();
                    btnBuyTicket.setEnabled(true);
                    txtmyBalance.setTextColor(Color.parseColor("#203DD1"));
                    imgNoticeUang.setVisibility(View.GONE);

                }
            }
        });
        btnPles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueJumlahTicket = valueJumlahTicket + 1;
                txtJumlahTicket.setText(valueJumlahTicket.toString());
                if (valueJumlahTicket > 1) {
                    btnMines.animate().alpha(1).setDuration(300).start();
                    btnMines.setEnabled(true);
                }
                valueTotalTicket = valueHargaTicket * valueJumlahTicket;
                txtTotalTicket.setText("US$ "+valueTotalTicket+"");
                if(valueTotalTicket > myBalance){
                    btnBuyTicket.animate().translationY(250).alpha(0).setDuration(350).start();
                    btnBuyTicket.setEnabled(true);
                    txtmyBalance.setTextColor(Color.parseColor("#D1206B"));
                    imgNoticeUang.setVisibility(View.VISIBLE);
                }
            }
        });


        btnBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //menyimpan bukti bayar ke firebase
                reference3 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new).child(namaWisata.getText().toString()+ nomor_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("id_ticket").setValue(namaWisata.getText().toString() + nomor_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(namaWisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("jumlah_ticket").setValue(valueJumlahTicket.toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);
                        Intent gotoSuccess = new Intent(TicketCheckoutAct.this,SuccessBuyTicketAct.class);
                        startActivity(gotoSuccess);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //update sisa balance
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = myBalance - valueTotalTicket;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}
