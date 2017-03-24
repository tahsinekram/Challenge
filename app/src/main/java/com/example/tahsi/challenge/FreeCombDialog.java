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
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tahsi.challenge.data.AppContract;


/**
 * {@link FreeCombDialog} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class FreeCombDialog extends DialogFragment {
    private ContentValues values=new ContentValues();
    private ButtonSelect mCallback;
    private View view;
    private ViewHolder viewHolder;
    public FreeCombDialog() {
        // Empty constructor required for DialogFragment
    }

    private static class ViewHolder {
        public TextView AttrFree1;
        public TextView AttrFree2;
        public EditText freeEdit1;
        public EditText freeEdit2;
        public TextView AttrComb1;
        public TextView AttrComb2;
        public EditText comb;
    }

    public static FreeCombDialog newInstance(String attr1, String attr2, String edit1, String edit2, String editcomb, int type, String free, int flag, String challenge) {
        FreeCombDialog frag = new FreeCombDialog();
        Bundle args = new Bundle();
        args.putString("Attribute1", attr1);
        args.putString("Attribute2",attr2);
        args.putString("edit1",edit1);
        args.putString("edit2",edit2);
        args.putString("editcomb",editcomb);
        args.putInt("type",type);
        args.putInt("flag",flag);
        args.putString("free",free);
        args.putString("Challenge",challenge);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        if(getArguments().getInt("type")==1) {
            if (view == null) {
                view = inflater.inflate(R.layout.popup_free, null);
                viewHolder = new ViewHolder();
                viewHolder.AttrFree1 = (TextView) view.findViewById(R.id.attribute1);
                viewHolder.AttrFree2 = (TextView) view.findViewById(R.id.attribute2);
                viewHolder.freeEdit1 = (EditText) view.findViewById(R.id.editAttr1);
                viewHolder.freeEdit1.setTextColor(getActivity().getResources().getColor(R.color.editText));
                viewHolder.freeEdit2 = (EditText) view.findViewById(R.id.editAttr2);
                viewHolder.freeEdit2.setTextColor(getActivity().getResources().getColor(R.color.editText));
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
        }
        if(getArguments().getInt("type")==2) {
            if (view == null) {
                view = inflater.inflate(R.layout.popup_comb, null);
                viewHolder=new ViewHolder();
                viewHolder.AttrComb1=(TextView) view.findViewById(R.id.attributeComb1);
                viewHolder.AttrComb2=(TextView) view.findViewById(R.id.attributeComb2);
                viewHolder.comb=(EditText) view.findViewById(R.id.editAttrComb1);
                viewHolder.comb.setTextColor(getActivity().getResources().getColor(R.color.editText));
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(),R.style.MyCustomTheme);
        if(getArguments().getInt("type")==1) {
            if (getArguments().getInt("flag") == 1) {
                viewHolder.AttrFree1.setText(getArguments().getString("Attribute1"));
                viewHolder.AttrFree2.setText(getArguments().getString("Attribute2"));
                alertDialogBuilder.setView(view);
            } else if (getArguments().getInt("flag") == 2) {
                viewHolder.AttrFree1.setText(getArguments().getString("Attribute1"));
                viewHolder.AttrFree2.setText(getArguments().getString("Attribute2"));
                viewHolder.freeEdit1.setText(getArguments().getString("edit1"));
                viewHolder.freeEdit2.setText(getArguments().getString("edit2"));
                alertDialogBuilder.setView(view);
            }
        }
        if(getArguments().getInt("type")==2) {
            if(getArguments().getInt("flag")==1){
                viewHolder.AttrComb1.setText(getArguments().getString("Attribute1"));
                viewHolder.AttrComb2.setText(getArguments().getString("Attribute2"));
                alertDialogBuilder.setView(view);
            }else if(getArguments().getInt("flag")==2){
                viewHolder.AttrComb1.setText(getArguments().getString("Attribute1"));
                viewHolder.AttrComb2.setText(getArguments().getString("Attribute2"));
                viewHolder.comb.setText(getArguments().getString("editcomb"));
                alertDialogBuilder.setView(view);
            }
        }

        alertDialogBuilder.setPositiveButton(R.string.dialogPos,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (getArguments().getInt("type") == 1) {
                    mCallback.button(1, viewHolder.freeEdit1.getText().toString().trim() + "/" + viewHolder.freeEdit2.getText().toString().trim(),
                            viewHolder.AttrFree1.getText().toString().trim(), viewHolder.AttrFree2.getText().toString().trim());
                    ((ViewGroup)view.getParent()).removeView(view);
                }
                else if(getArguments().getInt("type")==2) {
                    if(getArguments().getInt("flag")==1) {
                        values.put(AppContract.ChallengeEntry.COLUMN_CHALLENGE, getArguments().getString("Challenge"));
                        values.put(AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR, getArguments().getString("Attribute1")+"/"+getArguments().getString("Attribute2"));
                        values.put(AppContract.ChallengeEntry.COLUMN_COMBINED, viewHolder.comb.getText().toString().trim());
                        values.put(AppContract.ChallengeEntry.COLUMN_FREE, getArguments().getString("free"));
                        getActivity().getContentResolver().insert(AppContract.ChallengeEntry.CONTENT_URI_FREECOMB, values);
                        values.clear();
                        dismiss();
                        ((ViewGroup)view.getParent()).removeView(view);

                    }
                    else if(getArguments().getInt("flag")==2){
                        values.put(AppContract.ChallengeEntry.COLUMN_COMBINED, viewHolder.comb.getText().toString().trim());
                        values.put(AppContract.ChallengeEntry.COLUMN_FREE, getArguments().getString("free"));
                        int answerupdate=getActivity().getContentResolver().update(AppContract.ChallengeEntry.CONTENT_URI_FREECOMB,values,
                                AppContract.ChallengeEntry.COLUMN_CHALLENGE + " LIKE '%" + getArguments().getString("Challenge") + "%' COLLATE NOCASE",null);
                        values.clear();
                        dismiss();
                        ((ViewGroup)view.getParent()).removeView(view);
                    }
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
        public void button(int position, String etext, String at1, String at2);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (ButtonSelect)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


}




