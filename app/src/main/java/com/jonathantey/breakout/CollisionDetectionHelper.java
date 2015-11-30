/**
 * Created by Fan Lu on 2015/11/20.
 * Contributor Jonathan Tey
 * Helper class for detecting ball vs paddle collision or ball vs brick collision
 */
package com.jonathantey.breakout;

import com.jonathantey.model.Ball;
import com.jonathantey.model.GameObject;

public class CollisionDetectionHelper {

    /**
     * Implemented by Fan Lu
     * Collision buffer to eliminate false detection.
     */
    private static int collisionBuffer = GamePanel.BALL_RADIUS;

    /**
     * Implemented by Fan Lu
     * Detect Top collision.
     */
    public static boolean topCollision(Ball ball, GameObject gameObject){
        if(ball.getRectangle().bottom > gameObject.getRectangle().top
                && ball.getRectangle().bottom < gameObject.getRectangle().top + collisionBuffer
                && (ball.getRectangle().right - collisionBuffer >= gameObject.getRectangle().left
                && ball.getRectangle().left + collisionBuffer < gameObject.getRectangle().right)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Implemented by Fan Lu
     * Detect bottom collision.
     */
    public static boolean bottomCollision(Ball ball, GameObject gameObject){
        if(ball.getRectangle().top < gameObject.getRectangle().bottom
                && ball.getRectangle().top > gameObject.getRectangle().bottom - collisionBuffer
                && (ball.getRectangle().left + collisionBuffer < gameObject.getRectangle().right
                && ball.getRectangle().right > gameObject.getRectangle().left + collisionBuffer)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Implemented by Fan Lu
     * Detect Side collision.
     */
    public static boolean sideCollision(Ball ball, GameObject gameObject){
        if(ball.getRectangle().bottom >= gameObject.getRectangle().top
                && ball.getRectangle().top < gameObject.getRectangle().top + collisionBuffer){
            return true;
        }else{
            return false;
        }
    }
}
