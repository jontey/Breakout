/**
 * Created by Fan Lu on 2015/11/16.
 * This is the main game panel that perform the top level game logic
 */

package com.jonathantey.breakout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.SensorManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jonathantey.model.Ball;
import com.jonathantey.model.Brick;
import com.jonathantey.model.Paddle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, Serializable{

    public static int WIDTH = 0;
    public static int HEIGHT = 0;

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

        resetGame(); //Initalize new game variables

        thread = new TimerThread(getHolder(), this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("surfaceChanged called");
    }

    /**
     * Implemented by Fan Lu
     * When surfaceDestroyed called, try to close the thread.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("surfaceDestroyed called");
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

                System.out.println("Ball Top : " + ball.getRectangle().top + "-Ball Bottom : " + ball.getRectangle().bottom
                        + "-Ball Left : " + ball.getRectangle().left + "-Ball Right : " + ball.getRectangle().right);
                System.out.println("Paddle Top : " + paddle.getRectangle().top + "-Paddle Bottom : " + paddle.getRectangle().bottom
                        + "-Paddle Left : " + paddle.getRectangle().left + "-Paddle Right : " + paddle.getRectangle().right);

                if (CollisionDetectionHelper.topCollision(ball, paddle)) {
                    //Top collision detecion
                    ball.setY(paddle.getRectangle().top - ball.getR());
                    ball.setDy(-ball.getDy());

                    System.out.println("Paddle Top Collision Occured");
                }else if(CollisionDetectionHelper.bottomCollision(ball, paddle)){
                    ball.setY(paddle.getRectangle().top - ball.getR());
                    ball.setDy(-ball.getDy());

                    System.out.println("Paddle Bottom Collision Occured");
                }else if(CollisionDetectionHelper.sideCollision(ball, paddle)){
                    //Side collision detection
                    if(ball.getRectangle().centerX() < paddle.getRectangle().centerX()){
                        ball.setX(paddle.getX() - ball.getR());
                    }else{
                        ball.setX(paddle.getX() + paddle.getWidth() + ball.getR());
                    }

                    ball.setDx(-ball.getDx());
                    ball.setDy(-ball.getDy());
                    System.out.println("Paddle Side Collision Occured");
                }else{
                    //Other collision detection
                    ball.setDx(-ball.getDx());
                    System.out.println("Paddle Other Collision Occured");
                }
            }

            //Brick vs Ball collision detection
            for(int levelIdx = 0; levelIdx < bricks.size(); levelIdx++){
                for(int rowIdx = 0; rowIdx < bricks.get(levelIdx).size(); rowIdx++){
                    if(Rect.intersects(ball.getRectangle(), bricks.get(levelIdx).get(rowIdx).getRectangle())){
                        if(CollisionDetectionHelper.bottomCollision(ball, bricks.get(levelIdx).get(rowIdx))){
                            ball.setDy(-ball.getDy());
                            bricks.get(levelIdx).get(rowIdx).setHardness(bricks.get(levelIdx).get(rowIdx).getHardness() - 1);

                            System.out.println("Brick Bottom Collision Occured");
                        }else if(CollisionDetectionHelper.topCollision(ball, bricks.get(levelIdx).get(rowIdx))){
                            ball.setDy(-ball.getDy());
                            bricks.get(levelIdx).get(rowIdx).setHardness(bricks.get(levelIdx).get(rowIdx).getHardness() - 1);

                            System.out.println("Brick Top Collision Occured");
                        }else if(CollisionDetectionHelper.sideCollision(ball, bricks.get(levelIdx).get(rowIdx))){
                            ball.setDx(-ball.getDx());
                            ball.setDy(-ball.getDy());
                            bricks.get(levelIdx).get(rowIdx).setHardness(bricks.get(levelIdx).get(rowIdx).getHardness() - 1);

                            System.out.println("Brick Side Collision Occured");
                        }else{
                            ball.setDx(-ball.getDx());
                            bricks.get(levelIdx).get(rowIdx).setHardness(bricks.get(levelIdx).get(rowIdx).getHardness() - 1);

                            System.out.println("Brick Other Collision Occured");
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

    //Call only if game is won
    public void wonGame() {
        time_end = System.currentTimeMillis();
        //Calculate time
        long time_elapsed = time_end - time_start;
        int score = (int) (time_elapsed/1000); //Get score in # of seconds passed
        Intent intent = new Intent(getContext(), Leaderboard.class);
        intent.putExtra("score", score);
        getContext().startActivity(intent);
    }

    //Reset all game variables
    public void resetGame(){
        gameStarted = false;
        WIDTH = getWidth();
        HEIGHT = getHeight();
        System.out.println("Game Reset called View Width = " + WIDTH + "    View Height = " + HEIGHT);
        //Initiate all game objects
        paddle = new Paddle(getWidth()/2 - getWidth()/12, HEIGHT - getHeight()/6, getWidth()/6, getWidth()/18);
        ball = new Ball(getWidth()/2, paddle.getY() - getWidth()/24 ,getWidth()/24);
        bricks = new ArrayList<ArrayList<Brick>>();
        for(int levelIdx = 0; levelIdx < 3; levelIdx++){
            ArrayList<Brick> level = new ArrayList<Brick>();
            bricks.add(level);
            for(int rowIdx = 0; rowIdx < 9 - levelIdx; rowIdx++){
                level.add(new Brick(rowIdx * WIDTH / (9 - levelIdx), 0 + levelIdx * HEIGHT / 12, WIDTH / (9 - levelIdx) - 4, HEIGHT / 12 - 4, random.nextInt(5) + 1));
            }
        }

        //Reset time variables
        time_end = 0;
        time_start = 0;
    }

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
        }
    }

    public void updateRotationSensor(float xRotation) {
        //Update ball's x axis acceleration based on how much device tilted along the x axis
        if(gameStarted){
            ball.setDx(ball.getDx() + -(int) xRotation);
        }

    }

    public void setBallSpeed(double multiplier){
        ballSpeed = (int) Math.round(BASE_SPEED * multiplier);
        ball.setSpeed(ballSpeed);
    }
}
