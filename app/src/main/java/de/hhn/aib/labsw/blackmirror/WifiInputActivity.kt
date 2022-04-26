package de.hhn.aib.labsw.blackmirror

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.nio.charset.StandardCharsets

/**
 * WifiInputActivity to get the wifi data from the user and hand it over as UTF-8 String to the bluetooth interface.
 *
 * @author Niklas Binder
 * @version 2022-04-21
 */
class WifiInputActivity : AbstractActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_input)

        val wifiInputButton = findViewById<Button>(R.id.button_ConfirmWifiData)
        wifiInputButton.setOnClickListener {
            val wifiSsidEditText = findViewById<EditText>(R.id.editText_WifiSSID)
            val wifiPasswordEditText = findViewById<EditText>(R.id.editText_WifiPassword)
            if (wifiSsidEditText.text.toString().isBlank()) {
                Toast.makeText(
                    this,
                    getString(R.string.wifiInput_emptyWifiSSID_toastText),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                sendWifiData(
                    convertToUtf8String(wifiSsidEditText.text.toString()),
                    convertToUtf8String(wifiPasswordEditText.text.toString())
                )
            }
        }
    }

    /**
     * Function to convert a normal string to a UTF-8 compatible String.
     */
    private fun convertToUtf8String(input: String): String {
        val utf8Bytes: ByteArray = input.toByteArray(StandardCharsets.UTF_8)
        return String(utf8Bytes, StandardCharsets.UTF_8)
    }

    /**
     * Function to send the Wifi SSID & the Wifi password to the raspberry pi / blackmirror.
     */
    private fun sendWifiData(ssid: String, password: String) {
        //Sending Data
    }
}