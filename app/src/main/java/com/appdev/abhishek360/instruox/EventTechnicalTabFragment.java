package com.appdev.abhishek360.instruox;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventTechnicalTabFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventTechnicalTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventTechnicalTabFragment extends Fragment
{


    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    private SharedPreferences sharedPreferences;

    public EventTechnicalTabFragment()
    {
        // Required empty public constructor
    }


    public static EventTechnicalTabFragment newInstance(String param1, String param2)
    {
        EventTechnicalTabFragment fragment = new EventTechnicalTabFragment();
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
        View v =inflater.inflate(R.layout.fragment_event_technical_tab, container, false);

        sharedPreferences=this.getActivity().getSharedPreferences(LoginActivity.spKey,Context.MODE_PRIVATE);

        recyclerView= (RecyclerView)v.findViewById(R.id.technical_event_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), LinearLayoutManager.VERTICAL));

        setupEventAdapter();



        return v;
    }

    private void setupEventAdapter()
    {



        Query q = db.collection("/EVENTS_INSTRUO/TECHNICAL_EVENTS/EVENTS");

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



                holder.name_event.setText(""+model.getTITLE());
                holder.venue.setText("Venue: "+model.getVENUE());
                holder.timing.setText("Time: "+model.getTIME());


                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                final String eventId=snapshot.getId();

                Set<String> eventSet = sharedPreferences.getStringSet(LoginActivity.spEventsKey,null);

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
                            final String token = sharedPreferences.getString(LoginActivity.spAccessTokenKey, "void");
                            readUserData(eventId, token);


                        }
                    });

                }

                /*storageReference = firebaseStorage.getReference().child("/users/PropertyPic/" + hid + "/" + pid + "/"+model.getRoomId()+"/0.jpg");

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                        Glide.with(getActivity()).load(uri.toString()).into(roomHolder.img);
                    }
                }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        roomHolder.img.setImageResource(R.drawable.camera);
                    }
                });*/

            }

        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }



    public boolean readUserData(final String eventId,final String token)
    {

        boolean status=false;

        HurlStack hurlStack = new HurlStack()
        {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException
            {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                try
                {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
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

    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers)
    {

        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]
                {
                        new X509TrustManager()
                        {
                            public X509Certificate[] getAcceptedIssuers()
                            {
                                return originalTrustManager.getAcceptedIssuers();
                            }

                            public void checkClientTrusted(X509Certificate[] certs, String authType)
                            {
                                try
                                {
                                    if (certs != null && certs.length > 0)
                                    {
                                        certs[0].checkValidity();
                                    } else
                                    {
                                        originalTrustManager.checkClientTrusted(certs, authType);
                                    }
                                }
                                catch (CertificateException e)
                                {
                                    Log.w("checkClientTrusted", e.toString());
                                }
                            }

                            public void checkServerTrusted(X509Certificate[] certs, String authType)
                            {
                                try
                                {
                                    if (certs != null && certs.length > 0)
                                    {
                                        certs[0].checkValidity();
                                    } else
                                    {
                                        originalTrustManager.checkServerTrusted(certs, authType);
                                    }
                                } catch (CertificateException e)
                                {
                                    Log.w("checkServerTrusted", e.toString());
                                }
                            }
                        }
                };
    }



    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException
    {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = getResources().openRawResource(R.raw.certificate); // this cert file stored in \app\src\main\res\raw folder path

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();

    }


    private HostnameVerifier getHostnameVerifier()
    {
        return new HostnameVerifier()
        {
            @Override
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
                // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
                //HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                //return hv.verify("localhost", session);
            }
        };
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
