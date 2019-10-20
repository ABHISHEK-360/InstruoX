package com.appdev.abhishek360.instruo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class EventCoordinatorFragment extends Fragment
{
    private TextView eventCoordinators;
    private EventAdapter eventDetails;


    private OnFragmentInteractionListener mListener;

    public EventCoordinatorFragment()
    {
        // Required empty public constructor
    }


    public static EventCoordinatorFragment newInstance(EventAdapter eventAdapter)
    {
        EventCoordinatorFragment fragment = new EventCoordinatorFragment();
        Bundle args = new Bundle();
        args.putParcelable(EventDetailsActivity.KEY_EVENT_OBJECT,eventAdapter);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
             eventDetails = getArguments().getParcelable(EventDetailsActivity.KEY_EVENT_OBJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v= inflater.inflate(R.layout.fragment_event_coordinator, container, false);

        eventCoordinators=v.findViewById(R.id.event_coordinator_co);
        eventCoordinators.setText(eventDetails.getCOORDINATORS());



        return v;
    }

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
