package com.example.bookingandroidapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingandroidapp.Connections.LogoutTask;
import com.example.bookingandroidapp.Connections.PrenotationsLoaderTask;
import com.example.bookingandroidapp.R;
import com.example.bookingandroidapp.account.CustomAccountManager;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private CustomAccountManager account;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView titoloTextView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        account = new CustomAccountManager(this);

        //set dell'username
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.user_name);
        usernameTextView.setText(CustomAccountManager.getLoginCredentials().username);

        // Configurazione Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurazione DrawerLayout e NavigationView
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_available_reservations:
                    // Gestione del click sul menu "Home"
                    PrenotationsLoaderTask p = new PrenotationsLoaderTask(HomeActivity.this, recyclerView, progressBar, titoloTextView);
                    p.execute();
                    break;
                case R.id.action_logout:
                    // Gestione del click sul menu "Logout"
                    showLogoutDialog();
                    break;
            }
            return true;
        });

        //lista delle prenotazioni dispononibili
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progress_bar);
        titoloTextView = findViewById(R.id.titolo_text_view);

        PrenotationsLoaderTask p = new PrenotationsLoaderTask(this, recyclerView, progressBar, titoloTextView);
        p.execute();

    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Sei sicuro di voler effettuare il logout?");
        builder.setPositiveButton("Sì", (dialog, which) -> {
            //eseguo il logout
            LogoutTask l = new LogoutTask(HomeActivity.this);
            l.execute();
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

}
