/**
 * Created by Jonathan Tey on 2015/11/16.
 * Contributor Fan Lu
 * This is the main game panel that perform the top level game logic
 */
package com.jonathantey.breakout;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 11/3/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private database db;
    private int edit_index;

    // Implemented by Jonathan Tey
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View myView;
        public ViewHolder(View v) {
            super(v);
            myView = v;
        }
    }

    // Implemented by Jonathan Tey
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(database database) {
        db = database;
    }

    // Implemented by Jonathan Tey
    public MyAdapter(database database, int idx) {
        edit_index = idx;
        db = database;
    }

    // Implemented by Jonathan Tey
    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Implemented by Jonathan Tey
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        TextView ranking = (TextView) holder.myView.findViewById(R.id.text_ranking);
        TextView name = (TextView) holder.myView.findViewById(R.id.text_name);
        TextView score = (TextView) holder.myView.findViewById(R.id.text_score);
        final EditText edit_name = (EditText) holder.myView.findViewById(R.id.edit_name);

        if(edit_index >= 0 && position == edit_index){
            name.setVisibility(View.INVISIBLE);
            edit_name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    ArrayList<String> db_line = db.data.get(edit_index);
                    db_line.set(0, s.toString());
                    db.saveDatabase();
                }
            });
        } else {
            //Hide if score not on leaderboard
            edit_name.setVisibility(View.INVISIBLE);
        }

        if(db.data.size() < 1) return;

        ArrayList<String> contact = db.data.get(position);

        if(position < 10) {
            ranking.setText(String.valueOf(position + 1));
            if (Integer.parseInt(contact.get(1)) == 999999999) {
                name.setText("________");
                score.setText("___");
            } else {
                name.setText(contact.get(0));
                score.setText(contact.get(1));
            }
        } else { //If current score is not on leaderboard
            ranking.setText("");
            name.setTypeface(null, Typeface.BOLD);
            score.setTypeface(null, Typeface.BOLD);
            name.setText("You");
            score.setText(contact.get(1));
        }
    }

    // Implemented by Jonathan Tey
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return db.data.size();
    }
}