package com.example.bookingandroidapp;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingandroidapp.account.CustomAccountManager;

public class LoginActivity extends AppCompatActivity {

    private CustomAccountManager accountManager;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Trova gli elementi UI della schermata di login
        usernameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);
        loginButton = findViewById(R.id.button_login);

        // Aggiungi un ascoltatore di eventi al pulsante di login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Controlla se l'utente ha inserito un nome utente e una password validi
                if (isValidInput()) {
                    // Esegue il login
                    performLogin();
                } else {
                    // Mostra un messaggio di errore
                    Toast.makeText(LoginActivity.this, "Please enter valid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Controlla se l'utente ha inserito un nome utente e una password validi.
     *
     * @return true se l'input è valido, altrimenti false
     */
    private boolean isValidInput() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        return !username.isEmpty() && !password.isEmpty();
    }

    /**
     * Esegue il login.
     */
    private void performLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Esegue la logica per il login qui, ad esempio tramite una chiamata a un API
        boolean loginSuccess = loginServlet.login(username, password, accountManager, this);

        if (loginSuccess) {
            // Login effettuato con successo, puoi avviare l'activity successiva
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("username", username); // Passa il nome utente alla HomeActivity
            startActivity(intent);
            finish();
        } else {
            // Login fallito, mostra un messaggio di errore
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("Errore di login")
                    .setMessage("Nome utente o password non validi.")
                    .setPositiveButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}

