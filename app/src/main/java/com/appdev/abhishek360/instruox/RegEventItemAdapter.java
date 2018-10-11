package com.appdev.abhishek360.instruox;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RegEventItemAdapter extends RecyclerView.Adapter<RegEventItemAdapter.RegEventsViewHolder>
{
    //private String[] member_pic_url;
    private ArrayList<String> regEventName;
    private ArrayList<String> regFee;
    private String[] paymentStaus;

    public void setRegEventName(ArrayList<String> regEventName)
    {
        this.regEventName = regEventName;
    }

    public void setRegFee(ArrayList<String> regFee)
    {
        this.regFee = regFee;
    }

    public String[] getPaymentStaus() {
        return paymentStaus;
    }

    public void setPaymentStaus(String[] paymentStaus) {
        this.paymentStaus = paymentStaus;
    }

    @NonNull
    @Override
   public RegEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());

        View view = inf.inflate(R.layout.reg_event_view_holder,parent,false);
        return new RegEventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegEventsViewHolder holder, int position)
    {
        holder.name_event.setText(regEventName.get(position));
        if(!regFee.isEmpty()) holder.pay_fee.setText("Pay:- Rs "+regFee.get(position));
        holder.pay_fee.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v)
            {

                
            }
        });






    }

    @Override
    public int getItemCount()
    {
        return regEventName.size();
    }


    public static class  RegEventsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name_event;
        Button pay_fee;

        public RegEventsViewHolder(View itemView)
        {
            super(itemView);
            name_event=(TextView) itemView.findViewById(R.id.myprofile_view_holder_name);
            pay_fee=(Button) itemView.findViewById(R.id.myprofile_view_holder_paynow);

            

            





        }
    }

}
