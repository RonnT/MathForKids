package top.titov.mathforkids;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public final class SensorClass implements SensorEventListener {
    private static int count;
    private static long someTime;
    private static int timeBetweenSome;
    private static boolean needTrackSquad;
    private static long lastSensorChangeTime;
    private static SensorManager sensorManager;
    public Runnable runnable;
    private final String tag;
    private boolean isListenerRegistered;

    public SensorClass(Context context, Runnable runnable) {
        this.tag = "HSquatSensor";
        this.isListenerRegistered = false;
        needTrackSquad = true;
        count = 0;
        someTime = 0;
        this.runnable = runnable;
        lastSensorChangeTime = 0;
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        SensorClass.sensorManager = sensorManager;
        if (sensorManager == null) {
            Log.v("sensor..", "Sensors not supported");
        }
    }

    public static void m6506a() {
        needTrackSquad = true;
        count = 0;
        someTime = 0;
        lastSensorChangeTime = 0;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int i) {
        count = i;
    }

    public final void registerSensor() {
        this.isListenerRegistered = sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 1);
    }

    public final void unregisterSensor() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            this.isListenerRegistered = false;
        }
    }

    public final boolean isRegistered() {
        return this.isListenerRegistered;
    }

    public final void onAccuracyChanged(Sensor sensor, int i) {
    }

    public final void onSensorChanged(SensorEvent sensorEvent) {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - lastSensorChangeTime;
        if (j > 100) {
            j = 0;
        }
        lastSensorChangeTime = currentTimeMillis;
        double sqrt = Math.sqrt((double) ((sensorEvent.values[0] * sensorEvent.values[0]) + (sensorEvent.values[1] * sensorEvent.values[1])));
        sqrt = Math.sqrt((sqrt * sqrt) + ((double) (sensorEvent.values[2] * sensorEvent.values[2]))) - 9.706650161743164d;
        if (someTime == 0) {
            if (sqrt < -0.6d) {
                someTime = j;
                timeBetweenSome = 0;
            }
        } else if (sqrt < -0.2d) {
            someTime += j;
            timeBetweenSome = 0;
        } else if (someTime > 0) {
            someTime -= j;
        }
        if (sqrt > 0.0d) {
            if (someTime > 225) {
                timeBetweenSome = (int) (((long) timeBetweenSome) + j);
                someTime += j;
                if (sqrt > 3.0d) {
                    timeBetweenSome = (int) (j + ((long) timeBetweenSome));
                }
                if (needTrackSquad && ((long) timeBetweenSome) > 450) {
                    needTrackSquad = false;
                    count++;
                    someTime = 0;
                    Log.v("HSquatSensor", "count: " + count);
                    this.runnable.run();
                    return;
                }
                return;
            }
            someTime = 0;
            timeBetweenSome = 0;
            needTrackSquad = true;
        } else if (timeBetweenSome > 0) {
            someTime -= 2 * j;
            timeBetweenSome = (int) (((long) timeBetweenSome) - (j * 2));
            needTrackSquad = true;
        }
    }
}