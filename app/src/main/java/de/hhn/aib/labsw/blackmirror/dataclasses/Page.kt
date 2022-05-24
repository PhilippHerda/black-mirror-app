package de.hhn.aib.labsw.blackmirror.dataclasses

/**
 * This class represents the pages on the mirror.
 *
 * @author Niklas Binder
 * @version 24-05-2022
 */
class Page {
    var widgets: ArrayList<Widget> = ArrayList()

    fun addWidget(widget: Widget) {
        widgets.add(widget)
    }
}
