package com.appdev.abhishek360.instruo.EventTabFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.appdev.abhishek360.instruo.Adapters.EventAdapter;
import com.appdev.abhishek360.instruo.EventDetailsActivity;
import com.appdev.abhishek360.instruo.ViewHolders.EventViewHolder;
import com.appdev.abhishek360.instruo.LoginActivity;
import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.Services.ApiRequestManager;
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

import io.reactivex.disposables.CompositeDisposable;


public class NonGenericTabFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    //private ProgressBar progressBar;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();

    private SharedPreferences sharedPreferences;
    private CompositeDisposable compositeDisposable;

    public static NonGenericTabFragment newInstance(String param1, String param2) {
        NonGenericTabFragment fragment = new NonGenericTabFragment();
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

        View v =inflater.inflate(R.layout.fragment_event_non_generic_tab, container, false);

        compositeDisposable = new CompositeDisposable();
        sharedPreferences = this.getActivity().getSharedPreferences(LoginActivity.spKey,Context.MODE_PRIVATE);

        //progressBar=(ProgressBar)v.findViewById(R.id.nongeneric_event_progressbar);
        recyclerView = (RecyclerView)v.findViewById(R.id.nongeneric_event_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        setupEventAdapter();

        return v;
    }

    private void setupEventAdapter() {
        Query q = db.collection("/EVENTS_INSTRUO/NON_GENERIC_EVENTS/EVENTS");

        FirestoreRecyclerOptions<EventAdapter> res = new FirestoreRecyclerOptions.Builder<EventAdapter>()
                .setQuery(q, EventAdapter.class).build();

        adapter = new FirestoreRecyclerAdapter<EventAdapter, EventViewHolder>(res) {
            int lastPosition=-1;

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inf = LayoutInflater.from(parent.getContext());

                View view = inf.inflate(R.layout.events_view_holder,parent,false);

                return new EventViewHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("error", e.getMessage());
                Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();
            }

            private void setAnimation(View viewToAnimate, int position) {
                if (position > lastPosition) {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.juspay_help_screen_enter);
                    viewToAnimate.startAnimation(animation);
                    lastPosition = position;
                }
            }

            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int position, @NonNull final EventAdapter model) {
                //Toast.makeText(getContext(),"Event Model:"+model,Toast.LENGTH_LONG).show();
                setAnimation(holder.getCardView(),position);

                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                final String eventId=snapshot.getId();

                holder.getName_event().setText(""+model.getTITLE());
                holder.getVenue().setText("Venue: "+model.getVENUE());
                holder.getTiming().setText("Time: "+model.getTIME());
                holder.getCardView().setOnClickListener(v -> {
                    Intent eventDetailsIntent = new Intent(getActivity(), EventDetailsActivity.class);

                    eventDetailsIntent.putExtra(EventDetailsActivity.KEY_EVENT_ID,eventId);
                    eventDetailsIntent.putExtra(EventDetailsActivity.KEY_EVENT_CAT, "NON_GENERIC_EVENTS");
                    eventDetailsIntent.putExtra(EventDetailsActivity.KEY_POSTER_REF,"/EVENTS_INSTRUO/NON_GENERIC_EVENTS/"+eventId+".png");

                    startActivity(eventDetailsIntent);
                });

                Set<String> eventSet = sharedPreferences.getStringSet(LoginActivity.spEventsKey,null);
                final String sessionId = sharedPreferences.getString(LoginActivity.spSessionId, "void");

                //Toast.makeText(getContext(),""+eventId+"Set:"+eventSet,Toast.LENGTH_LONG).show();

                if(eventSet == null && sessionId.equals("void")) {
                    holder.getRegisterEvent().setText("Login!");
                    holder.getRegisterEvent().setOnClickListener(v -> {
                        Intent loginIntent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(loginIntent);
                    });
                }
                else if (eventSet != null && eventSet.contains(eventId)) {
                    holder.getRegisterEvent().setEnabled(false);
                    holder.getRegisterEvent().setText("Registered");
                }
                else {
                    holder.getRegisterEvent().setOnClickListener(v -> {
                        //Toast.makeText(getContext(),""+eventId,Toast.LENGTH_LONG).show();
                        final ApiRequestManager apiRequestManager = new ApiRequestManager(
                                getContext().getApplicationContext(),
                                compositeDisposable
                        );

                        apiRequestManager.registerEvent(eventId);
                    });
                }
                try {
                    storageReference=firebaseStorage.getReference().child("/EVENTS_INSTRUO/NON_GENERIC_EVENTS/"+eventId+".png");

                    Glide.with(getActivity().getApplicationContext()).using(new FirebaseImageLoader()).load(storageReference)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.getPoster_url());
                }
                catch (Exception e) {
                    Log.d("Event Image:",""+e);
                    holder.getPoster_url().setImageResource(R.drawable.technical_poster_reduced);
                }
            }

        };

        adapter.notifyDataSetChanged();
        //progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }
}
