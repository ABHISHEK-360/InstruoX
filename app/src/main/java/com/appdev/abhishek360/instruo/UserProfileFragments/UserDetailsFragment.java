package com.appdev.abhishek360.instruo.UserProfileFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.appdev.abhishek360.instruo.LoginActivity;
import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.SslConfigurationManager;
import com.appdev.abhishek360.instruo.jsonRequestAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.glxn.qrgen.android.QRCode;

import java.util.ArrayList;

public class UserDetailsFragment extends Fragment {
    private ArrayList<String> personalDetails;
    private EditText fullName,emailId,contactNo,college;
    private EditText oldPass,newPass,conNewPass;
    private Button editDetails;
    private Button updatedetails,changePass;
    private ImageView qrCode_imageview;
    private SslConfigurationManager sslConfigurationManager;
    private SharedPreferences sharedPreferences;
    private String tokenId;
    private Dialog myDialog;

    private OnFragmentInteractionListener mListener;

    public static UserDetailsFragment newInstance(ArrayList<String> accountDetails) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("accountDetails",accountDetails);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            personalDetails = getArguments().getStringArrayList("accountDetails");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_personal_details, container, false);

        fullName=(EditText) v.findViewById(R.id.personal_details_edit_name);
        emailId=(EditText) v.findViewById(R.id.personal_details_edit_email);
        contactNo=(EditText) v.findViewById(R.id.personal_details_edit_contact);
        editDetails=(Button)v.findViewById(R.id.myprofile_button_edit_details);
        updatedetails=v.findViewById(R.id.myprofile_button_save_changes);
        changePass=v.findViewById(R.id.myprofile_button_change_pass);
        oldPass=v.findViewById(R.id.personal_details_edit_oldpass);
        newPass=v.findViewById(R.id.personal_details_edit_newpass);
        conNewPass=v.findViewById(R.id.personal_details_edit_confnewpass);
        college=v.findViewById(R.id.personal_details_edit_college);
        qrCode_imageview=v.findViewById(R.id.personal_details_qr_code);
        myDialog= new Dialog(this.getContext());
        sslConfigurationManager=new SslConfigurationManager();

        sharedPreferences=this.getActivity().getSharedPreferences(LoginActivity.spKey,Context.MODE_PRIVATE);
        tokenId=sharedPreferences.getString(LoginActivity.spAccessTokenKey,"void");

        fullName.setText(personalDetails.get(0));
        emailId.setText(personalDetails.get(1));
        college.setText(personalDetails.get(2));
        contactNo.setText(personalDetails.get(3));

        final Bitmap myBitmap = QRCode.from("instruoX:"+personalDetails.get(1)).withSize(720,720).bitmap();
        qrCode_imageview.setImageBitmap(myBitmap);

        editDetails.setOnClickListener(v1 -> {
            fullName.setEnabled(true);
           // emailId.setEnabled(true);
            contactNo.setEnabled(true);
        });

        updatedetails.setOnClickListener(v12 -> {
            jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

            jsonRequestAdapter.setRequestAction("UPDATE");
            jsonRequestAdapter.setRequestData("userName",fullName.getText().toString());
            jsonRequestAdapter.setRequestData("contact",contactNo.getText().toString());

            jsonRequestAdapter.setRequestParameteres("filter","id");
            sslConfigurationManager.updateUserData(jsonRequestAdapter,tokenId,getActivity());

        });

        changePass.setOnClickListener(v13 -> {

            String pass_str = newPass.getText().toString();
            String conPass_str=conNewPass.getText().toString();
            if(pass_str.isEmpty())
                Toast.makeText(getActivity(),"Enter new Password!",Toast.LENGTH_LONG).show();
            else if(conPass_str.isEmpty())
                Toast.makeText(getActivity(),"Confirm new Password!",Toast.LENGTH_LONG).show();
            else if(!pass_str.equals(conPass_str))
                Toast.makeText(getActivity(),"New Password Not Matched!",Toast.LENGTH_LONG).show();
            else {
                jsonRequestAdapter jsonRequestAdapter = new jsonRequestAdapter();

                jsonRequestAdapter.setRequestAction("UPDATE");
                jsonRequestAdapter.setRequestData("password",pass_str);

                jsonRequestAdapter.setRequestParameteres("filter","id");
                sslConfigurationManager.updateUserData(jsonRequestAdapter,tokenId,getActivity());
            }
        });

        return v;
    }

    public void showPopUp(View V,Bitmap bm) {
        myDialog.setContentView(R.layout.qrcode_popup);
        myDialog.show();

        ImageView qrCode = (ImageView) myDialog.findViewById(R.id.popup_qr_code_image);
        qrCode.setImageBitmap(bm);
        myDialog.setCancelable(false);

        FloatingActionButton closeBtn = (FloatingActionButton) myDialog.findViewById(R.id.close_popup_qr_code);

        closeBtn.setOnClickListener(v -> myDialog.dismiss());
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
