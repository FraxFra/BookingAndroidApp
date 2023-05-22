package com.example.bookingandroidapp.Connections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestSessionTask extends AsyncTask<Void, Void, String> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private static final String LOGIN_URL = "http://192.168.174.114:8080/BookingWebApp_war_exploded/PageServlet?";

    public TestSessionTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String result;

        try {

            URL url = new URL(LOGIN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "operation=testSession";

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
        Result<Integer> decodedResult = Result.getFromJson(result, new TypeToken<Result<Integer>>(){});
        Log.i("TestSession: ", result);

        if (!decodedResult.ok) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setNeutralButton("ok", null);
            builder.setMessage(decodedResult.error);
            builder.show();
        }
    }


}
