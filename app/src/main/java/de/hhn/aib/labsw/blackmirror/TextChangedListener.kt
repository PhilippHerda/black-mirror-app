package de.hhn.aib.labsw.blackmirror

import android.text.Editable
import android.text.TextWatcher

/**
 * Implementation from:
 * https://stackoverflow.com/questions/11134144/android-edittext-onchange-listener
 *
 * @author Markus Marewitz
 * @version 2022-04-17
 */
abstract class TextChangeListener<T>(val t: T) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        if (s != null) onTextChanged(t, s)
    }

    abstract fun onTextChanged(textView: T, s: Editable)
}
