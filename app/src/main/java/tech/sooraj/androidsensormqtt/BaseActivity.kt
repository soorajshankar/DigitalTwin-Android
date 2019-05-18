package tech.sooraj.androidsensormqtt


import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    var TAG="ANDROID_SENSOR_MQTT"
    var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pd = ProgressDialog(this)

    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
    }


    fun hideProgressDialogue() {
        if (pd!!.isShowing()) pd?.dismiss()
    }

    fun showProgressDialogue(message: String) {
        pd?.setMessage(message)
        pd?.show()
    }

}