package com.example.bookingandroidapp.Connections;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.bookingandroidapp.account.CustomAccountManager;
import com.example.bookingandroidapp.activities.HomeActivity;
import com.example.bookingandroidapp.data.User;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class LoginTask extends AsyncTask<Void, Void, String> {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @SuppressLint("StaticFieldLeak")
    private final Activity activity;
    private final String username;
    private final String password;
    private ProgressDialog progressDialog;

    private static final String LOGIN_URL = "http://192.168.174.114:8080/BookingWebApp_war_exploded/PageServlet?";

    public LoginTask(Context context, Activity activity, String username, String password) {
        this.context = context;
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
    protected String doInBackground(Void... voids) {
        String result;

        try {

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
        Log.i("login: ", result);
        progressDialog.dismiss();

        if (!decodedResult.ok) {
            Toast.makeText(activity, result, Toast.LENGTH_SHORT).show();
        } else {
            //salvo le credenziali
            CustomAccountManager.saveLoginCredentials(username, password);

            //vado alla Home
            Intent intent = new Intent(activity, HomeActivity.class);
            activity.startActivity(intent);
            activity.finish();
        }
    }
}
