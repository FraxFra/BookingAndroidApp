package com.example.bookingandroidapp.Connections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class PrenotationBookerTask extends  AsyncTask<Void, Void, String>{
    @SuppressLint("StaticFieldLeak")
    private final int slotId;
    @SuppressLint("StaticFieldLeak")
    private final String subjectName;
    @SuppressLint("StaticFieldLeak")
    private final int teacherId;
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private static final String LOGIN_URL = "http://192.168.1.5:8080/BookingWebApp_war_exploded/SlotServlet?";

    @SuppressWarnings("deprecation")
    public PrenotationBookerTask(int slotId, String subjectName, int teacherId, Context context) {
        this.slotId = slotId;
        this.subjectName = subjectName;
        this.teacherId = teacherId;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        // la prenotazione viene effettuata su db
        String result;

        try {

            URL url = new URL(LOGIN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "operation=newBooking&SlotId=" + slotId + "&SubjectName=" + subjectName + "&TeacherId=" + teacherId;

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
        super.onPostExecute(result);
        Result<Integer> decodedResult = Result.getFromJson(result, new TypeToken<Result<Integer>>(){});
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setNeutralButton("ok", null);

        if (!Objects.equals(decodedResult.error, "")) {
            builder.setMessage(decodedResult.error);
        } else {
            if(!decodedResult.ok) {
                builder.setMessage("la prenotazione è stata precedentemente effettuata");
            } else {
                builder.setMessage("la prenotazione effettuata con successo!");
            }
        }
        builder.show();
    }
}

