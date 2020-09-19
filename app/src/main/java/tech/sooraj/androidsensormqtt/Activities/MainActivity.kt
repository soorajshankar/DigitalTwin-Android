package tech.sooraj.androidsensormqtt.Activities

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import tech.sooraj.androidsensormqtt.BaseActivity
import tech.sooraj.androidsensormqtt.R
import java.util.*
data class Rotation(val x: Number,val y: Number,val z: Number)
class MainActivity : BaseActivity() {
    override var TAG = "AppCompatActivity"

    internal lateinit var mqttAndroidClient: MqttAndroidClient

    internal lateinit var serverUri: String
    internal lateinit var healthStatus: String

    internal var clientId: String? = "client1"
//    internal val subscriptionTopic = "spBv1.0/Android/subscribe"
    internal var publishTopic:String = "digital_twin/android/"+clientId

    internal var isPolling: Boolean? = false;

    private var timer =Timer();
    private var tv_values: TextView? = null

    internal var seekValue: Int = 0
    internal lateinit var btn_stp_strt: Button
    var rotation:Rotation= Rotation(0,0,0)
    var lastRotation:Rotation= Rotation(0,0,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ip: String?
        healthStatus = "RED"
        val extras = intent.extras
        if (extras == null) {
            ip = "test.mosquitto.org"
            clientId = "AndroidTest"
        } else {
            ip = extras.getString("IPADDR")
            clientId = extras.getString("DEVICEID")
        }
        serverUri = "tcp://$ip:1883"
        seekValue = 0
        publishTopic="digital_twin/android/"+clientId

        initializeViews()
        initializeSensors()
        initializeMqtt()
    }

    private fun initializeViews() {
        tv_values=findViewById<TextView>(R.id.tv_values)
        tv_values?.setText("Sooraj Test")

        btn_stp_strt=findViewById<Button>(R.id.btn_stp_start)
        btn_stp_strt?.setText("Stop")
        btn_stp_strt.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                if(isPolling===true){
                return stopPolling()
                }else{
                    startPolling()
                }
            }
        })

    }

    private fun initializeSensors() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        if (gyroscopeSensor == null) {
            Log.e(TAG, "Proximity sensor not available.")
//            finish(); // Close app
        }
        // Create a listener
        var gyroscopeSensorListener = object : SensorEventListener {
            override fun onSensorChanged(sensorEvent: SensorEvent) {
                rotation= Rotation(sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2])
//                Log.e(TAG, "X>>"+sensorEvent.values[0].toString()+" Y>>"+sensorEvent.values[1].toString()+" Z>>"+sensorEvent.values[2].toString())
                tv_values?.setText("X:  "+rotation.x+"\n"+"Y:  "+rotation.y+"\n"+"Z:  "+rotation.z)
            }

            override fun onAccuracyChanged(sensor: Sensor, i: Int) {
                Log.e(TAG, "onAccuracyChanged.")
            }
        }

// Register the listener
        sensorManager.registerListener(gyroscopeSensorListener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)

    }

    private fun initializeMqtt() {

        mqttAndroidClient = MqttAndroidClient(applicationContext, serverUri, clientId)
        showProgressDialogue("Connecting..")
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {
                hideProgressDialogue()
                if (reconnect) {
                    addToHistory("Reconnected to : $serverURI")
                    // Because Clean Session is true, we need to re-subscribe
//                    subscribeToTopic()
                } else {
                    addToHistory("Connected to: $serverURI")
                }
                startPolling()
            }

            override fun connectionLost(cause: Throwable) {
                addToHistory("The Connection was lost.")
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                addToHistory("Incoming message: " + String(message.getPayload()))
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {

            }
        })

        val mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.setAutomaticReconnect(true)
        mqttConnectOptions.setCleanSession(true)


        try {
            addToHistory("Connecting to $serverUri")
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    val disconnectedBufferOptions = DisconnectedBufferOptions()
                    disconnectedBufferOptions.setBufferEnabled(true)
                    disconnectedBufferOptions.setBufferSize(100)
                    disconnectedBufferOptions.setPersistBuffer(false)
                    disconnectedBufferOptions.setDeleteOldestMessages(false)
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions)
                    hideProgressDialogue()
                    startPolling()
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    addToHistory("Failed to connect to: $serverUri" + exception.toString())
                    hideProgressDialogue()
                }
            })


        } catch (ex: MqttException) {
            ex.printStackTrace()
        }

    }

    fun publishMessage(publishMessage:String) {
        try {
            val message = MqttMessage()
            message.payload = publishMessage.toByteArray()
            mqttAndroidClient.publish(publishTopic, message)
            addToHistory("Message Published")
            if (!mqttAndroidClient.isConnected) {
                addToHistory(mqttAndroidClient.bufferedMessageCount.toString() + " messages in buffer.")
            }
        } catch (e: MqttException) {
            System.err.println("Error Publishing: " + e.message)
            e.printStackTrace()
        }

    }
    private fun stopPolling() {

        timer.purge()
        timer.cancel()

        btn_stp_strt?.setText("Start")
        val red = ContextCompat.getColor(this, R.color.Red)
        btn_stp_strt?.setBackgroundColor(red)
        isPolling=false
        showToast("MQTT Stream stoped")
    }
    private fun startPolling() {
        timer =Timer()

        btn_stp_strt?.setText("Stop")
        val green = ContextCompat.getColor(this, R.color.Green)
        btn_stp_strt?.setBackgroundColor(green)

        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if(isPolling==true){
                    addToHistory("vals>>>"+rotation.toString())
                    publishMessage(rotation.toString())
                }
            }
        }, 0, 1000)
        isPolling=true
        showToast("MQTT Stream started")
    }

    private fun showToast(s: String) {
        Toast.makeText(applicationContext,s,Toast.LENGTH_SHORT).show()
    }

    private fun addToHistory(mainText: String) {
        println("LOG: $mainText")
    }

}
