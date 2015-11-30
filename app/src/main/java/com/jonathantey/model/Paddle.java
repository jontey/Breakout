/**
 * Created by Fan Lu on 2015/11/17.
 * Contributor Jonathan Tey
 * This class models the Paddle object
 */
package com.jonathantey.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Paddle extends GameObject{

    private Paint paint;

    /**
     * Implemented by Fan Lu
     * Paddle constructor
     */
    public Paddle(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Implemented by Fan Lu
     * Update paddle position
     */
    public void update(int x){
        this.x = x;
    }

    /**
     * Implemented by Fan Lu
     * Draw Paddle on the screen
     */
    public void draw(Canvas canvas){
        try{
            if(paint != null){
                canvas.drawRect(x, y, x + width, y + height, paint);
            }else{
                paint = new Paint();
                paint.setColor(Color.GRAY);
                paint.setStyle(Paint.Style.FILL);
            }
        }catch(Exception e){

        }
    }
}
