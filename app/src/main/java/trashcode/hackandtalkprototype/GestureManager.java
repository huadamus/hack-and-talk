package trashcode.hackandtalkprototype;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

final class GestureManager implements SensorEventListener {

    private final GestureInterpreter gestureInterpreter;
    final ChatActivity chatActivity;

    GestureManager(ChatActivity chatActivity) {
        this.chatActivity = chatActivity;
        SensorManager sensorManager = (SensorManager) chatActivity.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), 10000);
        this.gestureInterpreter = new GestureInterpreter(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            gestureInterpreter.analyzeValues(sensorEvent.values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}