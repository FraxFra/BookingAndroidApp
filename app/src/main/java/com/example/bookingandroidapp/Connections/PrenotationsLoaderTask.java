package com.example.bookingandroidapp.Connections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingandroidapp.data.AvailablePrenotationsAdapter;
import com.example.bookingandroidapp.data.Slot;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PrenotationsLoaderTask extends  AsyncTask<Void, Void, List<Slot>>{
    @SuppressLint("StaticFieldLeak")
    private final Context context;
    @SuppressLint("StaticFieldLeak")
    private final RecyclerView recyclerView;
    @SuppressLint("StaticFieldLeak")
    private final ProgressBar progressBar;
    @SuppressLint("StaticFieldLeak")
    private final TextView emptyView;
    private static final String LOGIN_URL = "http://192.168.1.5:8080/BookingWebApp_war_exploded/SlotServlet?";


    @SuppressWarnings("deprecation")
    public PrenotationsLoaderTask(Context context, RecyclerView recyclerView, ProgressBar progressBar, TextView emptyView) {
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
        this.emptyView = emptyView;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    protected List<Slot> doInBackground(Void... voids) {
        // Recupera la lista di prenotazioni dal database
        String result;

        try {

            URL url = new URL(LOGIN_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "operation=availableBookings";

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
        Result<Slot> decodedResult = Result.getFromJson(result, new TypeToken<Result<Slot>>(){});
        return decodedResult.data;
    }

    @Override
    protected void onPostExecute(List<Slot> prenotazioni) {
        super.onPostExecute(prenotazioni);
        progressBar.setVisibility(View.GONE);

        if (prenotazioni.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            AvailablePrenotationsAdapter adapter = new AvailablePrenotationsAdapter(prenotazioni, context);
            recyclerView.setAdapter(adapter);
        }
    }
}
