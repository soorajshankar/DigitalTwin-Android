package tech.sooraj.androidsensormqtt


import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper



abstract class BaseActivity : AppCompatActivity() {
    open var TAG="ANDROID_SENSOR_MQTT"
    var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(this)

    }


    fun hideProgressDialogue() {
        if (pd!!.isShowing()) pd?.dismiss()
    }

    fun showProgressDialogue(message: String) {
        pd?.setMessage(message)
        pd?.show()
    }

}