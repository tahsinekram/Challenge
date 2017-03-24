package com.example.tahsi.challenge;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahsi.challenge.data.AppContract;
import com.example.tahsi.challenge.databinding.ActivityLoginBinding;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<String>>{

    private static final String USGS_REQUEST_URL =
            "http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en";
    private static final int QUOTE_LOADER_ID = 1;
    private LoginViewModel model;
    private TextView quoteText;
    private Session session;
    private ConnectivityManager connMgr;
    private static String [] projection = {
            AppContract.ChallengeEntry._LOGINID,
            AppContract.ChallengeEntry.COLUMN_EMAIL,
            AppContract.ChallengeEntry.COLUMN_PASS};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "GloriaHallelujah.ttf");
        session = new Session(this);
        if(session.loggedin()){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        model=new LoginViewModel();
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLoginViewModel(model);
        quoteText=(TextView)binding.getRoot().findViewById(R.id.frameText);
        quoteText.setTypeface(myTypeface);
        animateLogin((ViewGroup) binding.getRoot().findViewById(R.id.loginRoot));
        binding.setActivity(this);

        connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(QUOTE_LOADER_ID, null, this);
        } else {
            quoteText.setText("No internet connection");
        }
    }

    public void register(){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        finish();
    }


    @Override
    public android.content.Loader<List<String>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new QuoteLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<String>> loader, List<String> strings) {
        quoteText.setText(strings.get(0));
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<String>> loader) {

        quoteText.clearComposingText();

    }

    public void login(LoginViewModel viewModel){

        if(viewModel.getUsername().toString().trim()!=null || viewModel.getPassword().toString().trim()!=null){
            String email = viewModel.getUsername().toString().trim();
            String pass = viewModel.getPassword().toString().trim();
            if(getUser(email,pass)){
                session.setLoggedin(true);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //Toast.makeText(this,viewModel.getPassword().toString().trim() + " " + viewModel.getUsername().toString().trim(),Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Incorrect format",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Wrong email/password",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getUser(String em, String pass){
        String whereClause= AppContract.ChallengeEntry.COLUMN_EMAIL + " = " + "'"+em+"'" + " and " +
                AppContract.ChallengeEntry.COLUMN_PASS + " = " + "'"+pass+"'";
        Cursor cursor=getContentResolver().query(AppContract.ChallengeEntry.CONTENT_URI_LOGIN,projection,whereClause,null,null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        return false;
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

