package com.example.bookingandroidapp;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RequestAsyncTask extends AsyncTask<String, Void, String> {
    private static final String DEBUG_TAG = "DEBUG";
    private static final String server_path = "http://192.168.1.5:8080/BookingWebApp_war_exploded/";
    static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();


    private PostRequestFunction postRequestFunction;
    private int resultCode = 999;
    private boolean cookieMode = false;

    public RequestAsyncTask(){
        super();
        postRequestFunction = null;
    }

    public RequestAsyncTask(PostRequestFunction p, boolean cookieMode){
        super();
        postRequestFunction = p;
        this.cookieMode = cookieMode;
    }

    public RequestAsyncTask(PostRequestFunction p){
        this(p,false);
    }

    @Override
    protected String doInBackground(String... urls) {

        // params arrivano da execute(..): params[0] e' l'URL
        try {
            Log.d("test",urls[1]);
            return downloadUrl(server_path + urls[0], urls[1]);
        } catch (IOException e) {
            return "Unable to retrieve web page. URL may be invalid.";
        }
    }

    // onPostExecute mostra il risultato di doInBackground(..)
    @Override
    protected void onPostExecute(String result) {
        if (postRequestFunction != null){
            postRequestFunction.executeAfterRequest(result,resultCode);
        }
    }

    // Dato un URL, crae una  HttpUrlConnection e recupera
    // il contenuto di una pagina WEB come InputStream,
    // e restituisci  una String

    private String downloadUrl(String myurl, String json) throws IOException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(myurl);
            byte[] input = json.getBytes(StandardCharsets.UTF_8);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(2000 /* milliseconds */);
            conn.setConnectTimeout(2000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
            conn.setRequestProperty("Accept","*/*");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( input.length ));
            conn.addRequestProperty("operation","login");

            if(!cookieMode){
                if (msCookieManager.getCookieStore().getCookies().size() > 0) {
                    // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
                    conn.setRequestProperty("Cookie",
                            TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
                }
            }

            //conn.connect();

            try(OutputStream os = conn.getOutputStream()){

                os.write(input, 0, input.length);
            }

            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            resultCode = response;
            // Converti  InputStream in  String

            if (cookieMode){
                Map<String, List<String>> headerFields = conn.getHeaderFields();
                List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

                if (cookiesHeader != null) {
                    for (String cookie : cookiesHeader) {
                        msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                    }
                }
            }

            return readIt(conn.getInputStream());
        } catch (Exception ex) {
            Log.e("async exception : ", ex.getMessage()+ "\n" + conn.getResponseMessage() + "\n" +  ex.getClass().getName() + "\n" + Arrays.toString(ex.getStackTrace()));
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    // Leggi InputStream e convertilo in Stringa
    String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            result.append(line).append("\n");
        }
        return result.toString();
    }
}
