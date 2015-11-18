package com.jonathantey.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jonathantey.breakout.GamePanel;

/**
 * Created by Lucas on 2015/11/17.
 */
public class Ball extends GameObject{

    private Paint paint;



    private int r;

    public Ball(int x, int y, int r){
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public void update(){
        if( x > GamePanel.WIDTH - this.r || x < r){

            if(x > GamePanel.WIDTH - this.r){
                x = GamePanel.WIDTH - this.r;
            }

            if(x < r){
                x = r;
            }

            dx = -dx;
        }
        if(y > GamePanel.HEIGHT - 2 * this.r|| y < this.r){

            if(y > GamePanel.HEIGHT - 2 * this.r){
                y = GamePanel.HEIGHT - 2 * this.r;
            }

            if(y < this.r){
                y = this.r;
            }

            dy = -dy;
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

    public void setVector(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public Rect getRectangle() {
        return new Rect(x - r, y - r, x + r, y + r);
    }
}
