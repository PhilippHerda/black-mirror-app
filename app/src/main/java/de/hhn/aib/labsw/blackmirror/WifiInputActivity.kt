package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.nio.charset.Charset

class WifiInputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_input)

        val wifiInputButton = findViewById<Button>(R.id.button_ConfirmWifiData)
        wifiInputButton.setOnClickListener {
            val wifiSsidEditText = findViewById<EditText>(R.id.editTextText_WifiSSID)
            val wifiPasswordEditText = findViewById<EditText>(R.id.editTextText_WifiPassword)
            println(wifiSsidEditText.text)
            println(wifiPasswordEditText.text)
            if (wifiSsidEditText.text.toString().isBlank()) {
                Toast.makeText(
                    this,
                    getString(R.string.wifiInput_emptyWifiSSID_toastText),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                sendWifiData(wifiSsidEditText.text.toString(), wifiPasswordEditText.text.toString())
            }
        }
    }

    fun sendWifiData(ssid: String, password: String) {
        //Schnittstelle
    }
}