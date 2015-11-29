package com.jonathantey.breakout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class BreakoutActivity extends AppCompatActivity {

    private database db;
    private GamePanel gamePanel = null;
    private RotationSensor rotationSensor;

    // Implemented by Jonathan Tey
    // When app is launched
    // - Set fullscreen mode
    // - Set initial game conditions
    // - Initialize game controls and suitable listeners
    // - Wait for user to start game
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set the ContentView of the main activity to be the Game Panel
        setContentView(R.layout.breakout_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RelativeLayout gameLayout = (RelativeLayout) findViewById(R.id.gameLayout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (savedInstanceState != null){
            gamePanel = (GamePanel) savedInstanceState.getSerializable("GamePanel");
            System.out.println("Get Saved Game Panel");
        }else{
            gamePanel = new GamePanel(this);
            System.out.println("Create new Game Panel");
        }

        //gamePanel.setSystemUiVisibility( gamePanel.SYSTEM_UI_FLAG_HIDE_NAVIGATION );
        gameLayout.addView(gamePanel);
        System.out.println("onCreate called");

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_games_white_24dp);

        rotationSensor = new RotationSensor(this);
        db = new database(getBaseContext(), "leaderboard.txt");

        //Speed slider
        SeekBar speed_control = (SeekBar) findViewById(R.id.speed_control);
        speed_control.setProgress(20);
        speed_control.incrementProgressBy(10);
        speed_control.setMax(70);

        final TextView speed_display = (TextView) findViewById(R.id.speed);

        speed_control.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                switch(progress){
                    case 0:
                        speed_display.setText(String.valueOf("0.25x"));
                        gamePanel.setBallSpeed(0.25);
                        break;
                    case 1:
                        speed_display.setText(String.valueOf("0.5x"));
                        gamePanel.setBallSpeed(0.5);
                        break;
                    case 2:
                        speed_display.setText(String.valueOf("1.0x"));
                        gamePanel.setBallSpeed(1);
                        break;
                    case 3:
                        speed_display.setText(String.valueOf("2.0x"));
                        gamePanel.setBallSpeed(2);
                        break;
                    case 4:
                        speed_display.setText(String.valueOf("3.0x"));
                        gamePanel.setBallSpeed(3);
                        break;
                    case 5:
                    case 6:
                        speed_display.setText(String.valueOf("4.0x"));
                        gamePanel.setBallSpeed(4);
                        break;
                    case 7:
                        speed_display.setText(String.valueOf("5.0x"));
                        gamePanel.setBallSpeed(5);
                        break;
                    default:
                        speed_display.setText(String.valueOf("1.0x"));
                        gamePanel.setBallSpeed(1);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // Implemented by Jonathan Tey
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_breakout, menu);
        return true;
    }

    // Implemented by Jonathan Tey
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Start new game
        if(id == R.id.action_newgame){
            gamePanel.resetGame();
        }
        //Open leaderboard
        if (id == R.id.action_leaderboard) {
            Intent intent = new Intent(findViewById(R.id.toolbar).getContext(), Leaderboard.class);
            intent.putExtra("score", -1);
            findViewById(R.id.toolbar).getContext().startActivity(intent);
            return true;
        }

        if(id == R.id.action_quit){
            db.saveDatabase();
            this.finishAffinity();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("onRestoreInstanceState called");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable("GamePanel", gamePanel);
        System.out.println("protected onSaveInstanceState called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        rotationSensor.unregisterListener();
        System.out.println("onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("onStop called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        rotationSensor.registerListener();
        System.out.println("onResume called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart called");
    }

    public void updateRotationSensor(float xRotation) {
        gamePanel.updateRotationSensor(xRotation);
    }
}
