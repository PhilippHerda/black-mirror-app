package de.hhn.aib.labsw.blackmirror

import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import de.hhn.aib.labsw.blackmirror.dataclasses.Location

/**
 * @author Markus Marewitz, Niklas Binder
 * @version 2022-06-16
 */
class WeatherLocationActivity : AbstractActivity() {
    lateinit var editTextLat: EditText
    lateinit var editTextLon: EditText
    private lateinit var sendWeatherLocationButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_location)

        editTextLat = findViewById(R.id.editTextLat)
        editTextLon = findViewById(R.id.editTextLon)
        sendWeatherLocationButton = findViewById(R.id.btnSendWeatherLocationData)

        editTextLat.setText((49.066).toString())
        editTextLon.setText((9.220).toString())

        editTextLat.onFocusChangeListener = object : EditTextListener(editTextLat) {
            override fun onTextEdited(newText: String): Boolean {
                var newTextCleared = newText.replace(",", ".")
                editTextLat.text = Editable.Factory.getInstance().newEditable(newTextCleared)
                return validateLat(newTextCleared)
            }
        }
        editTextLon.onFocusChangeListener = object : EditTextListener(editTextLon) {
            override fun onTextEdited(newText: String): Boolean {
                var newTextCleared = newText.replace(",", ".")
                editTextLon.text = Editable.Factory.getInstance().newEditable(newTextCleared)
                return validateLon(newTextCleared)
            }

        }

        sendWeatherLocationButton.setOnClickListener {
            if (validateLat(editTextLat.text.toString()) && validateLon(editTextLon.text.toString())) {
                val location = Location(editTextLat.text.toString().toDouble(),
                    editTextLon.text.toString().toDouble())

                publishToRemotes("location", location)
            }
        }
    }

    /**
     * @param input must be a double between -90 and 90 degrees
     */
    private fun validateLat(input: String): Boolean {
        return validateInput(input) {
            return@validateInput if (it < -90.0 || it > 90.0) {
                Toast.makeText(this, getString(R.string.invalidLatTitle) + " " + getString(R.string.invalidLatMsg), Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, getString(R.string.invalidLonTitle) + " " + getString(R.string.invalidLonMsg), Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, getString(R.string.invalidDoubleTitle) + " " + getString(R.string.invalidDoubleMsg), Toast.LENGTH_SHORT).show()
            false
        }
    }
}
