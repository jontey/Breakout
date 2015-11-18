package com.jonathantey.breakout;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 11/3/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private database db;
    private int edit_index;

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

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(database database) {
        db = database;
    }

    public MyAdapter(database database, int idx) {
        edit_index = idx;
        db = database;
    }

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

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        TextView ranking = (TextView) holder.myView.findViewById(R.id.text_ranking);
        TextView name = (TextView) holder.myView.findViewById(R.id.text_name);
        TextView score = (TextView) holder.myView.findViewById(R.id.text_score);

        if(db.data.size() < 1) return;

        ArrayList<String> contact = db.data.get(position);
        ranking.setText(position);
        if(Integer.parseInt(contact.get(1)) == 0){
            name.setText("-----");
            score.setText("--");
        } else {
            name.setText(contact.get(0));
            score.setText(contact.get(1));
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return db.data.size();
    }
}