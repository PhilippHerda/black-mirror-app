package de.hhn.aib.labsw.blackmirror.dataclasses

/**
 * This class represents the pages on the mirror.
 *
 * @author Niklas Binder
 * @version 24-05-2022
 */
data class Page(val widgets : ArrayList<Widget>) {

    fun addWidget(widget: Widget) {
        widgets.add(widget)
    }
}
