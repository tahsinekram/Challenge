/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tahsi.challenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;

import com.example.tahsi.challenge.data.AppContract;


/**
 * {@link ChallengeDialog} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class ChallengeDialog extends DialogFragment {
    private EditText mEditText;
    private ContentValues values=new ContentValues();
    private ButtonSelect mCallback;
    private StringSelect sCallback;
    public ChallengeDialog() {
        // Empty constructor required for DialogFragment
    }
    public static ChallengeDialog newInstance(String title, String msg, int flag) {
        ChallengeDialog frag = new ChallengeDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("Message",msg);
        args.putInt("flag",flag);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        String message=getArguments().getString("Message");
        final int typeflag=getArguments().getInt("flag");
        mEditText=new EditText(getActivity());
        mEditText.setTextColor(getActivity().getResources().getColor(R.color.editText));
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.MyCustomTheme);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setIcon(R.drawable.goal);
        alertDialogBuilder.setView(mEditText);
        alertDialogBuilder.setPositiveButton(R.string.dialogPos,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(typeflag==1) {
                    values.put(AppContract.ChallengeEntry.COLUMN_CHALLENGE, mEditText.getText().toString().trim());
                    getActivity().getContentResolver().insert(AppContract.ChallengeEntry.CONTENT_URI_CHALLENGE, values);
                    values.clear();
                    mCallback.button(1,mEditText.getText().toString().trim());
                }
                else if(typeflag==2){
                    sCallback.select(mEditText.getText().toString().trim());
                }
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.dialogNeg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }



    // Container Activity must implement this interface
    public interface ButtonSelect {
        public void button(int position, String challenge);
    }

    public interface StringSelect {
        public void select(String etext);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ButtonSelect)activity;
            sCallback= (StringSelect)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


}




