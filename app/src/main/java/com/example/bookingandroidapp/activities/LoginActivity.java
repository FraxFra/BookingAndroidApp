package com.example.bookingandroidapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingandroidapp.Connections.LoginTask;
import com.example.bookingandroidapp.Connections.TestSessionTask;
import com.example.bookingandroidapp.R;
import com.example.bookingandroidapp.account.CustomAccountManager;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    CustomAccountManager account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        account = new CustomAccountManager(this);

        TestSessionTask t = new TestSessionTask(this);
        if(CustomAccountManager.hasSavedCredentials()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        // Initialize UI elements
        mUsernameEditText = findViewById(R.id.username_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        Button mLoginButton = findViewById(R.id.login_button);

        // Set onClickListener for login button
        mLoginButton.setOnClickListener(v -> {
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            //eseguo il login
            LoginTask l = new LoginTask(this, LoginActivity.this, username, password);
            l.execute();
        });
    }


}
