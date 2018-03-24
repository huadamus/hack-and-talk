package trashcode.hackandtalkprototype;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

final class GestureManager implements SensorEventListener {
    private SensorManager mSensorManager;

    GestureManager(Context context) {
        mSensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        assert mSensorManager != null;
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}