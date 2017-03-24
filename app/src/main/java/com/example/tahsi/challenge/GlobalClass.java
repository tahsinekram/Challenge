package com.example.tahsi.challenge;

import android.app.Application;

/**
 * Created by tahsi on 2/1/2017.
 */

public class GlobalClass extends Application {

    private String challenge;


    public void setChallenge(String name){
        challenge=name;
    }


    public String getChallenge()
    {
        return challenge;
    }

}
