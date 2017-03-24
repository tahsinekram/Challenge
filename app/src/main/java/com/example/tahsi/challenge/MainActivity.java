package com.example.tahsi.challenge;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.tahsi.challenge.data.AppContract;
import com.example.tahsi.challenge.swipemenulistview.SwipeMenu;
import com.example.tahsi.challenge.swipemenulistview.SwipeMenuCreator;
import com.example.tahsi.challenge.swipemenulistview.SwipeMenuItem;
import com.example.tahsi.challenge.swipemenulistview.SwipeMenuListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        ChallengeDialog.ButtonSelect,ChallengeDialog.StringSelect{

    private BrainCursorAdapter mCursorAdapter;
    private SwipeMenuListView cListview;
    private GlobalClass globalVariable;
    private SearchView searchViewMain;
    //private EditText input;
    private static int CHALLENGE_LOADER = 0;
    private SwipeMenuItem item1,item2;
    private FloatingActionButton fab;
    private SwipeMenuCreator creator;
    private static String [] projection = {
            AppContract.ChallengeEntry._ID,
            AppContract.ChallengeEntry.COLUMN_CHALLENGE};
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalVariable= (GlobalClass) getApplicationContext();
        setContentView(R.layout.activity_main);
        session = new Session(this);
        if(!session.loggedin()){
            logout();
        }
        mCursorAdapter=new BrainCursorAdapter(this,null,1);
        cListview = (SwipeMenuListView) findViewById(R.id.Question);

        cListview.setAdapter(mCursorAdapter);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog("CHALLENGE","Enter Challenge",1,"Challenge");
            }
        });

        creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                //create an action that will be showed on swiping an item in the list
                item1 = new SwipeMenuItem(
                        MainActivity.this);
                item1.setBackground(R.color.edit);
                // set width of an option (px)
                item1.setIcon(R.drawable.ic_action_edit);
                item1.setWidth(200);
                item1.setHeight(200);
                menu.addMenuItem(item1);

                item2 = new SwipeMenuItem(
                        MainActivity.this);
                // set item background
                item2.setIcon(R.drawable.ic_action_delete);
                item2.setBackground(R.color.delete);
                item2.setWidth(200);
                item2.setHeight(200);
                menu.addMenuItem(item2);
            }
        };

        cListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                intent.putExtra("qdata", ((TextView) view.findViewById(R.id.challenge)).getText().toString().trim());
                Pair<View, String> p1 = Pair.create(view.findViewById(R.id.quest), "time");
                Pair<View, String> p2 = Pair.create(view.findViewById(R.id.challenge), "motion");
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(MainActivity.this,p1,p2);
                startActivity(intent, options.toBundle());
            }
        });

        cListview.setMenuCreator(creator);
        // set SwipeListener
        cListview.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        cListview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        Cursor c= (Cursor)mCursorAdapter.getItem(position);
                        //unbindDrawable((menu.getMenuItem(position)).getBackground());
                        getContentResolver().delete(AppContract.ChallengeEntry.CONTENT_URI_CHALLENGE, AppContract.ChallengeEntry.COLUMN_CHALLENGE + " LIKE '%" + (c.getString(c.getColumnIndex(AppContract.ChallengeEntry.COLUMN_CHALLENGE))) + "%' COLLATE NOCASE", null);
                        getContentResolver().delete(AppContract.ChallengeEntry.CONTENT_URI_FREECOMB, AppContract.ChallengeEntry.COLUMN_CHALLENGE + " LIKE '%" +(c.getString(c.getColumnIndex(AppContract.ChallengeEntry.COLUMN_CHALLENGE)))+ "%' COLLATE NOCASE", null);
                        break;
                }
                return false;
            }
        });

        getSupportLoaderManager().initLoader(CHALLENGE_LOADER,null,this);

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
                    mCursorAdapter.swapCursor(getContentResolver().query(AppContract.ChallengeEntry.CONTENT_URI_CHALLENGE,projection,
                            AppContract.ChallengeEntry.COLUMN_CHALLENGE +" LIKE '%" + newText +"%' COLLATE NOCASE",null,null));
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.logout:
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void logout(){
        session.setLoggedin(false);
        finish();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,   // Parent activity context
                AppContract.ChallengeEntry.CONTENT_URI_CHALLENGE,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);
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
    protected void onDestroy(){
        super. onDestroy();
        cListview=null;
        fab=null;
        creator=null;
        item1=null;
        item2=null;
        unbindDrawable(getDrawable(R.drawable.ic_action_edit));
        unbindDrawable(getDrawable(R.drawable.ic_action_delete));
        unbindDrawable(getDrawable(R.drawable.head_bullet));
        unbindDrawable(getDrawable(R.drawable.menuback));
        unbindDrawable(getDrawable(R.drawable.water));
        getSupportLoaderManager().destroyLoader(CHALLENGE_LOADER);
    }

    private void unbindDrawable(Drawable d) {
        if (d != null)
            d.setCallback(null);
    }

    private void showAlertDialog(String title, String message,int flag,String fragname) {
        FragmentTransaction fm=getFragmentManager().beginTransaction();
        ChallengeDialog alertDialog = ChallengeDialog.newInstance(title,message,flag);
        alertDialog.show(fm,fragname);
    }

    @Override
    public void button(int position,String chall) {
        if(position==1){
            globalVariable.setChallenge(chall);
            showAlertDialog("CHALLENGE","Enter 12 Attributes",2,"Attribute");
        }
    }

    @Override
    public void select(String etext) {
        Intent intent = new Intent(getApplicationContext(), DiceActivity.class);
        intent.putExtra("qdata",etext);
        startActivity(intent);
    }


}

