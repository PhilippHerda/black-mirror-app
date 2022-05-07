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

        editTextLat.onFocusChangeListener = object : EditTextListener(editTextLat) {
            override fun onTextEdited(newText: String): Boolean {
                return validateLat(newText)
            }
        }

        editTextLon.onFocusChangeListener = object : EditTextListener(editTextLon) {
            override fun onTextEdited(newText: String): Boolean {
                return validateLon(newText)
            }
        }
    }

    /**
     * @param input must be a double between -90 and 90 degrees
     */
    private fun validateLat(input: String): Boolean {
        return validateInput(input) {
            return@validateInput if (it < -90.0 || it > 90.0) {
                showInvalidInput(R.string.invalidLatTitle, R.string.invalidLatMsg)
                false
            } else {
                true
            }
        }
    }

    /**
     * @param input must be a double between -180 and 180 degrees
     */
    private fun validateLon(input: String): Boolean {
        return validateInput(input) {
            return@validateInput if (it < -180.0 || it > 180.0) {
                showInvalidInput(R.string.invalidLonTitle, R.string.invalidLonMsg)
                false
            } else {
                true
            }
        }
    }

    /**
     * @param input must be a double that satisfies the 'additionalChecks'
     * @param additionalChecks callback for when the input was generally valid to make additional checks
     * @return `true` when the input is valid, `false` when the input should be rejected
     */
    private fun validateInput(input: String, additionalChecks: (Double) -> Boolean): Boolean {
        return if (input.isEmpty()) false
        else try {
            val double = input.toDouble()
            return additionalChecks(double)
        } catch (e: NumberFormatException) {
            showInvalidInput(R.string.invalidDoubleTitle, R.string.invalidDoubleMsg)
            false
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

}
