package com.appdev.abhishek360.instruo.HomeFragments;

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
import android.widget.Toast;

import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.Adapters.TeamMemberAdapter;
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

public class TeamFragment extends Fragment {
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreRecyclerAdapter adapter;
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    private StorageReference storageReference;

    public static TeamFragment newInstance(String param1, String param2) {
        TeamFragment fragment = new TeamFragment();

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

        View v = inflater.inflate(R.layout.fragment_team, container, false);

        recyclerView= (RecyclerView)v.findViewById(R.id.team_member_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        setupTeamAdapter();

        return v;
    }

    private void setupTeamAdapter() {
        Query q = db.collection("/INSTRUO_TEAM");

        FirestoreRecyclerOptions<TeamMemberAdapter> res = new FirestoreRecyclerOptions
                .Builder<TeamMemberAdapter>()
                .setQuery(q, TeamMemberAdapter.class).build();

        adapter = new FirestoreRecyclerAdapter<TeamMemberAdapter, TeamMemberAdapter.TeamMemberViewHolder>(res) {
            int lastPosition=-1;

            @NonNull
            @Override
            public TeamMemberAdapter.TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inf = LayoutInflater.from(parent.getContext());

                View view = inf.inflate(R.layout.team_view_holder,parent,false);

                return new TeamMemberAdapter.TeamMemberViewHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                super.onError(e);
                Log.e("error", e.getMessage());
                Toast.makeText(getContext(),""+e,Toast.LENGTH_LONG).show();
            }

            @Override
            protected void onBindViewHolder(@NonNull final TeamMemberAdapter.TeamMemberViewHolder holder, int position, @NonNull final TeamMemberAdapter model) {
                holder.name_tv.setText(""+model.getName());
                holder.email_tv.setText(""+model.getEmail());
                holder.phone_tv.setText(""+model.getPhone());
                holder.post_tv.setText(""+model.getPost());

                DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
                final String memberId = snapshot.getId();

                try {
                    storageReference=firebaseStorage.getReference().child("/INSTRUO_TEAM_PICS/"+memberId+".jpg" );
                    Glide.with(getActivity().getApplicationContext()).using(new FirebaseImageLoader()).load(storageReference)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.member_pic_url);

                }
                catch (Exception e) {
                    Log.d("Event Image:",""+e);
                    holder.member_pic_url.setImageResource(R.drawable.profilepic);
                }
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
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
}
