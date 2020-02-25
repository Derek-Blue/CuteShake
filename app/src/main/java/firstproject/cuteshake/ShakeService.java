package firstproject.cuteshake;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Random;

public class ShakeService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private float mAccel; //除重力外的加速度
    private float mAccelCurrent; //當前加速度，包括重力
    private float mAccelLast;  //最後的加速度，包括重力

    private int resId;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        resId = intent.getIntExtra("resid", 0);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_UI,new Handler());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float)Math.sqrt((double)x*x + y*y + z*z);
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f +delta;

        if (mAccel > 0.5){
            Random randomColor = new Random();
            int color = Color.argb(255,randomColor.nextInt(256),randomColor.nextInt(256),randomColor.nextInt(256));
            MainActivity.background.getBackground().setTint(color);
            MainActivity.background.getBackground().setAlpha(100);

            TypedArray typedArray = getResources().obtainTypedArray(resId);
            int count = typedArray.length();

            Random randomImage = new Random();
            int index = randomImage.nextInt(count);
            int[] resid = new int[count];
            for (int i=0; i < count; i++){
                resid[i] = typedArray.getResourceId(i,0);
            }
            typedArray.recycle();
            MainActivity.imageView.setImageResource(resid[index]);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
