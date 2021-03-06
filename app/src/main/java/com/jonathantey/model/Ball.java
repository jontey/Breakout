/**
 * Created by Fan Lu on 2015/11/17.
 * Contributor Jonathan Tey
 * This class models the Ball object
 */
package com.jonathantey.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jonathantey.breakout.GamePanel;

public class Ball extends GameObject{

    private Paint paint;
    private boolean inPlay;
    private boolean game_won;

    private int r;

    private int maxSpeed;

    /**
     * Implemented by Fan Lu
     * Ball constructor
     */
    public Ball(int x, int y, int r){
        inPlay = true;
        game_won = false;
        this.x = x;
        this.y = y;
        this.r = r;
    }

    /**
     * Implemented by Fan Lu
     * Update the Ball parameter
     */
    public void update(){
        if(!inPlay){
            return;
        }
        if( x > GamePanel.WIDTH - this.r || x < r){

            if(x > GamePanel.WIDTH - this.r){
                x = GamePanel.WIDTH - this.r;
            }

            if(x < r){
                x = r;
            }

            dx = -dx;
        }
        if(y > GamePanel.HEIGHT - 2 * this.r){ //Reached bottom of screen
            if(y > GamePanel.HEIGHT - 2 * this.r){
                y = GamePanel.HEIGHT - 2 * this.r;
            }
            //Ball is out of bounds
            inPlay = false;
        } else if (y < this.r){ //Reached top of screen
            if(y < this.r){
                y = this.r;
            }
            dy = -dy;
            inPlay = false;
            game_won = true;
        }

        if(dx > maxSpeed){
            dx = maxSpeed;
        }

        if(dx < -maxSpeed){
            dx = -maxSpeed;
        }
        this.x += this.dx;
        this.y += this.dy;
    }

    /**
     * Implemented by Fan Lu
     * Draw the Ball
     */
    public void draw(Canvas canvas){
        try{
            if(paint != null){
                canvas.drawCircle(x, y, r, paint);
            }else{
                paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
            }
        }catch(Exception e){

        }
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }


    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Implemented by Fan Lu
     * Update the ball with the new speed
     */
    public void setSpeed(int speed){
        setMaxSpeed(speed);
        if(dx < 0){
            dx = -speed;
        } else {
            dx = speed;
        }
        if(dy < 0){
            dy = -speed;
        } else {
            dy = speed;
        }
    }

    /**
     * Implemented by Fan Lu
     * Ball constructor
     */
    public void setVector(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public boolean isInPlay(){
        return inPlay;
    }

    public boolean isGame_won(){
        return game_won;
    }
    @Override
    public Rect getRectangle() {
        return new Rect(x - r, y - r, x + r, y + r);
    }
}
