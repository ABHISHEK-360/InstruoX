package com.appdev.abhishek360.instruo;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;


public class EventRulesFragment extends Fragment
{

    private Map<String,String > eventRules_str;
    private TextView eventRound1_textview,eventRound2_textview,eventRound3_textview;
    private EventAdapter eventDetails;



    private OnFragmentInteractionListener mListener;

    public EventRulesFragment() {
        // Required empty public constructor
    }


    public static EventRulesFragment newInstance(EventAdapter adapter)
    {
        EventRulesFragment fragment = new EventRulesFragment();
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
        View v=inflater.inflate(R.layout.fragment_event_rules, container, false);




        eventRound1_textview=v.findViewById(R.id.event_rules_round1);
        eventRound2_textview=v.findViewById(R.id.event_rules_round2);
        eventRound3_textview=v.findViewById(R.id.event_rules_round3);


        eventRules_str=eventDetails.getRULES();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            eventRound1_textview.setText(Html.fromHtml(eventRules_str.get("ROUND_1"),Html.FROM_HTML_MODE_COMPACT));
            eventRound2_textview.setText(Html.fromHtml(eventRules_str.get("ROUND_2"),Html.FROM_HTML_MODE_COMPACT));
            eventRound3_textview.setText(Html.fromHtml(eventRules_str.get("ROUND_3"),Html.FROM_HTML_MODE_COMPACT));

        }
        else
        {
            eventRound1_textview.setText(Html.fromHtml(eventRules_str.get("ROUND_1")));
            eventRound2_textview.setText(Html.fromHtml(eventRules_str.get("ROUND_2")));
            eventRound3_textview.setText(Html.fromHtml(eventRules_str.get("ROUND_3")));
        }*/

        if(eventRules_str!=null)
        {
            eventRound1_textview.setText(eventRules_str.get("ROUND_1"));
            eventRound2_textview.setText(eventRules_str.get("ROUND_2"));
            eventRound3_textview.setText(eventRules_str.get("ROUND_3"));
        }



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
        } else
            {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
