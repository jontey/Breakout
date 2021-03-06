/**
 * Created by Fan Lu on 2015/11/16.
 * Contributor Jonathan Tey
 * This is the main game panel that perform the top level game logic
 */

package com.jonathantey.breakout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jonathantey.model.Ball;
import com.jonathantey.model.Brick;
import com.jonathantey.model.Paddle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, Serializable{

    public static int WIDTH = 0;
    public static int HEIGHT = 0;
    public static int BALL_RADIUS = 0;

    private SensorManager sensorManager;
    private TimerThread thread;
    private boolean gameStarted;
    private Paddle paddle;
    private Ball ball;
    private final double BASE_SPEED = 10;
    private int ballSpeed = 10;

    private ArrayList<ArrayList<Brick>> bricks;
    private Random random = new Random();

    //Game score
    private long time_start;
    private long time_end;

    /**
     * Implemented by Fan Lu
     * Initialize the main Game Panel
     */
    public GamePanel(Context context) {
        super(context);

        //Add the callback to the SurfaceHolder to the intercept events
        getHolder().addCallback(this);

        //Make GamePanel focusable to it can handle events
        setFocusable(true);
    }

    /**
     * Implemented by Fan Lu
     * Initialize all the Fields and Game Object when SurfaceView created and start the timer thread
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        WIDTH = getWidth();
        HEIGHT = getHeight();
        BALL_RADIUS = this.WIDTH / 24;

        resetGame(); //Initalize new game variables

        thread = new TimerThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    /**
     * Implemented by Fan Lu
     * Exist only for the SurfaceHolder.Callback completion
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * Implemented by Fan Lu
     * When surfaceDestroyed called, try to close the thread.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameStarted = false;
        int counter = 0;
        while(retry && counter < 1000){
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Implemented by Fan Lu
     * OnTouchEvent happens, update the Paddle position based on the event position
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //When user first touch the screen, if the game has not start yet, start the game.
            if(!gameStarted){
                gameStarted = true;
                time_start = System.currentTimeMillis();
                time_end = 0;
                ball.setVector(ballSpeed, -ballSpeed);
                ball.setMaxSpeed(ballSpeed);
                paddle.update((int) event.getX() - paddle.getWidth() / 2);
            }
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            //When game started, if user have MOVE Motion on the screen, move the paddle accordingly
            if(gameStarted){
                paddle.update((int)event.getX() - paddle.getWidth()/2);
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    /**
     * Implemented by Fan Lu
     * Called by the TimerThread to update all the Game Object in 25 FPS, can be changed in TimerThread
     * Also perform the collision detection and update Game Object accordingly
     */
    public void update() {
        if(gameStarted){
            if(!ball.isInPlay()){
                if(ball.isGame_won()){
                    wonGame();
                }
                resetGame(); //Ball is out of bounds.. reset game
                return;
            }
            //Paddle vs Ball collision detection
            if (Rect.intersects(ball.getRectangle(), paddle.getRectangle())) {

//                System.out.println("Ball Top : " + ball.getRectangle().top + "-Ball Bottom : " + ball.getRectangle().bottom
//                        + "-Ball Left : " + ball.getRectangle().left + "-Ball Right : " + ball.getRectangle().right);
//                System.out.println("Paddle Top : " + paddle.getRectangle().top + "-Paddle Bottom : " + paddle.getRectangle().bottom
//                        + "-Paddle Left : " + paddle.getRectangle().left + "-Paddle Right : " + paddle.getRectangle().right);

                if (CollisionDetectionHelper.topCollision(ball, paddle)) {
                    //Top collision detecion
                    ball.setY(paddle.getRectangle().top - ball.getR());
                    ball.setDy(-ball.getDy());
                    ball.setDx(calculateBounceOffset(ball.getDx(), ball.getDy()));

                }else if(CollisionDetectionHelper.bottomCollision(ball, paddle)){
                    ball.setY(paddle.getRectangle().top - ball.getR());
                    ball.setDy(-ball.getDy());
                    ball.setDx(calculateBounceOffset(ball.getDx(), ball.getDy()));

                }else if(CollisionDetectionHelper.sideCollision(ball, paddle)){
                    //Side collision detection
                    if(ball.getRectangle().centerX() < paddle.getRectangle().centerX()){
                        ball.setX(paddle.getX() - ball.getR());
                    }else{
                        ball.setX(paddle.getX() + paddle.getWidth() + ball.getR());
                    }

                    ball.setDx(-calculateBounceOffset(ball.getDx(), ball.getDy()));
                    ball.setDy(-ball.getDy());
                }else{
                    //Other collision detection
                    ball.setDx(-ball.getDx());
                }
            }

            //Brick vs Ball collision detection
            for(int levelIdx = 0; levelIdx < bricks.size(); levelIdx++){

                for(int rowIdx = 0; rowIdx < bricks.get(levelIdx).size(); rowIdx++){

                    if(Rect.intersects(ball.getRectangle(), bricks.get(levelIdx).get(rowIdx).getRectangle())){
                        if(CollisionDetectionHelper.bottomCollision(ball, bricks.get(levelIdx).get(rowIdx))){
                            ball.setDy(-ball.getDy());
                            ball.setDx(calculateBounceOffset(ball.getDx(), ball.getDy()));
                            bricks.get(levelIdx).get(rowIdx).setHardness(bricks.get(levelIdx).get(rowIdx).getHardness() - 1);

                        }else if(CollisionDetectionHelper.topCollision(ball, bricks.get(levelIdx).get(rowIdx))){
                            ball.setDy(-ball.getDy());
                            ball.setDx(calculateBounceOffset(ball.getDx(), ball.getDy()));
                            bricks.get(levelIdx).get(rowIdx).setHardness(bricks.get(levelIdx).get(rowIdx).getHardness() - 1);

                        }else if(CollisionDetectionHelper.sideCollision(ball, bricks.get(levelIdx).get(rowIdx))){
                            ball.setDx(-calculateBounceOffset(ball.getDx(), ball.getDy()));
                            bricks.get(levelIdx).get(rowIdx).setHardness(bricks.get(levelIdx).get(rowIdx).getHardness() - 1);

                        }else{
                            ball.setDx(-ball.getDx());
                            bricks.get(levelIdx).get(rowIdx).setHardness(bricks.get(levelIdx).get(rowIdx).getHardness() - 1);

                        }
                    }

                    if(bricks.get(levelIdx).get(rowIdx).getHardness() == 0){
                        bricks.get(levelIdx).remove(rowIdx);
                    }
                }

                if(bricks.get(levelIdx).size() == 0){
                    bricks.remove(levelIdx);
                }

            }


            ball.update();
        }
    }

    // Implemented by Jonathan Tey
    //Call only if game is won
    public void wonGame() {
        time_end = System.currentTimeMillis();
        gameStarted = false;
        //Calculate time
        long time_elapsed = time_end - time_start;
        int score = (int) (time_elapsed/1000); //Get score in # of seconds passed
        Intent intent = new Intent(getContext(), Leaderboard.class);
        intent.putExtra("score", score);
        getContext().startActivity(intent);
    }

    // Implemented by Jonathan Tey
    //Reset all game variables
    public void resetGame(){
        gameStarted = false;

        //Initiate all game objects
        paddle = new Paddle(getWidth()/2 - getWidth()/12, HEIGHT - getHeight()/6, getWidth()/6, getWidth()/18);
        ball = new Ball(getWidth()/2, paddle.getY() - getWidth()/24 ,BALL_RADIUS);
        bricks = new ArrayList<ArrayList<Brick>>();
        for(int levelIdx = 0; levelIdx < 3; levelIdx++){
            ArrayList<Brick> level = new ArrayList<Brick>();
            bricks.add(level);
            int previousHardness = 0;
            int currentHardness = 0;
            for(int rowIdx = 0; rowIdx < 9 - levelIdx; rowIdx++){
                currentHardness = (random.nextInt(5) + 1);
                while(previousHardness == currentHardness){
                    currentHardness = (random.nextInt(5) + 1);
                }
                previousHardness = currentHardness;
                level.add(new Brick(rowIdx * WIDTH / (9 - levelIdx), 0 + levelIdx * HEIGHT / 12,
                        WIDTH / (9 - levelIdx) - 4, HEIGHT / 12 - 4, currentHardness));
            }
        }

        //Reset time variables
        time_end = 0;
        time_start = System.currentTimeMillis();
    }

    /**
     * Implemented by Fan Lu
     * Draw the whole screen every frame
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //Draw all the elements.
        if(canvas != null){
            canvas.drawColor(Color.LTGRAY);
            paddle.draw(canvas);
            ball.draw(canvas);
            for(ArrayList<Brick> brickLevels: bricks){
                for(Brick brick: brickLevels){
                    brick.draw(canvas);
                }
            }

            //Update the time elapsed
            drawScore(canvas);



        }
    }

    /**
     * Implemented by Fan Lu
     * Draw the score on the left bottom of the screen
     */
    private void drawScore(Canvas canvas) {
        if(gameStarted){
            long time_elapsed = System.currentTimeMillis() - time_start;
            Paint paint2 = new Paint();
            paint2.setColor(Color.BLACK);
            paint2.setTextSize(30);
            paint2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            SimpleDateFormat sdf = new SimpleDateFormat("mm : ss");
            canvas.drawText("Time Elapsed = " + time_elapsed / (60 * 1000) + " : " + (time_elapsed / 1000) % 60 + "." + (time_elapsed / 100) % 10, 50, HEIGHT - 100, paint2);
        }
    }

    /**
     * Implemented by Fan Lu
     * Update the rotationSensor every time the gravity sensor updates
     */
    public void updateRotationSensor(float xRotation) {
        //Update ball's x axis acceleration based on how much device tilted along the x axis
        if(gameStarted){
            ball.setDx(ball.getDx() + -(int) xRotation);
        }

    }

    /**
     * Implemented by Fan Lu
     * Set the ball's speed
     */
    public void setBallSpeed(double multiplier){
        ballSpeed = (int) Math.round(BASE_SPEED * multiplier);
        ball.setSpeed(ballSpeed);
    }

    /**
     * Implemented by Fan Lu
     * Calculate the ball bounce offset within +- 7 degrees from the angle of impact(dx, dy).
     */
    public int  calculateBounceOffset(int dx, int dy){
        double inRadian = Math.atan2(dy, dx);
        double inAngle = Math.toDegrees(inRadian);

        double offSet = 14 * random.nextDouble() - 7;
        double outAngle = inAngle + offSet;

        int outDx = (int) (dy / Math.tan(Math.toRadians(outAngle)));

        return outDx;
    }
}
