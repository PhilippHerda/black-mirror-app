package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        object : CountDownTimer(2000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                startMainActivity()
            }
        }.start()
    }

    private fun startMainActivity() {
        startActivity(Intent(this, WidgetLayoutActivity::class.java))
    }
}