package com.appdev.abhishek360.instruo.HomeFragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdev.abhishek360.instruo.Adapters.WorkshopListAdapter;
import com.appdev.abhishek360.instruo.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class WorkshopFragment extends Fragment {

    private RecyclerView recyclerView;
    private WorkshopListAdapter adapter;
    private StorageReference storageReference;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();


    public static WorkshopFragment newInstance() {
        WorkshopFragment fragment = new WorkshopFragment();
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
        View v = inflater.inflate(R.layout.fragment_event_workshop, container, false);

        recyclerView = v.findViewById(R.id.workshops_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        adapter = new WorkshopListAdapter(getActivity());

        ArrayList<StorageReference> references = new ArrayList<>();
        references.add(firebaseStorage.getReference().child("WORKSHOPS/poster_android.jpeg"));
        references.add(firebaseStorage.getReference().child("WORKSHOPS/poster_cad.jpeg"));

        adapter.setStorageReferences(references);
        recyclerView.setAdapter(adapter);
        return v;
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
