package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helloBtn = findViewById<Button>(R.id.btn_hello)
        helloBtn.setOnClickListener {
            val intent = Intent(this, WifiInputActivity::class.java)
            startActivity(intent)
        }
    }
}