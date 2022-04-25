package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val helloBtn = findViewById<Button>(R.id.btn_hello)
        helloBtn.setOnClickListener {
            //val helloWorldLbl = findViewById<TextView>(R.id.lbl_helloWorld)
            //helloWorldLbl.visibility = View.VISIBLE;
        }
    }
}