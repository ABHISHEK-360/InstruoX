package com.appdev.abhishek360.instruo.EventDetailsFragments;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appdev.abhishek360.instruo.Adapters.DocsAdapter;
import com.appdev.abhishek360.instruo.Adapters.EventAdapter;
import com.appdev.abhishek360.instruo.EventDetailsActivity;
import com.appdev.abhishek360.instruo.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.Context.DOWNLOAD_SERVICE;


public class DescriptionFragment extends Fragment {
    private String eventTime_str, eventVenue_str,
            eventDesc_str, eventPrize_str,
            eventFee_str,eventId,eventType;
    private TextView eventDesc_textview,
            eventPrize_textview,
            eventFee_textview;
    private EventAdapter eventDetails;
    private RecyclerView docsRecycler;
    private TextView default_text;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;

    public static DescriptionFragment newInstance(EventAdapter adapter, String eventId) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putParcelable(EventDetailsActivity.KEY_EVENT_OBJECT,adapter);
        args.putString("eventId",eventId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventDetails=getArguments().getParcelable(EventDetailsActivity.KEY_EVENT_OBJECT);
            eventId=getArguments().getString("eventId");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_event_description, container, false);
        eventDesc_textview=v.findViewById(R.id.event_desc_desc);
        eventPrize_textview=v.findViewById(R.id.event_desc_prize);
        eventFee_textview=v.findViewById(R.id.event_desc_reg_fee);
        default_text=(TextView)v.findViewById(R.id.desc_docs_default_text) ;

        docsRecycler=(RecyclerView)v.findViewById(R.id.desc_uploaded_docs_list);
        docsRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        setupDocsAdapter();

        eventDesc_str=eventDetails.getDESC();
        eventPrize_str=eventDetails.getPRIZE_MONEY();
        eventFee_str=eventDetails.getREG_FEE();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            eventDesc_textview.setText(Html.fromHtml(eventDesc_str,Html.FROM_HTML_MODE_COMPACT));
        }
        else {
            eventDesc_textview.setText(Html.fromHtml(eventDesc_str));
        }
        eventPrize_textview.setText(eventPrize_str);
        eventFee_textview.setText(eventFee_str);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void setupDocsAdapter() {
        Query q=firebaseFirestore.collection("/EVENTS_INSTRUO_DOCS/"+eventId+"/DOCS");

        FirestoreRecyclerOptions<DocsAdapter> res = new FirestoreRecyclerOptions.Builder<DocsAdapter>()
                .setQuery(q,DocsAdapter.class).build();

        adapter = new FirestoreRecyclerAdapter<DocsAdapter, DocsAdapter.DocsViewHolder>(res) {
            @NonNull
            @Override
            public DocsAdapter.DocsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inf = LayoutInflater.from(parent.getContext());

                View view = inf.inflate(R.layout.docs_view_holder,parent,false);

                return new DocsAdapter.DocsViewHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("error", e.getMessage());
                //Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();
            }

            public void onDataChanged() {
                if(getItemCount() == 0) {
                    default_text.setVisibility(View.VISIBLE);
                }
                else default_text.setVisibility(View.GONE);
            }

            @Override
            protected void onBindViewHolder(@NonNull final DocsAdapter.DocsViewHolder holder, int position, @NonNull final DocsAdapter model) {
                if (model.getDocsname()!=null) {
                    holder.getNameDoc().setText(""+model.getDocsname());
                    holder.getPurposeDoc().setText(""+model.getPurpose());
                }

                holder.getDownloadDoc().setOnClickListener(v -> {
                    storageReference = firebaseStorage.getReference().child("/EVENTS_DOCS/" + eventId + "/" + model.getDocsname());
                    Toast.makeText(getActivity(),"/EVENTS_DOCS/"+eventId+"/"+model.getDocsname(),Toast.LENGTH_LONG).show();

                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        if (isWriteStoragePermissionGranted()) {
                            downloadFile(uri,model.getDocsname());
                            Toast.makeText(getContext(),"Starting Download!",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(),"File Not Found!",Toast.LENGTH_LONG).show();
                        Log.d("File Not Found ",""+e);
                    });
                });
            }
        };

        adapter.notifyDataSetChanged();
        docsRecycler.setAdapter(adapter);
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
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
        else {
            Log.v("Permission_write:","Permission is granted");
            return true;
        }
    }

    private void downloadFile(Uri uri,String filename) {
        //Uri uri = Uri.parse(url);
        DownloadManager.Request r = new DownloadManager.Request(uri);

        // This put the download in the same Download dir the browser uses
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
        r.allowScanningByMediaScanner();

        // Notify user when download is completed
        // (Seems to be available since Honeycomb only)
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Start download
        Toast.makeText(getContext(),"Download Started",Toast.LENGTH_LONG);
        DownloadManager dm = (DownloadManager) this.getActivity().getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(r);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 3:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.getActivity(),"Tap Again to download",Toast.LENGTH_LONG).show();

                    Log.v("Write_permission","Permission: " + permissions[0] + "was " + grantResults[0]);
                    //resume tasks needing this permission
                }
                else {
                    Toast.makeText(this.getActivity(),"Grant Permission to Download.",Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
