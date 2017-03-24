package com.example.tahsi.challenge;

import android.content.ContentValues;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Toast;

import com.example.tahsi.challenge.data.AppContract;
import com.example.tahsi.challenge.databinding.ActivityRegisterBinding;

/**
 * Created by tahsi on 3/5/2017.
 */

public class RegisterActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginViewModel model= new LoginViewModel();
        ActivityRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        binding.setLoginViewModel(model);
        binding.setActivity(this);
        animateLogin((ViewGroup) binding.getRoot().findViewById(R.id.regroot));
    }


    public void onRegisterButtonClick(LoginViewModel lmodel) {
        String email = lmodel.getUsername().toString().trim();
        String pass = lmodel.getPassword().toString().trim();
        ContentValues values=new ContentValues();
        if (email.isEmpty() && pass.isEmpty()) {
            displayToast("Username/password field empty");
        } else {
            values.put(AppContract.ChallengeEntry.COLUMN_EMAIL,email);
            values.put(AppContract.ChallengeEntry.COLUMN_PASS,pass);
            getContentResolver().insert(AppContract.ChallengeEntry.CONTENT_URI_LOGIN,values);
            displayToast("User registered");
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            finish();
        }
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

    }

    private void animateLogin(ViewGroup root){
        int count = root.getChildCount();
        float offset = getResources().getDimensionPixelSize(R.dimen.offset_y);
        Interpolator interpolator =
                AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in);

        // loop over the children setting an increasing translation y but the same animation
        // duration + interpolation
        for (int i = 0; i < count; i++) {
            View view = root.getChildAt(i);
            view.setVisibility(View.VISIBLE);
            view.setTranslationY(offset);
            view.setAlpha(0.85f);
            // then animate back to natural position
            view.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setInterpolator(interpolator)
                    .setDuration(1000L)
                    .start();
            // increase the offset distance for the next view
            offset *= 1.5f;
        }
    }

}
