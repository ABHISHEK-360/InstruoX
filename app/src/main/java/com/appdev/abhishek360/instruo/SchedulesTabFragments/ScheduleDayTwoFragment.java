package com.appdev.abhishek360.instruo.SchedulesTabFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.Adapters.ScheduleAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class ScheduleDayTwoFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private OnFragmentInteractionListener mListener;

    public static ScheduleDayTwoFragment newInstance(String param1, String param2){
        ScheduleDayTwoFragment fragment = new ScheduleDayTwoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_schedule_day_two, container, false);
        recyclerView= (RecyclerView)v.findViewById(R.id.schedule_day2_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        setupScheduleAdapter();
        return v;
    }


    private void setupScheduleAdapter() {
        Query q = firebaseFirestore.collection("/INSTRUO_SCHEDULE/DAY_2/SCHEDULES");

        FirestoreRecyclerOptions<ScheduleAdapter> res = new FirestoreRecyclerOptions.Builder<ScheduleAdapter>()
                .setQuery(q.orderBy("timing"), ScheduleAdapter.class).build();

        adapter = new FirestoreRecyclerAdapter<ScheduleAdapter, ScheduleAdapter.ScheduleViewHolder>(res) {
            int lastPosition=-1;
            @NonNull
            @Override
            public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inf = LayoutInflater.from(parent.getContext());

                View view = inf.inflate(R.layout.schedule_view_holder,parent,false);

                return new ScheduleAdapter.ScheduleViewHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("error", e.getMessage());
                Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();
            }

            private void setAnimation(View viewToAnimate, int position) {
                if (position > lastPosition) {
                    int bais= position%2;
                    Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.card_slide_left);;
                    switch (bais) {
                        case 0 :
                            animation = AnimationUtils.loadAnimation(getContext(),R.anim.card_slide_left);
                            break;

                        case 1 :
                            animation = AnimationUtils.loadAnimation(getContext(),R.anim.card_slide_left);
                            break;
                    }
                    viewToAnimate.startAnimation(animation);
                    lastPosition = position;
                }
            }

            @Override
            protected void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleViewHolder holder, int position, @NonNull ScheduleAdapter model) {
                    setAnimation(holder.getScheduleCard(),position);
                    holder.getNameScheduleTv().setText(""+model.getEvent());
                    holder.getVenueScheduleTv().setText(""+model.getVenue());
                    holder.getTimingScheduleTv().setText(""+model.getTiming());
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

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
        }
        else {
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
        void onFragmentInteraction(Uri uri);
    }
}
