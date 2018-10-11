package com.appdev.abhishek360.instruox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class RegisteredEventsFragment extends Fragment
{




    private OnFragmentInteractionListener mListener;
    private TextView textView;
    private ArrayList<String> regEventsList;
    private ArrayList<String> entryFee;

    private RecyclerView recyclerView;
    private RegEventItemAdapter adapter;

    public RegisteredEventsFragment()
    {
        // Required empty public constructor
    }


    public static RegisteredEventsFragment newInstance(ArrayList<String> events,ArrayList<String> eventEntryFee)
    {
        RegisteredEventsFragment fragment = new RegisteredEventsFragment();
        Bundle args = new Bundle();

        args.putStringArrayList("events",events);
        args.putStringArrayList("entryFee",eventEntryFee);


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            regEventsList = getArguments().getStringArrayList("events");
            entryFee= getArguments().getStringArrayList("entryFee");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v=inflater.inflate(R.layout.fragment_registered_events, container, false);

        recyclerView=(RecyclerView)v.findViewById(R.id.myprofile_recycler_reg_event) ;
        adapter=new RegEventItemAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        //String[] str=(String[]) regEventsList.to();

        adapter.setRegEventName(regEventsList);
        adapter.setRegFee(entryFee);

        recyclerView.setAdapter(adapter);


        //textView.setText(""+regEventsList);



        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }




    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
