package com.appdev.abhishek360.instruox;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
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


public class EventNonGenericTabFragment extends Fragment
{

    private OnFragmentInteractionListener mListener;


    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    private ProgressBar progressBar;

    private SharedPreferences sharedPreferences;

    public EventNonGenericTabFragment()
    {
        // Required empty public constructor
    }


    public static EventNonGenericTabFragment newInstance(String param1, String param2)
    {
        EventNonGenericTabFragment fragment = new EventNonGenericTabFragment();
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
        View v =inflater.inflate(R.layout.fragment_event_non_generic_tab, container, false);

        sharedPreferences=this.getActivity().getSharedPreferences(LoginActivity.spKey,Context.MODE_PRIVATE);

        progressBar=(ProgressBar)v.findViewById(R.id.nongeneric_event_progressbar);
        recyclerView= (RecyclerView)v.findViewById(R.id.nongeneric_event_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        setupEventAdapter();

        return v;
    }



    private void setupEventAdapter()
    {




        Query q = db.collection("/EVENTS_INSTRUO/NON_GENERIC_EVENTS/EVENTS");



        FirestoreRecyclerOptions<EventAdapter> res = new FirestoreRecyclerOptions.Builder<EventAdapter>()
                .setQuery(q, EventAdapter.class).build();



        adapter = new FirestoreRecyclerAdapter<EventAdapter, EventViewHolder>(res)
        {


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

            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int position, @NonNull EventAdapter model)
            {

                //Toast.makeText(getContext(),"Event Model:"+model,Toast.LENGTH_LONG).show();

                holder.name_event.setText(""+model.getTITLE());
                holder.venue.setText("Venue: "+model.getVENUE());
                holder.timing.setText("Time: "+model.getTIME());

                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                final String eventId=snapshot.getId();




                Set<String> eventSet = sharedPreferences.getStringSet(LoginActivity.spEventsKey,null);

                //Toast.makeText(getContext(),""+eventId+"Set:"+eventSet,Toast.LENGTH_LONG).show();

                if(eventSet==null)
                {
                    holder.registerEvent.setEnabled(false);

                }
                else if (eventSet.contains(eventId))
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
                            String token = sharedPreferences.getString(LoginActivity.spAccessTokenKey, "void");
                            final SslConfigurationManager sslConfigurationManager = new SslConfigurationManager();

                            sslConfigurationManager.readUserData(eventId, token,getContext());


                        }
                    });

                }


            }

        };




        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);

    }



    /*public boolean readUserData(final String eventId,final String token)
    {


        final SslConfigurationManager sslConfigurationManager = new SslConfigurationManager();

        HurlStack hurlStack = new HurlStack()
        {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException
            {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                try
                {
                    httpsURLConnection.setSSLSocketFactory(sslConfigurationManager.getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(sslConfigurationManager.getHostnameVerifier());
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity(),hurlStack);






        String URL = "https://instruo.in/api/v1/user";

        jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

        jsonRequestAdapter.setRequestAction("UPDATE");
        jsonRequestAdapter.setRequestData("eventIdAdd",eventId);
        jsonRequestAdapter.setRequestParameteres("filter",null);



        final Gson json = new GsonBuilder().serializeNulls().create();



        final String jsonRequest = json.toJson(jsonRequestAdapter);





        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonRequest,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if (response.get("responseStatus").equals("FAILED"))
                            {
                                tosty(getActivity(),"Try Again! Failed To Register! ");
                                //progresBar.setVisibility(View.GONE);



                            }
                            if (response.get("responseStatus").equals("OK"))
                            {

                                tosty(getActivity(),"Registered Successfully ! Please Check Registered Event Page for Payment status. ");

                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        //Log.d("Response",""+response);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d("Error:",""+error);
                        //readUserData(token);

                        tosty(getActivity(),"Trying Again: Network Error!");


                    }
                }


        )
        {
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("authorization", token);


                return params;
            }
        };

        requestQueue.add(objectRequest);

        return true;

    }*/




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

        void onFragmentInteraction(Uri uri);
    }
}
