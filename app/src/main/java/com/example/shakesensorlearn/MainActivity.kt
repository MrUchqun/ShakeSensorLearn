package com.example.shakesensorlearn

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.seismic.ShakeDetector

class MainActivity : AppCompatActivity(),
    ShakeDetector.Listener {

    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var sensorManager: SensorManager
    private lateinit var shakeDetector: ShakeDetector
    private lateinit var sensorAccelerometer: Sensor
    private lateinit var sensorMagneticField: Sensor


    private var gravity = FloatArray(3)
    private var geoMagnetic = FloatArray(3)
    private val orientation = FloatArray(3)
    private val rotateMatrix = FloatArray(9)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        getShake()
    }

    private fun initViews() {
        imageView = findViewById(R.id.imageView)

        button = findViewById(R.id.button)
        button.setOnClickListener {
            imageView.rotation = 180f
        }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        val listenerAccelerometer = object : SensorEventListener {
            override fun onSensorChanged(p0: SensorEvent?) {
                gravity = p0!!.values

                SensorManager.getRotationMatrix(rotateMatrix, null, gravity, geoMagnetic)
                SensorManager.getOrientation(rotateMatrix, orientation)

                imageView.rotation = (-orientation[0] * 180 / Math.PI).toFloat()
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }
        }

        val listenerMagneticField = object : SensorEventListener {
            override fun onSensorChanged(p0: SensorEvent?) {
                geoMagnetic = p0!!.values

                SensorManager.getRotationMatrix(rotateMatrix, null, gravity, geoMagnetic)
                SensorManager.getOrientation(rotateMatrix, orientation)

                imageView.rotation = (-orientation[0] * 180 / Math.PI).toFloat()
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }
        }

        sensorManager.registerListener(
            listenerAccelerometer,
            sensorAccelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        sensorManager.registerListener(
            listenerMagneticField,
            sensorMagneticField,
            SensorManager.SENSOR_DELAY_NORMAL
        )

    }

    override fun hearShake() {
        Toast.makeText(this, "Exit App!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getShake() {
        shakeDetector = ShakeDetector(this)
        shakeDetector.start(sensorManager)
    }

}

