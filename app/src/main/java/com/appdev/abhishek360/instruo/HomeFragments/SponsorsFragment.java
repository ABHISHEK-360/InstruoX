package com.appdev.abhishek360.instruo.HomeFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.Adapters.SponsorsAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class SponsorsFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirestoreRecyclerAdapter adapter;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();

    public static SponsorsFragment newInstance(String param1, String param2) {
        SponsorsFragment fragment = new SponsorsFragment();
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

        View v =  inflater.inflate(R.layout.fragment_sponsors, container, false);
        recyclerView= (RecyclerView)v.findViewById(R.id.sponsors_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));
        setupSponsorsAdapter();

        return v;
    }

    private void setupSponsorsAdapter() {
        Query q = firebaseFirestore.collection("/INSTRUO_SPONSORS/").orderBy("key");

        FirestoreRecyclerOptions<SponsorsAdapter> res = new FirestoreRecyclerOptions.Builder<SponsorsAdapter>()
                .setQuery(q, SponsorsAdapter.class).build();

        adapter = new FirestoreRecyclerAdapter<SponsorsAdapter, SponsorsAdapter.SponsorViewHolder>(res) {
            @NonNull
            @Override
            public SponsorsAdapter.SponsorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inf = LayoutInflater.from(parent.getContext());

                View view = inf.inflate(R.layout.sponsors_view_holder,parent,false);

                return new SponsorsAdapter.SponsorViewHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("error", e.getMessage());
                Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onBindViewHolder(@NonNull final SponsorsAdapter.SponsorViewHolder holder, int position, @NonNull SponsorsAdapter model) {
                holder.category_sponsors.setText(""+model.getCat());

                try {
                    storageReference=firebaseStorage.getReference().child("/SPONSORS_LOGO/"+model.getLogo());
                    Glide.with(getActivity().getApplicationContext()).using(new FirebaseImageLoader()).load(storageReference)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.logo_sponsors);
                }
                catch (Exception e) {
                    Log.d("Event Image:",""+e);
                    holder.logo_sponsors.setImageResource(R.drawable.loading_icon);
                }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
