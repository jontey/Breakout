/**
 * Created by Fan Lu on 2015/11/16.
 * This is the main game panel that perform the top level game logic
 */

package com.jonathantey.breakout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jonathantey.model.Ball;
import com.jonathantey.model.Paddle;

import java.io.Serializable;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, Serializable, SensorEventListener {

    public static int WIDTH = 0;
    public static int HEIGHT = 0;
    //Collision buffer to eliminate false detection.
    public static int collisionBuffer = 10;

    private TimerThread thread;
    private boolean gameStarted;
    private Paddle paddle;
    private Ball ball;
    private int ballSpeed = 10;

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
                ball.setVector(ballSpeed, -ballSpeed);
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
                resetGame(); //Ball is out of bounds.. reset game
                return;
            }
            //Paddle vs Ball collision detection
            if (Rect.intersects(ball.getRectangle(), paddle.getRectangle())) {
                System.out.println("Ball Top : " + ball.getRectangle().top + "-Ball Bottom : " + ball.getRectangle().bottom
                        + "-Ball Left : " + ball.getRectangle().left + "-Ball Right : " + ball.getRectangle().right);
                System.out.println("Paddle Top : " + paddle.getRectangle().top + "-Paddle Bottom : " + paddle.getRectangle().bottom
                        + "-Paddle Left : " + paddle.getRectangle().left + "-Paddle Right : " + paddle.getRectangle().right);
                if (ball.getRectangle().bottom > paddle.getRectangle().top
                        && ball.getRectangle().bottom < paddle.getRectangle().top + collisionBuffer
                        && ( ball.getRectangle().left + collisionBuffer < paddle.getRectangle().right
                        && ball.getRectangle().right > paddle.getRectangle().left + collisionBuffer)) {
                    //Top collision detecion
                    ball.setY(paddle.getRectangle().top - ball.getR());
                    ball.setDy(-ball.getDy());

                    System.out.println("Top Collision Occured");
                }else if(ball.getRectangle().bottom >= paddle.getRectangle().top
                        && ball.getRectangle().top < paddle.getRectangle().top + collisionBuffer ){
                    //Side collision detection
                    if(ball.getRectangle().centerX() < paddle.getRectangle().centerX()){
                        ball.setX(paddle.getX() - ball.getR());
                    }else{
                        ball.setX(paddle.getX() + paddle.getWidth() + ball.getR());
                    }

                    ball.setDx(-ball.getDx());
                    ball.setDy(-ball.getDy());

                    System.out.println("Side Collision Occured");
                }else{
                    //Other collision detection
                    ball.setDx(-ball.getDx());

                    System.out.println("Other Collision Occured");
                }
            }

            //Brick vs Ball collision detection



            ball.update();
        }
    }

    //Reset all game variables
    public void resetGame(){
        gameStarted = false;
        WIDTH = getWidth();
        HEIGHT = getHeight();
        System.out.println("surfaceCreated called View Width = " + WIDTH + "    View Height = " + HEIGHT);
        //Initiate all game objects
        paddle = new Paddle(getWidth()/2 - getWidth()/12, HEIGHT - getHeight()/6, getWidth()/6, getWidth()/18);
        ball = new Ball(getWidth()/2, paddle.getY() - getWidth()/24 ,getWidth()/24);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        //Draw all the elements.
        if(canvas != null){
            paddle.draw(canvas);
            ball.draw(canvas);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
