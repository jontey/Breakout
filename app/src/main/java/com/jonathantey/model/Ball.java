/**
 * Created by Fan Lu on 2015/11/17.
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

    private int r;

    private int maxSpeed;

    public Ball(int x, int y, int r){
        inPlay = true;
        this.x = x;
        this.y = y;
        this.r = r;
    }

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
        } else if (y < this.r){
            if(y < this.r){
                y = this.r;
            }
            dy = -dy;
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

    public void setVector(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public boolean isInPlay(){
        return inPlay;
    }

    @Override
    public Rect getRectangle() {
        return new Rect(x - r, y - r, x + r, y + r);
    }
}
