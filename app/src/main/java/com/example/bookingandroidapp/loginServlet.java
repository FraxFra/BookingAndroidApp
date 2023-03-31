package com.example.bookingandroidapp;

import android.util.Log;

import com.example.bookingandroidapp.account.CustomAccountManager;
import com.example.bookingandroidapp.data.Result;
import com.example.bookingandroidapp.data.User;
import com.google.gson.reflect.TypeToken;
import java.lang.Boolean;

public class loginServlet {
    public static boolean login(String username, String password, final CustomAccountManager accountManager, LoginActivity activity) {
        String json = "operation=login&Username="+username+"&Password="+password;
        final boolean[] sas = {false};
        new RequestAsyncTask((res,errCode) -> {
            Log.d("Test2", "Login " + res);

            if (errCode != 200) {
                sas[0] = false;
            } else {
                Result<User> result = Result.getFromJson(res, new TypeToken<Result<User>>(){});
                if (result.ok) {
                    if (accountManager.hasSavedCredentials() == false) {
                        accountManager.saveLoginCredentials(username,password);
                    }
                    sas[0] = true;
                    //activity.setLoggedIn(true);
                }
            }
        }, !activity.isLoggedIn()).execute("PageServlet",json);
        return sas[0];
    }
}

