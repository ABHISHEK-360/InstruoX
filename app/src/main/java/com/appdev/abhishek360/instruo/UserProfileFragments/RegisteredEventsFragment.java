package com.appdev.abhishek360.instruo.UserProfileFragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.abhishek360.instruo.Adapters.RegEventItemAdapter;
import com.appdev.abhishek360.instruo.R;

import java.util.ArrayList;

public class RegisteredEventsFragment extends Fragment {
    private ArrayList<String> regEventsList;
    private ArrayList<String> entryFee;
    private ArrayList<Integer> paymentStatus;
    private ArrayList<String> transId;
    private ArrayList<String> paymentTime;
    //private ArrayList<String> accountDetails;
    private RecyclerView recyclerView;
    private RegEventItemAdapter adapter;

    public static RegisteredEventsFragment newInstance(ArrayList<String> events, ArrayList<String> eventEntryFee, ArrayList<Integer> paymentStatus, ArrayList<String> transId, ArrayList<String> paymentTime ) {
        RegisteredEventsFragment fragment = new RegisteredEventsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("events", events);
        args.putStringArrayList("entryFee", eventEntryFee);
        args.putIntegerArrayList("paymentStatus", paymentStatus);
        args.putStringArrayList("transId", transId);
        args.putStringArrayList("paymentTime", paymentTime);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            regEventsList = getArguments().getStringArrayList("events");
            entryFee = getArguments().getStringArrayList("entryFee");
            paymentStatus = getArguments().getIntegerArrayList("paymentStatus");
            transId = getArguments().getStringArrayList("transId");
            paymentTime = getArguments().getStringArrayList("paymentTime");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_registered_events, container, false);

        recyclerView = (RecyclerView)v.findViewById(R.id.myprofile_recycler_reg_event) ;
        adapter = new RegEventItemAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        adapter.setRegEventName(regEventsList);
        adapter.setRegFee(entryFee);
        adapter.setPaymentStatus(paymentStatus);
        adapter.setTransId(transId);
        adapter.setPaymentTime(paymentTime);

        //adapter.setAccountDetails(accountDetails);
        adapter.setActivity(this.getActivity());

        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
