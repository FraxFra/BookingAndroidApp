package com.example.bookingandroidapp.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.bookingandroidapp.R;
import com.example.bookingandroidapp.account.CustomAccountManager;
import com.example.bookingandroidapp.data.Result;
import com.example.bookingandroidapp.data.User;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CustomAccountManager account = new CustomAccountManager(this);

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
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                new LoginTask(LoginActivity.this, username, password).execute(username, password);
            }
        });
    }

    public static class LoginTask extends AsyncTask<String, Void, String> {
        @SuppressLint("StaticFieldLeak")
        private final Activity activity;
        private final String username;
        private final String password;
        private ProgressDialog progressDialog;

        private static final String LOGIN_URL = "http://192.168.1.5:8080/BookingWebApp_war_exploded/PageServlet?";

        public LoginTask(Activity activity, String username, String password) {
            this.activity = activity;
            this.username = username;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(activity, "Login", "Logging in...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            String username = params[0];
            String password = params[1];
            String result = null;

            try {
                Uri.Builder builder = new Uri.Builder();

                URL url = new URL(LOGIN_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                String postData = "operation=login&Username=" + username + "&" + "Password=" + password;

                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    result = sb.toString();
                } else {
                    result = "Error: " + conn.getResponseMessage();
                }
            } catch (Exception e) {
                result = "Error: " + e.getMessage();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Result<User> decodedResult = Result.getFromJson(result, new TypeToken<Result<User>>(){});
            Log.i("login: ", result.toString());
            progressDialog.dismiss();

            if (!decodedResult.ok) {
                Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
            } else {
                // Login successful, start HomeActivity
                CustomAccountManager.saveLoginCredentials(username, password);
                Intent intent = new Intent(activity, HomeActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }

}
