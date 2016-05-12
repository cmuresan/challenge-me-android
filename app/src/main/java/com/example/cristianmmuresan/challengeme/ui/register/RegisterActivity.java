package com.example.cristianmmuresan.challengeme.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.ui.home.HomeActivity;
import com.example.cristianmmuresan.challengeme.util.EmailValidator;
import com.example.cristianmmuresan.challengeme.util.PreferenceUtils;
import com.example.cristianmmuresan.challengeme.util.ProgressDialogUtil;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout emailView;
    private TextInputLayout passwordView;
    private TextInputLayout confirmPasswordView;
    private View focusView;
    private ProgressDialogUtil progresDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailView = (TextInputLayout) findViewById(R.id.emailInputLayout);
        passwordView = (TextInputLayout) findViewById(R.id.passwordInputLayout);
        confirmPasswordView = (TextInputLayout) findViewById(R.id.confirmPasswordInputLayout);

        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                progresDialog = new ProgressDialogUtil(this);
                progresDialog.show();
                attemptRegister();
                break;
        }
    }

    public void attemptRegister() {
        //Reset errors.
        emailView.setError(null);
        passwordView.setError(null);
        confirmPasswordView.setError(null);

        String email = null;
        String password = null;
        String confirmPassword = null;

        email = emailView.getEditText().getText().toString();
        password = passwordView.getEditText().getText().toString();
        confirmPassword = confirmPasswordView.getEditText().getText().toString();

        if (!isValidEmail(email) || !isValidPassword(password, confirmPassword)) {
            focusView.requestFocus();
        } else {
            registerUser(email, password);
        }
    }

    private void registerUser(String email, String password) {
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(email);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                progresDialog.dismiss();
                if(e == null){
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this, "There was an error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isValidPassword(String password, String confirmPassword) {
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.required_field));
            focusView = passwordView;
            return false;
        }
        if (TextUtils.isEmpty(confirmPassword)){
            confirmPasswordView.setError(getString(R.string.required_field));
            focusView = confirmPasswordView;
            return false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordView.setError(getString(R.string.passwords_not_matching));
            focusView = confirmPasswordView;
            return false;
        }
        return true;
    }

    public boolean isValidEmail(String email) {
        EmailValidator ev = new EmailValidator();

        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.required_field));
            focusView = emailView;
            return false;
        } else if (!ev.validate(email)) {
            emailView.setError(getString(R.string.invalid_email_address));
            focusView = emailView;
            return false;
        }
        return true;
    }
}
