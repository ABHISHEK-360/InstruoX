package com.appdev.abhishek360.instruo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Set;


public class EventAutomatonTabFragment extends Fragment
{


    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();


    public EventAutomatonTabFragment()
    {
        // Required empty public constructor
    }


    public static EventAutomatonTabFragment newInstance(String param1, String param2)
    {
        EventAutomatonTabFragment fragment = new EventAutomatonTabFragment();
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
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_event_automaton_tab, container, false);

        progressBar=(ProgressBar)v.findViewById(R.id.automaton_event_progressbar);
        sharedPreferences= this.getActivity().getSharedPreferences(LoginActivity.spKey,Context.MODE_PRIVATE);



        recyclerView= (RecyclerView)v.findViewById(R.id.robotics_event_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        setupEventAdapter();

        //recyclerView.setAdapter(new EventAdapter());
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setupEventAdapter()
    {




        Query q = db.collection("/EVENTS_INSTRUO/AUTOMATON_EVENTS/EVENTS");


        FirestoreRecyclerOptions<EventAdapter> res = new FirestoreRecyclerOptions.Builder<EventAdapter>()
                .setQuery(q, EventAdapter.class).build();



        adapter = new FirestoreRecyclerAdapter<EventAdapter, EventViewHolder>(res)
        {
            int lastPosition=-1;

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                LayoutInflater inf = LayoutInflater.from(parent.getContext());

                View view = inf.inflate(R.layout.events_view_holder,parent,false);

                return new EventViewHolder(view);
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
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.juspay_help_screen_enter);
                    viewToAnimate.startAnimation(animation);
                    lastPosition = position;
                }
            }

            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int position, @NonNull final EventAdapter model)
            {



                setAnimation(holder.cardView,position);

                holder.name_event.setText(""+model.getTITLE());
                holder.venue.setText("Venue: "+model.getVENUE());
                holder.timing.setText("Time: "+model.getTIME());


                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                final String eventId=snapshot.getId();


                holder.cardView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent eventDetailsIntent = new Intent(getActivity(),EventDetailsActivity.class);
                        eventDetailsIntent.putExtra("tabCode",0);
                        eventDetailsIntent.putExtra(EventDetailsActivity.KEY_EVENT_OBJECT,model);
                        eventDetailsIntent.putExtra(EventDetailsActivity.KEY_EVENT_ID,eventId);

                        eventDetailsIntent.putExtra(EventDetailsActivity.KEY_POSTER_REF,"/EVENTS_INSTRUO/AUTOMATON_EVENTS/"+eventId+".jpeg");

                        startActivity(eventDetailsIntent);

                    }
                });

                Set<String> eventSet = sharedPreferences.getStringSet(LoginActivity.spEventsKey,null);
                final String token = sharedPreferences.getString(LoginActivity.spAccessTokenKey, "void");


                if(eventSet==null&&token.equals("void"))
                {
                    holder.registerEvent.setText("Login!");
                    holder.registerEvent.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {

                            Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
                            startActivity(loginIntent);

                        }
                    });

                }
                else if (eventSet!=null&&eventSet.contains(eventId))
                {
                    holder.registerEvent.setEnabled(false);
                    holder.registerEvent.setText("Registered");
                }
                else {

                    holder.registerEvent.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {

                            //Toast.makeText(getContext(),""+eventId,Toast.LENGTH_LONG).show();
                            //readUserData(eventId, token);

                            final SslConfigurationManager sslConfigurationManager = new SslConfigurationManager();

                            sslConfigurationManager.updateUserData(eventId, token,getContext());


                        }
                    });

                }

                try
                {
                    storageReference=firebaseStorage.getReference().child("/EVENTS_INSTRUO/AUTOMATON_EVENTS/"+eventId+".jpeg");
                    Glide.with(getActivity().getApplicationContext()).using(new FirebaseImageLoader()).load(storageReference)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.poster_url);

                }
                catch (Exception e)
                {
                    Log.d("Event Image:",""+e);
                    holder.poster_url.setImageResource(R.drawable.technical_poster_reduced);

                }

                /*storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        try
                        {
                            Glide.with(getActivity().getApplicationContext()).load(uri.toString()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.poster_url);


                        }
                        catch (Exception e)
                        {
                            Log.d("Picture Load:",""+e);
                        }                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Log.d("Picture Load:",""+e);
                        //tosty(getActivity(),""+e);
                        holder.poster_url.setImageResource(R.drawable.technical_poster_reduced);
                    }
                });*/

            }

        };


        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);

    }



    @Override
    public void onStart()
    {
        super.onStart();

        adapter.startListening();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
