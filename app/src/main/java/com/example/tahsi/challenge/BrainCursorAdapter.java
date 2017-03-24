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

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tahsi.challenge.data.AppContract;

/**
 * {@link BrainCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class BrainCursorAdapter extends CursorAdapter{

    private int typeFlag;
    private ViewHolder viewHolder;
    private Context ctx;
    /**
     * Constructs a new {@link BrainCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BrainCursorAdapter(Context context, Cursor c, int flag) {
        super(context, c, 0 /* flags */);
        typeFlag=flag;
    }

    /**
     * Makes a new blank list activity_attribute view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list activity_attribute view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list activity_attribute view using the layout specified in list_item.xml
        /*if(typeFlag==1){
            chId=cursor.getString(cursor.getColumnIndex(AppContract.BrainEntry._ID));
            ch=cursor.getString(cursor.getColumnIndex(AppContract.BrainEntry.COLUMN_CHALLENGE));
            synchronized (scrollCache) {
                if (scrollCache.get(chId) == null) {
                    scrollCache.put(chId, ch);
                    Toast.makeText(ctx, "Fucking here", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(typeFlag==2)
        {
            ansId= cursor.getString(cursor.getColumnIndex(AppContract.BrainEntry._ANSWERID));
            ans=cursor.getString(cursor.getColumnIndex(AppContract.BrainEntry.COLUMN_QUESTION));
            synchronized (scrollCache) {
                if (scrollCache.get(ansId) == null) {
                    scrollCache.put(ansId, ans);
                }
            }
        }*/
        View rowView= LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        viewHolder=new ViewHolder();
        viewHolder.ChallengeText=(TextView)rowView.findViewById(R.id.challenge);
        viewHolder.questImage=(ImageView)rowView.findViewById(R.id.quest);
        rowView.setTag(viewHolder);
        return rowView;
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list activity_attribute layout. For example, the name for the current pet can be set on the name TextView
     * in the list activity_attribute layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list activity_attribute layout
        String columnName=null;
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if(typeFlag==1) {
            columnName= cursor.getString(cursor.getColumnIndex(AppContract.ChallengeEntry.COLUMN_CHALLENGE));

        }
        else if(typeFlag==2)
        {
            columnName = cursor.getString(cursor.getColumnIndex(AppContract.ChallengeEntry.COLUMN_ATTRIBUTE_PAIR));
        }
        else{
            throw new IllegalArgumentException("App requiers a pair");
        }

        // Read the pet attributes from the Cursor for the current pet
        // Update the TextViews with the attributes for the current pet
        viewHolder.ChallengeText.setText(columnName);
        viewHolder.questImage.setImageResource(R.drawable.head_bullet);
    }


    private static class ViewHolder {
        public TextView ChallengeText;
        public ImageView questImage;

    }

}




