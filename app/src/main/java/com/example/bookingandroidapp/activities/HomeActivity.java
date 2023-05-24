package com.example.bookingandroidapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.bookingandroidapp.Connections.PrenotationBookingsLoaderTask;
import com.example.bookingandroidapp.Connections.PrenotationsLoaderTask;
import com.example.bookingandroidapp.R;
import com.example.bookingandroidapp.account.CustomAccountManager;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    private CustomAccountManager account;
    private RecyclerView availableBookingsRecyclerView;
    private RecyclerView bookedPrenotationsRecyclerView;
    private ProgressBar progressBar;
    private TextView titoloTextView;

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
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

        //gestione delle animazioni
        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // Configurazione DrawerLayout e NavigationView
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_available_reservations:
                    // Gestione del click sul menu "Home"
                    drawerLayout.closeDrawers();
                    bookedPrenotationsRecyclerView.startAnimation(fadeOut);
                    bookedPrenotationsRecyclerView.setVisibility(View.GONE);

                    PrenotationsLoaderTask p = new PrenotationsLoaderTask(HomeActivity.this, availableBookingsRecyclerView, progressBar, titoloTextView);
                    p.execute();
                    break;

                case R.id.action_booked_prenotations:
                    // Gestione del click sul menu "le mie prenotazioni"
                    drawerLayout.closeDrawers();
                    availableBookingsRecyclerView.startAnimation(fadeOut);
                    availableBookingsRecyclerView.setVisibility(View.GONE);

                    PrenotationBookingsLoaderTask pb = new PrenotationBookingsLoaderTask(this, bookedPrenotationsRecyclerView, progressBar, titoloTextView);
                    pb.execute();
                    break;

                case R.id.action_logout:
                    // Gestione del click sul menu "Logout"
                    showLogoutDialog();
                    break;

            }
            return true;
        });

        //lista delle prenotazioni dispononibili
        availableBookingsRecyclerView = findViewById(R.id.available_prenotations_recycler_view);
        availableBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progress_bar);
        titoloTextView = findViewById(R.id.titolo_text_view);
        availableBookingsRecyclerView.setVisibility(View.VISIBLE);

        PrenotationsLoaderTask p = new PrenotationsLoaderTask(this, availableBookingsRecyclerView, progressBar, titoloTextView);
        p.execute();

        //lista delle prenotazioni già prenotate
        bookedPrenotationsRecyclerView = findViewById(R.id.booked_prenotations_recycler_view);
        bookedPrenotationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookedPrenotationsRecyclerView.setVisibility(View.GONE);


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
