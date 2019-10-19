package com.appdev.abhishek360.instruox;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class RegEventItemAdapter extends RecyclerView.Adapter<RegEventItemAdapter.RegEventsViewHolder>
{
    //private String[] member_pic_url;
    private ArrayList<String> regEventName;
    private ArrayList<String> regFee;
    //private ArrayList<String> accountDetails;
    private Set<String> paymentStaus;
    private MyProfileActivity activity;

    public void setActivity(Activity activity)
    {
        this.activity = (MyProfileActivity) activity;
    }

    public void setRegEventName(ArrayList<String> regEventName)
    {
        //regEventName.add("Accomodation Fee");
        this.regEventName = regEventName;

    }

    public void setRegFee(ArrayList<String> regFee)
    {
        //regFee.add("200");
        this.regFee = regFee;
    }



    public Set<String> getPaymentStaus()
    {
        return paymentStaus;
    }

    public void setPaymentStaus(Set<String> paymentStaus)
    {
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
    public void onBindViewHolder(@NonNull RegEventsViewHolder holder, final int position)
    {
        holder.name_event.setText(regEventName.get(position));
        ArrayList<String> eventId=activity.getEventId();

        if(!regFee.get(position).isEmpty())
        {
            if(paymentStaus.contains(eventId.get(position)))
            {
                holder.pay_fee.setText("Paid:- ₹ "+regFee.get(position));
                holder.pay_fee.setEnabled(false);

            }
            else
            {
                holder.pay_fee.setText("Pay:- ₹ "+regFee.get(position));
            }
        }
        else holder.pay_fee.setEnabled(false);

        holder.pay_fee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String regFee_str = regFee.get(position);
                //if(regFee_str.isEmpty())

                //regFee_str="15";

                activity.payAmt(position,regFee_str);
                
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
