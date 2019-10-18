package com.elf.ticketingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter <TicketAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyTicket> myTickets;

    public TicketAdapter(Context c, ArrayList<MyTicket> p){
        context = c;
        myTickets = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_myticket,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.xnama_wisata.setText(myTickets.get(i).getNama_wisata());
        myViewHolder.xlokasi.setText(myTickets.get(i).getLokasi());
        myViewHolder.xjumlah_ticket.setText(myTickets.get(i).getJumlah_ticket() + " Tickets");

        final String getNama_wisata = myTickets.get(i).getNama_wisata();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyticketdetails = new Intent(context, MyTicketDetailAct.class);
                gotomyticketdetails.putExtra("nama_wisata", getNama_wisata);
                context.startActivity(gotomyticketdetails);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myTickets.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama_wisata, xlokasi, xjumlah_ticket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            xnama_wisata = itemView.findViewById(R.id.xnama_wisata);
            xjumlah_ticket = itemView.findViewById(R.id.xjumlah_ticket);
            xlokasi = itemView.findViewById(R.id.xlokasi);
        }
    }
}
