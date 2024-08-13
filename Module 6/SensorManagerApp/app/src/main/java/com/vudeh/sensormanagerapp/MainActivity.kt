package com.vudeh.sensormanagerapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var sensorEventListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SensorManagerApp", "onCreate called")
        setContentView(R.layout.activity_main)

        try {
            // Initialize SensorManager
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
            Log.d("SensorManagerApp", "SensorManager initialized")

            // Select the accelerometer sensor, handling the nullable type
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            Log.d("SensorManagerApp", "Accelerometer initialized: $accelerometer")

            // Implement the SensorEventListener
            sensorEventListener = object : SensorEventListener {
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

                override fun onSensorChanged(event: SensorEvent) {
                    try {
                        val x = event.values[0]
                        val y = event.values[1]
                        val z = event.values[2]
                        Log.d("SensorData", "X: $x, Y: $y, Z: $z")

                        // Update the TextView
                        val sensorValuesText = findViewById<TextView>(R.id.sensorValues)
                        sensorValuesText.text = "X: $x, Y: $y, Z: $z"
                    } catch (e: Exception) {
                        Log.e("SensorManagerApp", "Error processing sensor data", e)
                    }
                }
            }
            Log.d("SensorManagerApp", "SensorEventListener initialized")

        } catch (e: Exception) {
            Log.e("SensorManagerApp", "Error in onCreate", e)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("SensorManagerApp", "onResume called")
        try {
            accelerometer?.let {
                sensorManager.registerListener(sensorEventListener, it, SensorManager.SENSOR_DELAY_NORMAL)
                Log.d("SensorManagerApp", "SensorListener registered")
            } ?: run {
                Log.e("SensorManagerApp", "Accelerometer sensor not available")
            }
        } catch (e: Exception) {
            Log.e("SensorManagerApp", "Error in onResume", e)
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("SensorManagerApp", "onPause called")
        try {
            sensorManager.unregisterListener(sensorEventListener)
            Log.d("SensorManagerApp", "SensorListener unregistered")
        } catch (e: Exception) {
            Log.e("SensorManagerApp", "Error in onPause", e)
        }
    }
}
