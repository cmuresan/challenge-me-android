package com.example.cristianmmuresan.challengeme.ui.home.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cristianmmuresan.challengeme.Globals;
import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.ui.login.LoginActivity;
import com.example.cristianmmuresan.challengeme.util.EmailValidator;
import com.example.cristianmmuresan.challengeme.util.PreferenceUtils;
import com.example.cristianmmuresan.challengeme.util.ProgressDialogUtil;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout emailView;
    private TextInputLayout usernameView;
    private TextInputLayout firstnameView;
    private TextInputLayout lastnameView;
    private TextInputLayout bikeView;
    private String mEmail;
    private String mFirstname;
    private String mLastname;
    private String mBike;
    private ProgressDialogUtil progressDialog;

    public AccountFragment() {
        // Required empty public constructor
    }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.btn_logout).setOnClickListener(this);
        view.findViewById(R.id.btn_save_user).setOnClickListener(this);

        emailView = (TextInputLayout) view.findViewById(R.id.emailInputLayout);
        firstnameView = (TextInputLayout) view.findViewById(R.id.firstnameInputLayout);
        lastnameView = (TextInputLayout) view.findViewById(R.id.lastnameInputLayout);
        bikeView = (TextInputLayout) view.findViewById(R.id.bikeInputLayout);

        setUserData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                PreferenceUtils.logout();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.btn_save_user:
                attemptSave();
//                PreferenceUtils.saveUser();
        }
    }

    private void attemptSave() {
        emailView.setError(null);

        mEmail = emailView.getEditText().getText().toString();
        mFirstname = firstnameView.getEditText().getText().toString();
        mLastname = lastnameView.getEditText().getText().toString();
        mBike = bikeView.getEditText().getText().toString();

        if (!isValidEmail(mEmail)) {
            emailView.requestFocus();
        } else {
            saveUser();
        }
    }

    private void saveUser() {
        progressDialog = new ProgressDialogUtil(getActivity());
        progressDialog.show();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(Globals.iUser.getToken(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    parseUser.setEmail(mEmail);
                    parseUser.put("firstname", mFirstname);
                    parseUser.put("lastname", mLastname);
                    parseUser.put("bike", mBike);

                    PreferenceUtils.saveUser(parseUser);
                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            progressDialog.dismiss();
                            if (e == null) {
                                Toast.makeText(getActivity(), "User successfully updated.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("Error updating user:", e.getMessage());
                                Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public boolean isValidEmail(String email) {
        EmailValidator ev = new EmailValidator();

        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.required_field));
            return false;
        } else if (!ev.validate(email)) {
            emailView.setError(getString(R.string.invalid_email_address));
            return false;
        }
        return true;
    }

    private void setUserData() {
        if (Globals.iUser != null && Globals.iUser.getEmail() != null) {
            if (!TextUtils.isEmpty(Globals.iUser.getEmail()))
                emailView.getEditText().setText(Globals.iUser.getEmail());
            if (!TextUtils.isEmpty(Globals.iUser.getFirstname()))
                firstnameView.getEditText().setText(Globals.iUser.getFirstname());
            if (!TextUtils.isEmpty(Globals.iUser.getLastname()))
                lastnameView.getEditText().setText(Globals.iUser.getLastname());
            if (!TextUtils.isEmpty(Globals.iUser.getBike()))
                bikeView.getEditText().setText(Globals.iUser.getBike());
        }
    }
}
