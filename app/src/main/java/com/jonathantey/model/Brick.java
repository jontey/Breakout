/**
 * Created by Fan lu on 2015/11/20.
 * Contributor Jonathan Tey
 * This class models the brick object
 */
package com.jonathantey.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Brick extends GameObject {

    private int hardness;

    /**
     * Implemented by Fan Lu
     * Brick constructor
     */
    public Brick (int x, int y, int width, int height, int hardness){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hardness = hardness;
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    /**
     * Implemented by Fan Lu
     * Draw the brick based on its hardness
     */
    public void draw(Canvas canvas){
        Paint paint1 = new Paint();
        paint1.setStyle(Paint.Style.FILL);
        switch(this.hardness){
            case 5: paint1.setColor(Color.BLACK);
                break;
            case 4: paint1.setColor(Color.RED);
                break;
            case 3: paint1.setColor(Color.GREEN);
                break;
            case 2: paint1.setColor(Color.BLUE);
                break;
            case 1: paint1.setColor(Color.WHITE);
                break;
            default:
        }
        canvas.drawRect(this.x,this.y,this.x + this.width, this.y + this.height, paint1);
    }

}
