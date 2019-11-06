package com.appdev.abhishek360.instruo.UserProfileFragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.appdev.abhishek360.instruo.R;
import com.appdev.abhishek360.instruo.Services.ApiRequestManager;
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
import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;

public class UserDetailsFragment extends Fragment {
    private ArrayList<String> personalDetails;
    private EditText fullName,emailId,contactNo,college;
    private EditText oldPass,newPass,conNewPass;
    private Button editDetails;
    private Button updatedetails,changePass;
    private ImageView qrCode_imageview;
    private ApiRequestManager apiRequestManager;
    private Dialog myDialog;
    private CompositeDisposable compositeDisposable;

    public static UserDetailsFragment newInstance(ArrayList<String> accountDetails) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("accountDetails", accountDetails);

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

        compositeDisposable = new CompositeDisposable();
        fullName = (EditText) v.findViewById(R.id.personal_details_edit_name);
        emailId = (EditText) v.findViewById(R.id.personal_details_edit_email);
        contactNo = (EditText) v.findViewById(R.id.personal_details_edit_contact);
        editDetails = (Button) v.findViewById(R.id.myprofile_button_edit_details);
        updatedetails = v.findViewById(R.id.myprofile_button_save_changes);
        changePass = v.findViewById(R.id.myprofile_button_change_pass);
        oldPass = v.findViewById(R.id.personal_details_edit_oldpass);
        newPass = v.findViewById(R.id.personal_details_edit_newpass);
        conNewPass = v.findViewById(R.id.personal_details_edit_confnewpass);
        college = v.findViewById(R.id.personal_details_edit_college);
        qrCode_imageview = v.findViewById(R.id.personal_details_qr_code);
        myDialog = new Dialog(this.getContext());
        apiRequestManager = new ApiRequestManager(getContext().getApplicationContext(), compositeDisposable);

        fullName.setText(personalDetails.get(0));
        emailId.setText(personalDetails.get(1));
        college.setText(personalDetails.get(2));
        contactNo.setText(personalDetails.get(3));

        final Bitmap myBitmap = QRCode.from("instruoX:" + personalDetails.get(1)).withSize(720,720).bitmap();
        qrCode_imageview.setImageBitmap(myBitmap);

        editDetails.setOnClickListener(v1 -> {
            fullName.setEnabled(true);
            college.setEnabled(true);
            //contactNo.setEnabled(true);
        });

        updatedetails.setOnClickListener(v12 -> {
            Map<String, String> updateRequest = new HashMap<>();
            updateRequest.put("name", fullName.getText().toString());
            updateRequest.put("college", college.getText().toString());
            fullName.setEnabled(false);
            college.setEnabled(false);

            apiRequestManager.updateUserData(updateRequest);
        });

        changePass.setOnClickListener(v13 -> {
            String pass_old = oldPass.getText().toString();
            String pass_str =  newPass.getText().toString();
            String conPass_str = conNewPass.getText().toString();

            if(pass_str.isEmpty())
                Toast.makeText(getActivity(),"Enter new Password!", Toast.LENGTH_LONG).show();
            else if(conPass_str.isEmpty())
                Toast.makeText(getActivity(),"Confirm new Password!", Toast.LENGTH_LONG).show();
            else if(!pass_str.equals(conPass_str))
                Toast.makeText(getActivity(),"New Password Not Matched!", Toast.LENGTH_LONG).show();
            else {
                Map<String, String> updatePassRequest = new HashMap<>();
                updatePassRequest.put("password_old", pass_old);
                updatePassRequest.put("password_new", pass_str);

                apiRequestManager.updateUserPass(updatePassRequest);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }
}
