package com.example.cristianmmuresan.challengeme.ui.login;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.util.EmailValidator;
import com.example.cristianmmuresan.challengeme.util.ProgressDialogUtil;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout emailView;
    private ProgressDialogUtil progresDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailView = (TextInputLayout) findViewById(R.id.emailInputLayout);
        findViewById(R.id.btn_reset_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_reset_password:
                attemptReset();
                break;
        }
    }

    private void attemptReset() {
        String email = null;
        email = emailView.getEditText().getText().toString();

        if(!isValidEmail(email)){
            emailView.requestFocus();
        }else{
            progresDialog = new ProgressDialogUtil(this);
            progresDialog.show();
            resetPassword(email);
        }
    }

    private void resetPassword(String email) {
        ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(ParseException e) {
                progresDialog.dismiss();
                if(e == null){
                    Toast.makeText(ForgotPassword.this, "An email was successfully sent with reset instructions", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Log.e("Reset password error:", e.getMessage());
                    Toast.makeText(ForgotPassword.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    finish();
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
}
