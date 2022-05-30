package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, TodoListActivity::class.java))
//        startActivity(Intent(this, WeatherLocationActivity::class.java))
    }
}