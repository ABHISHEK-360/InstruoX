package com.appdev.abhishek360.instruox;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class PersonalDetailsFragment extends Fragment

{
    private ArrayList<String> personalDetails;
    private EditText fullName,emailId,contactNo;
    private Button editDetails;




    private OnFragmentInteractionListener mListener;

    public PersonalDetailsFragment()
    {
        // Required empty public constructor
    }


    public static PersonalDetailsFragment newInstance(ArrayList<String> accountDetails)
    {
        PersonalDetailsFragment fragment = new PersonalDetailsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("accountDetails",accountDetails);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            personalDetails = getArguments().getStringArrayList("accountDetails");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v=inflater.inflate(R.layout.fragment_personal_details, container, false);


        fullName=(EditText) v.findViewById(R.id.personal_details_edit_name);
        emailId=(EditText) v.findViewById(R.id.personal_details_edit_email);
        contactNo=(EditText) v.findViewById(R.id.personal_details_edit_contact);
        editDetails=(Button)v.findViewById(R.id.myprofile_button_edit_details);

        fullName.setText(personalDetails.get(0));
        emailId.setText(personalDetails.get(1));
        contactNo.setText(personalDetails.get(2));

        editDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                fullName.setEnabled(true);
                emailId.setEnabled(true);
                contactNo.setEnabled(true);
            }
        });






        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
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


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
