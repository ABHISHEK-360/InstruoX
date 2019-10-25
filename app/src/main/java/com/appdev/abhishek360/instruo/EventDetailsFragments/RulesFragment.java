package com.appdev.abhishek360.instruo.EventDetailsFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appdev.abhishek360.instruo.Adapters.EventAdapter;
import com.appdev.abhishek360.instruo.EventDetailsActivity;
import com.appdev.abhishek360.instruo.R;

import java.util.Map;


public class RulesFragment extends Fragment {
    private Map<String,String > eventRules_str;
    private TextView eventRound1_textview,eventRound2_textview,eventRound3_textview;
    private EventAdapter eventDetails;

    private OnFragmentInteractionListener mListener;

    public static RulesFragment newInstance(EventAdapter adapter) {
        RulesFragment fragment = new RulesFragment();
        Bundle args = new Bundle();
        args.putParcelable(EventDetailsActivity.KEY_EVENT_OBJECT,adapter);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventDetails=getArguments().getParcelable(EventDetailsActivity.KEY_EVENT_OBJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_event_rules, container, false);

        eventRound1_textview=v.findViewById(R.id.event_rules_round1);
        eventRound2_textview=v.findViewById(R.id.event_rules_round2);
        eventRound3_textview=v.findViewById(R.id.event_rules_round3);

        eventRules_str=eventDetails.getRULES();

        if(eventRules_str!=null) {
            eventRound1_textview.setText(eventRules_str.get("ROUND_1"));
            eventRound2_textview.setText(eventRules_str.get("ROUND_2"));
            eventRound3_textview.setText(eventRules_str.get("ROUND_3"));
        }

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
