package com.elf.ticketingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessBuyTicketAct extends AppCompatActivity {

    Animation app_spalsh, btt, ttb;
    Button btn_view_ticket, btn_my_dashboard;
    TextView app_tittle, subtittle;
    ImageView icon_succes_ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);

        btn_view_ticket = findViewById(R.id.buttonViewTicket);
        btn_my_dashboard = findViewById(R.id.buttonDashboard);
        app_tittle = findViewById(R.id.app_tittle);
        subtittle = findViewById(R.id.app_subtittle);
        icon_succes_ticket = findViewById(R.id.icon_succes_ticket);

        btn_view_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoProfile = new Intent(SuccessBuyTicketAct.this,ProfileAct.class);
                startActivity(gotoProfile);
            }
        });

        btn_my_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoHome = new Intent(SuccessBuyTicketAct.this,HomeAct.class);
                startActivity(gotoHome);
            }
        });
    }
}
