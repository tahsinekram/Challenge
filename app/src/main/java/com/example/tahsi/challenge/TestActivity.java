package com.example.tahsi.challenge;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tahsi.challenge.data.AppContract;

import java.util.StringTokenizer;

/**
 * Created by tahsi on 1/31/2017.
 */

public class TestActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        FreeCombDialog.ButtonSelect{
    private BrainCursorAdapter mCursorAdapter;
    private StringTokenizer aToken,ftoken;
    private SearchView searchViewMain;
    private String rowSelect,comb,challenge;
    private Session session;
    private static String[] projection = {
            AppContract.ChallengeEntry._FREECOMBID,
            AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR,
            AppContract.ChallengeEntry.COLUMN_FREE,
            AppContract.ChallengeEntry.COLUMN_CHALLENGE,
            AppContract.ChallengeEntry.COLUMN_COMBINED};
//String []arg=new String[]{rowSelect};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attribute);
        FrameLayout r = (FrameLayout) findViewById(R.id.item_root);
        Drawable dr=new BitmapDrawable(decodeSampledBitmapFromResource(getResources(), R.drawable.menuback,365, 148));
        r.setBackgroundDrawable(dr);
        session=new Session(this);
        this.setTitle(R.string.AttributeTitle);
        Intent intent = getIntent();
        rowSelect=intent.getExtras().getString("qdata");
        ((TextView)findViewById(R.id.ftext)).setText(rowSelect.trim());
        mCursorAdapter = new BrainCursorAdapter(this, null,2);
        final ListView listView = (ListView) findViewById(R.id.Question);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor ans=(Cursor)mCursorAdapter.getItem(i);
                comb= ans.getString(ans.getColumnIndex(AppContract.ChallengeEntry.COLUMN_COMBINED));
                challenge=ans.getString(ans.getColumnIndex(AppContract.ChallengeEntry.COLUMN_CHALLENGE));
                aToken= new StringTokenizer(ans.getString(ans.getColumnIndex(AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR)),"/");
                ftoken=new StringTokenizer(ans.getString(ans.getColumnIndex(AppContract.ChallengeEntry.COLUMN_FREE)),"/");
                showFreeCombDialog(aToken.nextToken(),aToken.nextToken(),ftoken.nextToken(),ftoken.nextToken(),null,1,null,2,
                        null);
            }
        });
        getSupportLoaderManager().initLoader(0, null, this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.logout:
                logout();
                return true;
            case android.R.id.home:
                supportFinishAfterTransition();
                overridePendingTransition(0,0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        session.setLoggedin(false);
        supportFinishAfterTransition();
        startActivity(new Intent(TestActivity.this,LoginActivity.class));
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String whereClause= AppContract.ChallengeEntry.COLUMN_CHALLENGE +" LIKE '%" + rowSelect +"%' COLLATE NOCASE";

        return new CursorLoader(this,   // Parent activity context
                AppContract.ChallengeEntry.CONTENT_URI_FREECOMB,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                whereClause,                   // No selection clause
                null,                 // No selection arguments
                null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.search,menu);
        searchViewMain = (SearchView) menu.findItem(R.id.search).getActionView();
        searchViewMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText!=null){
                    mCursorAdapter.swapCursor(getContentResolver().query(AppContract.ChallengeEntry.CONTENT_URI_FREECOMB,projection,
                            AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR +" LIKE '%" + newText +"%' COLLATE NOCASE",null,null));
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    @Override
    public void button(int position, String etext, String at1, String at2) {
        if(position==1){
            showFreeCombDialog(at1,at2,null,null,comb,2,etext,2,challenge);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindDrawable(getDrawable(R.drawable.menuback));
    }

    private void unbindDrawable(Drawable d) {
        if (d != null)
            d.setCallback(null);
    }


    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    private void showFreeCombDialog(String attribute1, String attribute2,String edit1,String edit2,String edcomb,
                                int type,String etxt,int flag,String challenge) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("freedialog");
        if (prev != null) {
            ft.remove(prev);

        }
        // Create and show the dialog.
        FreeCombDialog freeDialog = FreeCombDialog.newInstance(attribute1,attribute2,edit1,edit2,edcomb,type,etxt,flag,challenge);
        freeDialog.show(ft, "freedialog");
    }
}
