package com.appdev.abhishek360.instruox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class EventDescriptionFragment extends Fragment
{

    private String eventTime_str,eventVenue_str,eventDesc_str,eventPrize_str,eventFee_str;
    private TextView eventDesc_textview,eventPrize_textview,eventFee_textview;
    private EventAdapter eventDetails;


    private OnFragmentInteractionListener mListener;

    public EventDescriptionFragment()
    {
        // Required empty public constructor
    }


    public static EventDescriptionFragment newInstance(EventAdapter adapter)
    {
        EventDescriptionFragment fragment = new EventDescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(EventDetailsActivity.KEY_EVENT_OBJECT,adapter);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            eventDetails=getArguments().getParcelable(EventDetailsActivity.KEY_EVENT_OBJECT);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v= inflater.inflate(R.layout.fragment_event_description, container, false);
        eventDesc_textview=v.findViewById(R.id.event_desc_desc);
        eventPrize_textview=v.findViewById(R.id.event_desc_prize);
        eventFee_textview=v.findViewById(R.id.event_desc_reg_fee);

        eventDesc_str=eventDetails.getDESC();
        eventPrize_str=eventDetails.getPRIZE_MONEY();
        eventFee_str=eventDetails.getREG_FEE();

        eventDesc_textview.setText(eventDesc_str);
        eventPrize_textview.setText(eventPrize_str);
        eventFee_textview.setText(eventFee_str);


        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
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
