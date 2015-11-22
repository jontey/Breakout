package com.jonathantey.breakout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private database db;
    private boolean menu_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Leaderboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        menu_save = false;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leaderboard, menu);
        menu.findItem(R.id.edit_save).setVisible(menu_save);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Reset leaderboard
        if(id == R.id.action_resetleaderboard){
            db.reset();
            mAdapter.notifyDataSetChanged();
        }
        if(id == android.R.id.home){
            db.sortData(1, true);
            db.trim(10);
            db.saveDatabase();
            this.finish();
        }
        if(id == R.id.action_newgame){
            db.sortData(1, true);
            db.trim(10);
            db.saveDatabase();
            this.finish();
        }
        if(id == R.id.edit_save){
            db.sortData(1, true);
            db.trim(10);
            db.saveDatabase();
            getIntent().removeExtra("score");
            getIntent().putExtra("score", -1);
            menu_save = false;
            recreate();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        int score = intent.getIntExtra("score", -1);
        //score = 100;

        db = new database(getBaseContext(), "leaderboard.txt");
        int edit_index = -1;
        if(score > 0){
            for(int i=0; i<db.data.size(); i++){
                if(edit_index >= 0) break; //We have found our position
                ArrayList<String> db_line = db.data.get(i);
                int s = Integer.parseInt(db_line.get(1));
                if(s > score || s == 0){
                    edit_index = i;
                }
            }

            ArrayList<String> current_score = new ArrayList<String>(2);
            current_score.add(0, "You");
            current_score.add(1, String.valueOf(score));
            db.add(current_score);
            db.sortData(1, true);
        }

        if(edit_index < 10 && edit_index > -1){
            menu_save = true;
            invalidateOptionsMenu();
            db.trim(10);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_leaderboard_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
//        mAdapter = new MyAdapter(db, edit_index);
        mAdapter = new MyAdapter(db, edit_index);
        mRecyclerView.setAdapter(mAdapter);

        this.findViewById(R.id.edit_name).setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause(){
        super.onPause();
        db.trim(10);
        db.saveDatabase();
    }
}
