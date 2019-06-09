package tech.sooraj.androidsensormqtt.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.widget.EditText
import android.widget.Toast
import tech.sooraj.androidsensormqtt.BaseActivity
import tech.sooraj.androidsensormqtt.R


class LoginActivity : BaseActivity() {
    private var btn_start: AppCompatButton? = null
    private var et_host : EditText? = null
    private var et_freq: EditText? = null
    private var et_client: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getViews()
        setListeners()
    }

    private fun setListeners() {
        btn_start?.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            val host= et_host?.text.toString()
            val client= et_client?.text.toString()
            val freq= et_freq?.text.toString()
            intent.putExtra("IPADDR",host)
            intent.putExtra("DEVICEID",client)
            intent.putExtra("FREQ",freq)
            startActivity(intent);
        }
    }

    private fun getViews() {
        btn_start = findViewById<AppCompatButton>(R.id.btn_start) as AppCompatButton
        et_host= findViewById<EditText>(R.id.et_host) as EditText
        et_freq= findViewById<EditText>(R.id.et_freq) as EditText
        et_client= findViewById<EditText>(R.id.et_client) as EditText
    }
}
