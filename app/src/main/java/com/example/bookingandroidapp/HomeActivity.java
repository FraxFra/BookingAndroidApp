package com.example.bookingandroidapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private TextView welcomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeTextView = findViewById(R.id.welcome_text_view);

        // Recupera il nome utente dall'intent di login
        String username = getIntent().getStringExtra("username");

        // Mostra un messaggio di benvenuto
        String welcomeMessage = "Benvenuto, " + username + "!";
        welcomeTextView.setText(welcomeMessage);
    }
}

