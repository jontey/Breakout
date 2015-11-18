package com.jonathantey.breakout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class Leaderboard extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }
    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", -1);

        db = new database(getBaseContext(), "leaderboard.txt");
        /**
         * @TODO - Based on score check if score is on leaderboard or not
         * I think maybe check with the worst score - J
         */
        int edit_index = 0;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_leaderboard_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(db, edit_index);
        mRecyclerView.setAdapter(mAdapter);


    }
}
