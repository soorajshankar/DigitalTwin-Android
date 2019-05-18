package tech.sooraj.androidsensormqtt

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

 class MainActivity : AppCompatActivity() {

    internal lateinit var mqttAndroidClient: MqttAndroidClient

    internal lateinit var serverUri: String
    internal lateinit var healthStatus:String

    internal var clientId: String? = null
    internal val subscriptionTopic = "spBv1.0/Android/subscribe"
    internal val publishTopic = "spBv1.0/Android/pub"
    internal val publishMessage = "Hello World!"
    private var seekBar: SeekBar? = null
    private var thread: Thread? = null
    private var tv_sensorValues: TextView? = null
    internal var seekValue: Int = 0
    internal lateinit var btn_stop: Button
    private var tv_info: TextView? = null

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
        initializeMqtt()
    }

    private fun initializeMqtt() {

        mqttAndroidClient = MqttAndroidClient(applicationContext, serverUri, clientId)
//        showProgressDialogue("Connecting..")
        mqttAndroidClient.setCallback(object : MqttCallbackExtended {
            override fun connectComplete(reconnect: Boolean, serverURI: String) {
//                hideProgressDialogue()
                if (reconnect) {
                    addToHistory("Reconnected to : $serverURI")
                    // Because Clean Session is true, we need to re-subscribe
//                    subscribeToTopic()
                } else {
                    addToHistory("Connected to: $serverURI")
                }
//                startPolling()
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
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    addToHistory("Failed to connect to: $serverUri")
                }
            })


        } catch (ex: MqttException) {
            ex.printStackTrace()
        }

    }

    private fun addToHistory(mainText: String) {
        println("LOG: $mainText")
    }

}
