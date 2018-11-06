package com.appdev.abhishek360.instruox;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.Context.DOWNLOAD_SERVICE;


public class EventResultFragment extends Fragment
{
    private RecyclerView docsRecycler;
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    private StorageReference storageReference=firebaseStorage.getReference();
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    private TextView default_text;

    private String eventId;


    private OnFragmentInteractionListener mListener;

    public EventResultFragment()
    {

    }

    public static EventResultFragment newInstance( String eventId)
    {
        EventResultFragment fragment = new EventResultFragment();
        Bundle args = new Bundle();
        args.putString("eventId",eventId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            eventId=getArguments().getString("eventId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_event_result, container, false);

        default_text=(TextView)v.findViewById(R.id.results_default_text);

        docsRecycler=(RecyclerView)v.findViewById(R.id.result_docs_recycler);
        docsRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        setupResultsAdapter();

        return v;
    }

    private void setupResultsAdapter()
    {
        Query q=firebaseFirestore.collection("/EVENTS_INSTRUO_DOCS/"+eventId+"/RESULT_DOCS");

        //Toast.makeText(getActivity(),"/EVENTS_INSTRUO_DOCS/"+eventId+"/RESULT_DOCS",Toast.LENGTH_LONG).show();


        FirestoreRecyclerOptions<DocsAdapter> res = new FirestoreRecyclerOptions.Builder<DocsAdapter>()
                .setQuery(q,DocsAdapter.class).build();



        adapter = new FirestoreRecyclerAdapter<DocsAdapter, DocsAdapter.DocsViewHolder>(res)
        {

            @NonNull
            @Override
            public DocsAdapter.DocsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                LayoutInflater inf = LayoutInflater.from(parent.getContext());

                View view = inf.inflate(R.layout.docs_view_holder,parent,false);

                return new DocsAdapter.DocsViewHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e)
            {
                super.onError(e);
                Log.e("error", e.getMessage());
                //Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();


            }

            public void onDataChanged()
            {


                if(getItemCount() == 0)
                {

                    default_text.setVisibility(View.VISIBLE);

                }
                else default_text.setVisibility(View.GONE);


            }


            @Override
            protected void onBindViewHolder(@NonNull final DocsAdapter.DocsViewHolder holder, int position, @NonNull final DocsAdapter model)
            {
                if(getItemCount()==0) default_text.setVisibility(View.VISIBLE);

                if (model.getDocsname()!=null)
                {
                    holder.name_docs.setText(""+model.getDocsname());
                    holder.purpose_docs.setText(""+model.getPurpose());
                }


                holder.download_docs.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        storageReference=firebaseStorage.getReference().child("/EVENTS_RESULT/"+eventId+"/"+model.getDocsname());
                        Toast.makeText(getActivity(),"/EVENTS_RESULT/"+eventId+"/"+model.getDocsname(),Toast.LENGTH_LONG).show();

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri uri)
                            {
                                if (isWriteStoragePermissionGranted())
                                {
                                    downloadFile(uri,model.getDocsname());
                                    Toast.makeText(getContext(),"Starting Download!",Toast.LENGTH_LONG).show();

                                }


                            }
                        })
                        .addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(getContext(),"File Not Found!",Toast.LENGTH_LONG).show();
                                Log.d("File Not Found ",""+e);
                            }
                        });
                    }
                });


            }

        };

        adapter.notifyDataSetChanged();
        docsRecycler.setAdapter(adapter);



    }

    public  boolean isWriteStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23)
        {
            if (ContextCompat.checkSelfPermission(this.getActivity(),android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("Permission_write:","Permission is granted");
                return true;
            } else {

                Log.v("Permission_write:","Permission is revoked");
                ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission_write:","Permission is granted");
            return true;
        }
    }

    private void downloadFile(Uri uri,String filename)
    {


        //Uri uri = Uri.parse(url);
        DownloadManager.Request r = new DownloadManager.Request(uri);


        // This put the download in the same Download dir the browser uses
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        r.allowScanningByMediaScanner();

        // Notify user when download is completed
        // (Seems to be available since Honeycomb only)

        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Start download

        Toast.makeText(getContext(),"Downloading.....",Toast.LENGTH_LONG).show();



        DownloadManager dm = (DownloadManager) this.getActivity().getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(r);




    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {

            case 3:

                if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this.getActivity(),"Tap Again to download",Toast.LENGTH_LONG).show();

                    Log.v("Write_permission","Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission

                }
                else
                {
                    Toast.makeText(this.getActivity(),"Grant Permission to Download.",Toast.LENGTH_LONG).show();


                }
                break;
        }

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
