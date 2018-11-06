package com.appdev.abhishek360.instruox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;


public class ScheduleDayThreeFragment extends Fragment
{



    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;


    public ScheduleDayThreeFragment()
    {
        // Required empty public constructor
    }


    public static ScheduleDayThreeFragment newInstance(String param1, String param2)
    {
        ScheduleDayThreeFragment fragment = new ScheduleDayThreeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.fragment_schedule_day_three, container, false);

        recyclerView= (RecyclerView)v.findViewById(R.id.schedule_day3_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        setupScheduleAdapter();
        return v;
    }


    private void setupScheduleAdapter()
    {




        Query q = firebaseFirestore.collection("/INSTRUO_SCHEDULE/DAY_3/SCHEDULES").orderBy("timing");

        FirestoreRecyclerOptions<ScheduleAdapter> res = new FirestoreRecyclerOptions.Builder<ScheduleAdapter>()
                .setQuery(q, ScheduleAdapter.class).build();

        adapter = new FirestoreRecyclerAdapter<ScheduleAdapter, ScheduleAdapter.ScheduleViewHolder>(res)
        {
            int lastPosition =-1;

            @NonNull
            @Override
            public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                LayoutInflater inf = LayoutInflater.from(parent.getContext());

                View view = inf.inflate(R.layout.schedule_view_holder,parent,false);

                return new ScheduleAdapter.ScheduleViewHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e)
            {
                super.onError(e);
                Log.e("error", e.getMessage());
                Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();


            }
            private void setAnimation(View viewToAnimate, int position)
            {
                // If the bound view wasn't previously displayed on screen, it's animated

                if (position > lastPosition)
                {
                    int bais= position%2;
                    Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.card_slide_left);;
                    switch (bais)
                    {
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
            protected void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleViewHolder holder, int position, @NonNull ScheduleAdapter model)
            {
                    setAnimation(holder.scheduleCard,position);
                    holder.name_schedule_textview.setText(""+model.getEvent());
                    holder.venu_schedule.setText(""+model.getVenue());
                    holder.timing_chedule.setText(""+model.getTiming());


            }



        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
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
        }
        else
            {

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

        void onFragmentInteraction(Uri uri);
    }
}
