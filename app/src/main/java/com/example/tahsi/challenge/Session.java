package com.example.tahsi.challenge;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tahsi on 3/5/2017.
 */

public class Session {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public Session(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences("Challenge", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public boolean loggedin(){
        return prefs.getBoolean("loggedInmode", false);
    }
}
