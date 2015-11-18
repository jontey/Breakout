package com.jonathantey.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Lucas on 2015/11/17.
 */
public class Paddle extends GameObject{

    private Paint paint;

    public Paddle(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(int x){
        this.x = x;
    }

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
