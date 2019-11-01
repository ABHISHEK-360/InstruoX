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


public class CoordinatorFragment extends Fragment {
    private TextView eventCoordinators;
    private EventAdapter eventDetails;

    public static CoordinatorFragment newInstance(EventAdapter eventAdapter) {
        CoordinatorFragment fragment = new CoordinatorFragment();
        Bundle args = new Bundle();
        args.putParcelable(EventDetailsActivity.KEY_EVENT_OBJECT,eventAdapter);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
             eventDetails = getArguments().getParcelable(EventDetailsActivity.KEY_EVENT_OBJECT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_event_coordinator, container, false);

        eventCoordinators = v.findViewById(R.id.event_coordinator_co);
        eventCoordinators.setText(eventDetails.getCOORDINATORS());

        return v;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
    }
}
