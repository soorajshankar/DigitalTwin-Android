package tech.sooraj.androidsensormqtt.Activities

import android.content.Context
import android.hardware.Sensor
import android.os.Bundle
import android.hardware.SensorManager
import android.util.Log
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import tech.sooraj.androidsensormqtt.BaseActivity
import tech.sooraj.androidsensormqtt.R


class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (gyroscopeSensor == null) {
            Log.e(TAG, "Proximity sensor not available.")
//            finish(); // Close app
        }
        // Create a listener
        var gyroscopeSensorListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
//                Log.e(TAG, "X>>"+sensorEvent.values[0].toString()+" Y>>"+sensorEvent.values[1].toString()+" Z>>"+sensorEvent.values[2].toString())
                // More code goes here
            }

            override fun onAccuracyChanged(sensor: Sensor, i: Int) {
                Log.e(TAG, "onAccuracyChanged.")
            }
        }

// Register the listener
        sensorManager.registerListener(gyroscopeSensorListener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
}
