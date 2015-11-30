/**
 * Created by Fan Lu on 2015/11/18.
 * Contributor Jonathan Tey
 * This is the RotationSensor that respond to Sensor event
 */

package com.jonathantey.breakout;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public class RotationSensor implements SensorEventListener{

    private BreakoutActivity breakoutActivity;
    private SensorManager sensorManager;
    private Sensor rotationSensor;
    private long curTime;
    private long lastUpdateTime;

    /**
     * Implemented by Fan Lu
     * RotationSensor constructor, set the breakout activity Context.
     */
    public RotationSensor(Context context){
        breakoutActivity = (BreakoutActivity) context;
    }

    /**
     * Implemented by Fan Lu
     * registerListener
     */
    public void registerListener(){
        sensorManager = (SensorManager) breakoutActivity.getSystemService(Context.SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * Implemented by Fan Lu
     * unregisterListener
     */
    public void unregisterListener(){
        sensorManager.unregisterListener(this);
    }

    /**
     * Implemented by Fan Lu
     * Get the sensor value on SensorChanged event happened
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        curTime = System.currentTimeMillis();

        //Make sure we respond to the sensor event based on FPS rates
        if(curTime - lastUpdateTime > 1000 / TimerThread.FPS){

            float xRotation = event.values[0];
            breakoutActivity.updateRotationSensor(xRotation);
            lastUpdateTime = curTime;

        }

    }

    /**
     * Implemented by Fan Lu
     * Keep the interface override method complete
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
