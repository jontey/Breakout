/**
 * Created by Fan Lu on 2015/11/16.
 * Set the timer for drawing the screen.
 */
package com.jonathantey.breakout;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.Serializable;


public class TimerThread extends Thread implements Serializable{


    private final String LOG_TAG = TimerThread.class.getSimpleName();
    protected static final int FPS = 30;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;
    private double averageFPS;

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    //Initialize the SurfaceHolder and GamePanel when creating the Thread
    public TimerThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    //Timer thread run method to update and draw the SurfaceView with 30 FPS rate
    public void run(){
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000 / FPS;

        while(running){
            startTime = System.nanoTime();
            canvas = null;
            try{
                //Try locking the canvas for pixel editing
                canvas = this.surfaceHolder.lockCanvas();
                synchronized(surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }catch (Exception e){
                Log.d(LOG_TAG, "Error in timer thread", e);
            }finally {
                if(canvas != null){
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }


            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                if(waitTime > 0){
                    this.sleep(waitTime);
                }else{
                    System.out.println("Wait Time Negative : " + waitTime);
                }
            } catch (InterruptedException e) {
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == FPS) {
                averageFPS = 1000 / ((totalTime/frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("average FPS : " + averageFPS);
            }
        }
    }

}
