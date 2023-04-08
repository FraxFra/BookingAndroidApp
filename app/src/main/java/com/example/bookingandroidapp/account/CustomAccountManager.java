package com.example.bookingandroidapp.account;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

public class CustomAccountManager {
    private static SharedPreferences sharedPreferences;

    public CustomAccountManager(Activity activity){
        sharedPreferences = activity.getSharedPreferences("Preferences", MODE_PRIVATE);
    }

    public static void saveLoginCredentials(String username, String password){
        SharedPreferences.Editor e =  sharedPreferences.edit();
        e.putString("username",username);
        e.putString("password",password);
        e.apply();
    }

    public static boolean hasSavedCredentials(){
        return sharedPreferences.contains("username") && sharedPreferences.contains("password");
    }

    public Credential getLoginCredentials(){

        return new Credential(
                sharedPreferences.getString("username",null),
                sharedPreferences.getString("password",null)
        );
    }

    public static void deleteSavedCredentials(){
        SharedPreferences.Editor e =  sharedPreferences.edit();
        e.remove("username");
        e.remove("password");
        e.apply();
    }
}
