package com.example.bookingandroidapp.account;

import android.app.Activity;
import android.content.SharedPreferences;

public class CustomAccountManager {
    private SharedPreferences sharedPreferences;

    public CustomAccountManager(Activity activity){
        sharedPreferences = activity.getApplicationContext().getSharedPreferences("Preferences",0);
    }

    public void saveLoginCredentials(String username, String password){
        SharedPreferences.Editor e =  sharedPreferences.edit();
        e.putString("username",username);
        e.putString("password",password);
        e.apply();
    }

    public boolean hasSavedCredentials(){
        return sharedPreferences.contains("username") && sharedPreferences.contains("password");
    }

    public Credential getLoginCredentials(){

        return new Credential(
                sharedPreferences.getString("username",null),
                sharedPreferences.getString("password",null)
        );
    }

    public void deleteSavedCredentials(){
        SharedPreferences.Editor e =  sharedPreferences.edit();
        e.remove("username");
        e.remove("password");
        e.apply();
    }
}
