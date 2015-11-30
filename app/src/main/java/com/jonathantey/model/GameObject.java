/**
 * Created by Fan Lu on 2015/11/17.
 * Contributor Jonathan Tey
 * This is the abstract class that all game object with extends from
 */
package com.jonathantey.model;

import android.graphics.Rect;

import java.io.Serializable;

public abstract class GameObject implements Serializable{

    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;

    public Rect getRectangle(){
        return new Rect(x, y, x + width, y + height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
