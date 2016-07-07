package com.king.countfetalmovement;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * Created by king.zhou on 2016/6/23.
 */
public class CountService extends Service implements SensorEventListener {
    private static final String TAG = "KING&TB" ;
    public static final String ACTION = "com.lql.service.ServiceDemo";
    public static long tenSeconds = 10000;
    public static long oneMin = 60000;
    public static long fiveMin = 60000*5;
    public static long fourMin = 60000*4;

    public long cycleMin = 0;

    public long intervalTime = oneMin;
    public long preShakeTime;
    public long startTime;
    public boolean hasShaked =false;

    SensorManager sensorManager = null;
    Vibrator vibrator = null;
    MonitorThread thread;


    int value = 0;
    int amount = 0;

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "CountService onBind");
        return null;
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "CountService onCreate");
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        startTime = System.currentTimeMillis();
        preShakeTime = startTime-oneMin;

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.v(TAG, "CountService onStart");
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "CountService onStartCommand");

        if (intent!=null&&intent.getExtras()!=null){
            startTime = intent.getExtras().getLong("startTime");
            amount = intent.getExtras().getInt("amount");
            value = (int) ((System.currentTimeMillis()-startTime)/1000);
            Log.i("TB","value "+value);
        }else{
            MyApplication.stopService = false;
        }

        thread = new MonitorThread();
        thread.start();

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            if (Math.abs(values[0]) > 16 ) {
                long currentTime = System.currentTimeMillis();

                if(isInFirstMinCycle(currentTime)&&!hasShaked){
                    preShakeTime =  currentTime;
                    EventBus.getDefault().post(new MsgShakeEvent("shake"));
                    vibrator.vibrate(1000);
                    hasShaked = true;
                    amount++;
                }

                if(currentTime-preShakeTime>intervalTime){
                    //摇动一次
                    preShakeTime =  currentTime;
                    EventBus.getDefault().post(new MsgShakeEvent("shake"));
                    vibrator.vibrate(1000);
                    amount++;
                }

            }
        }

    }

    public boolean isInFirstMinCycle(long currentTime){
        boolean result= false;

        if((((currentTime-startTime)%fiveMin)<oneMin)&&cycleMin<(currentTime-startTime)){
            cycleMin = cycleMin + fiveMin;
            result = true;
            hasShaked = false;
        }
        return result;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
            if(value<3600&&!MyApplication.stopService){
                stopForeground(true);
                Intent intent = new Intent("com.king.countfetalmeovement.destroy");
                Bundle bundle = new Bundle();
                bundle.putLong("startTime",startTime);
                bundle.putInt("amount",amount);
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }

        sensorManager.unregisterListener(this);
        thread.setStop(true);
    }



    class MonitorThread extends Thread{

        private volatile boolean isStop = false;
        public void setStop(boolean stop){
            this.isStop = stop;
        }

        @Override
        public void run() {
            super.run();
            while (!isStop){
                if (value<3600){
                    MsgShakeEvent event =  new MsgShakeEvent("updateClockUI");
                    event.setValue(value);
                    event.setAmount(amount);
                    event.setStartTime(startTime);
                    EventBus.getDefault().post(event);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    value++;
                }else {
                    EventBus.getDefault().post(new MsgUpdateEvent("dismiss"));
                    Utils.saveRecordBean(getApplication(),startTime,amount);
                    isStop = true;

                }
            }
        }
    }
}