package com.example.tahsi.challenge;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by tahsi on 1/15/2017.
 */

public class DiceActivity extends AppCompatActivity implements Animation.AnimationListener,
        FreeCombDialog.ButtonSelect{

    private String AttrString,challenge;
    private Random rnd = new Random();
    public List<String> array;
    public int diceFace, animIndex, cumulative, attr1, attr2;
    private FloatingActionButton fab;
    private Animation shake;
    private ImageButton img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        this.setTitle(R.string.DiceTitle);
        animIndex=0;
        final GlobalClass globalVariable= (GlobalClass)getApplicationContext();
        challenge=globalVariable.getChallenge();
        img=(ImageButton) findViewById(R.id.dice);
        img.setImageResource(R.drawable.dice3droll);
        Intent intent = getIntent();
        AttrString = intent.getExtras().getString("qdata");
        array= new ArrayList<String>(Arrays.asList(AttrString.split("\\s*,\\s*")));
        shake = AnimationUtils.loadAnimation(DiceActivity.this, R.anim.shake);
        shake.setAnimationListener(this);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img.startAnimation(shake);
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoDialog();
            }
        });
    }


    @Override
    public void onAnimationStart(Animation animation) {
        diceFace = rnd.nextInt(6) + 1;
        animIndex += 1;
    }

    @Override
    public void onAnimationEnd(Animation animation) {

        switch (diceFace) {
            case 1:
                unbindDrawable(img.getDrawable());
                img.setImageResource(R.drawable.one);
                break;

            case 2:
                unbindDrawable(img.getDrawable());
                img.setImageResource(R.drawable.two);
                break;

            case 3:
                unbindDrawable(img.getDrawable());
                img.setImageResource(R.drawable.three);
                break;

            case 4:
                unbindDrawable(img.getDrawable());
                img.setImageResource(R.drawable.four);
                break;

            case 5:
                unbindDrawable(img.getDrawable());
                img.setImageResource(R.drawable.five);
                break;

            case 6:
                unbindDrawable(img.getDrawable());
                img.setImageResource(R.drawable.six);
                break;
        }

        if (animIndex == 1) {
            attr1 = diceFace;
        } else if (animIndex == 2) {
            animIndex=0;
            //Toast.makeText(getApplicationContext(),"here twice",Toast.LENGTH_LONG).show();
            attr2 = diceFace;
            cumulative = attr1 + attr2;
            showFreeDialog(array.get(attr1).trim(),array.get(cumulative).trim(),null,null,null,1,null,1,null);

        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    protected void onStop(){
        super.onStop();
        array.clear();
        unbindDrawable(getDrawable(R.drawable.dice3droll));
    }

    @Override
    protected void onDestroy(){
        array.clear();
        super.onDestroy();
        unbindDrawable(getDrawable(R.drawable.dice3droll));
    }

    private void unbindDrawable(Drawable d) {
        if (d != null)
            d.setCallback(null);
    }

    private void showFreeDialog(String attribute1, String attribute2,String edit1,String edit2,String edcomb,
                                int type,String etxt,int flag,String challenge) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("freecombdialog");
        if (prev != null) {
            ft.remove(prev);
        }
        // Create and show the dialog.
        FreeCombDialog freeDialog = FreeCombDialog.newInstance(attribute1,attribute2,edit1,edit2,edcomb,type,etxt,flag,challenge);
        freeDialog.show(ft, "freecombdialog");
    }




    @Override
    public void button(int position, String etext,String att1,String att2) {
        if(position==1){
            showFreeDialog(att1,att2,null,null,null,2,etext,1,challenge);
        }
    }

    private void showInfoDialog() {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction fo = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("infobdialog");
        if (prev != null) {
            fo.remove(prev);
        }
        // Create and show the dialog.
        InfoDialog info=InfoDialog.newInstance();
        info.show(fo, "infobdialog");
    }

}



