/**
 * Created by Fan Lu on 2015/11/18.
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

    public RotationSensor(Context context){
        breakoutActivity = (BreakoutActivity) context;
    }

    public void registerListener(){
        sensorManager = (SensorManager) breakoutActivity.getSystemService(Context.SENSOR_SERVICE);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void unregisterListener(){
        sensorManager.unregisterListener(this);
    }

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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
