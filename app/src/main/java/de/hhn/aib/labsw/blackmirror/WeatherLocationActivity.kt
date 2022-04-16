package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import java.lang.NumberFormatException

/**
 * @author Markus Marewitz
 * @version 2022-04-17
 */
class WeatherLocationActivity : AppCompatActivity() {
    lateinit var editTextLat: EditText
    lateinit var editTextLon: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_location)

        editTextLat = findViewById(R.id.editTextLat)
        editTextLon = findViewById(R.id.editTextLon)

        editTextLat.setText((49.066).toString())
        editTextLon.setText((9.220).toString())

        onTextChanged(editTextLat) { validateLat(it) }
        onTextChanged(editTextLon) { validateLon(it) }
    }

    /**
     * @param input must be a double between -90 and 90 degrees
     */
    private fun validateLat(input: String) {
        try {
            val lat = input.toDouble()
            if (lat < -90.0 || lat > 90.0)
                showInvalidInput(R.string.invalidLatTitle, R.string.invalidLatMsg)
        } catch (e: NumberFormatException) {
            showInvalidInput(R.string.invalidDoubleTitle, R.string.invalidDoubleMsg)
        }
    }

    /**
     * @param input must be a double between -180 and 180 degrees
     */
    private fun validateLon(input: String) {
        try {
            val lon = input.toDouble()
            if (lon < -180.0 || lon > 180.0)
                showInvalidInput(R.string.invalidLonTitle, R.string.invalidLonMsg)
        } catch (e: NumberFormatException) {
            showInvalidInput(R.string.invalidDoubleTitle, R.string.invalidDoubleMsg)
        }
    }

    private fun showInvalidInput(@StringRes titleResId: Int, @StringRes msgResId: Int) {
        runOnUiThread {
            AlertDialog.Builder(this)
                .setTitle(getString(titleResId))
                .setMessage(getString(msgResId))
                .show()
        }
    }

    private fun onTextChanged(editText: EditText, callback: (String) -> Unit) {
        editText.addTextChangedListener(object : TextChangeListener<EditText>(editText) {
            override fun onTextChanged(textView: EditText, s: Editable) {
                callback(s.toString())
            }
        })
    }
}