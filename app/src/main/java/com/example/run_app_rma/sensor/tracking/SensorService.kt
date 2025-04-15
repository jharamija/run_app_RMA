package com.example.run_app_rma.sensor.tracking

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class SensorService(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

    private var onAccelerometerData: ((FloatArray) -> Unit)? = null
    private var onGyroscopeData: ((FloatArray) -> Unit)? = null

    fun startListening(
        onAccelerometerData: (FloatArray) -> Unit,
        onGyroscopeData: (FloatArray) -> Unit
    ) {
        this.onAccelerometerData = onAccelerometerData
        this.onGyroscopeData = onGyroscopeData
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyroscope?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> onAccelerometerData?.invoke(event.values)
            Sensor.TYPE_GYROSCOPE -> onGyroscopeData?.invoke(event.values)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // You can log or handle accuracy changes if needed.
    }
}