package com.appdev.abhishek360.instruox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import static com.appdev.abhishek360.instruox.LoginActivity.tosty;


public class EventGamingTabFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    private SharedPreferences sharedPreferences;
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    private StorageReference storageReference;


    public EventGamingTabFragment()
    {
        // Required empty public constructor
    }



    public static EventGamingTabFragment newInstance(String param1, String param2)
    {
        EventGamingTabFragment fragment = new EventGamingTabFragment();
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
        View v = inflater.inflate(R.layout.fragment_event_gaming_tab, container, false);

        storageReference=FirebaseStorage.getInstance().getReference();

        recyclerView= (RecyclerView)v.findViewById(R.id.gaming_event_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        sharedPreferences=this.getActivity().getSharedPreferences(LoginActivity.spKey,Context.MODE_PRIVATE);
        setupEventAdapter();


        //recyclerView.setAdapter(new EventAdapter());
        return v;
    }


    private void setupEventAdapter()
    {



        Query q = db.collection("/EVENTS_INSTRUO/GAMING_EVENTS/EVENTS");



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
                    Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.juspay_help_screen_enter);
                    viewToAnimate.startAnimation(animation);
                    lastPosition = position;
                }
                else if (position < lastPosition)
                {
                    Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);
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

                        eventDetailsIntent.putExtra(EventDetailsActivity.KEY_POSTER_REF,"/EVENTS_INSTRUO/GAMING_EVENTS/"+eventId+".jpg");



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
                            final SslConfigurationManager sslConfigurationManager = new SslConfigurationManager();

                            sslConfigurationManager.updateUserData(eventId, token,getContext());


                        }
                    });


                }

                try
                {

                    storageReference=firebaseStorage.getReference().child("/EVENTS_INSTRUO/GAMING_EVENTS/"+eventId+".jpg");
                    Glide.with(getActivity().getApplicationContext()).using(new FirebaseImageLoader()).load(storageReference)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.poster_url);
                }
                catch (Exception e)
                {
                    Log.d("Event Image:",""+e);
                    holder.poster_url.setImageResource(R.drawable.technical_poster_reduced);

                }



            }

        };


        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

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
