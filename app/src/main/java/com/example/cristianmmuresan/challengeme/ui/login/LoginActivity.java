package com.example.cristianmmuresan.challengeme.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.cristianmmuresan.challengeme.R;
import com.example.cristianmmuresan.challengeme.ui.home.HomeActivity;
import com.example.cristianmmuresan.challengeme.ui.register.RegisterActivity;
import com.example.cristianmmuresan.challengeme.util.EmailValidator;
import com.example.cristianmmuresan.challengeme.util.PreferenceUtils;
import com.example.cristianmmuresan.challengeme.util.ProgressDialogUtil;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout emailView;
    private TextInputLayout passwordView;
    private View focusView;
    private ProgressDialogUtil progresDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailView = (TextInputLayout) findViewById(R.id.emailInputLayout);
        passwordView = (TextInputLayout) findViewById(R.id.passwordInputLayout);

        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_forgot_password).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                this.finish();
                break;
            case R.id.btn_login:
                attemptLogin();
                break;
            case R.id.btn_forgot_password:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }
    }

    private void attemptLogin() {
        //Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        String email = null;
        String password = null;
        email = emailView.getEditText().getText().toString();
        password = passwordView.getEditText().getText().toString();

        if (!isValidEmail(email) || !isValidPassword(password)) {
            focusView.requestFocus();
        } else {
            progresDialog = new ProgressDialogUtil(this);
            progresDialog.show();
            loginUser(email, password);
        }
    }

    private void loginUser(String email, String password) {
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                progresDialog.dismiss();
                if (parseUser != null) {
                    PreferenceUtils.saveUser(parseUser);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.required_field));
            focusView = passwordView;
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
