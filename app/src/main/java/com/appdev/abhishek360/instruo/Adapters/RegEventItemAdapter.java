package com.appdev.abhishek360.instruo.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.Services.AlertService;
import com.appdev.abhishek360.instruo.UserProfileActivity;

import java.util.ArrayList;

public class RegEventItemAdapter extends RecyclerView.Adapter<RegEventItemAdapter.RegEventsViewHolder> {
    //private String[] member_pic_url;
    private ArrayList<String> regEventName;
    private ArrayList<String> regFee;
    //private ArrayList<String> accountDetails;
    private ArrayList<Integer> paymentStatus;
    private UserProfileActivity activity;

    public void setActivity(Activity activity) {
        this.activity = (UserProfileActivity) activity;
    }

    public void setRegEventName(ArrayList<String> regEventName) {
        //regEventName.add("Accomodation Fee");
        this.regEventName = regEventName;
    }

    public void setRegFee(ArrayList<String> regFee) {
        //regFee.add("200");
        this.regFee = regFee;
    }


    public void setPaymentStaus(ArrayList<Integer> paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @NonNull
    @Override
    public RegEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());

        View view = inf.inflate(R.layout.reg_event_view_holder, parent, false);
        return new RegEventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegEventsViewHolder holder, final int position) {
        holder.name_event.setText(regEventName.get(position));

        if (!regFee.get(position).isEmpty()) {
            if (paymentStatus.get(position) == 1) {
                holder.pay_fee.setText("Paid:- ₹ " + regFee.get(position));
                holder.pay_fee.setEnabled(false);

            } else {
                holder.pay_fee.setText("Pay:- ₹ " + regFee.get(position));
            }
        } else holder.pay_fee.setEnabled(false);

        holder.pay_fee.setOnClickListener(v -> {
            String regFee_str = regFee.get(position);
            AlertService alertService = new AlertService(activity);
            alertService.showAlert("Message", "Payment will be available soon.");
            //activity.payAmt(position, regFee_str);
        });
    }

    @Override
    public int getItemCount() {
        return regEventName.size();
    }

    public static class RegEventsViewHolder extends RecyclerView.ViewHolder {
        TextView name_event;
        Button pay_fee;

        public RegEventsViewHolder(View itemView) {
            super(itemView);
            name_event = (TextView) itemView.findViewById(R.id.myprofile_view_holder_name);
            pay_fee = (Button) itemView.findViewById(R.id.myprofile_view_holder_paynow);
        }
    }

}
