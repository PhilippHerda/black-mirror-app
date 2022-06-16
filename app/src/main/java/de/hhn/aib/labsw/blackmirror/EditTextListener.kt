package de.hhn.aib.labsw.blackmirror

import android.view.View
import android.widget.EditText

/**
 * Listener wrapper that remembers the previous text and allows
 * for the callback to reject the changes.
 *
 * @author Markus Marewitz
 * @version 2022-04-18
 */
abstract class EditTextListener(
    private val editText: EditText
) : View.OnFocusChangeListener {
    private var prevText: String = editText.text.toString()

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (!hasFocus) {
            if (!onTextEdited(editText.text.toString()))
                editText.setText(prevText)
        }
    }

    abstract fun onTextEdited(newText: String): Boolean
}
