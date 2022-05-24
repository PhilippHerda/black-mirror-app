package de.hhn.aib.labsw.blackmirror.dataclasses


class Page {
    var widgets: ArrayList<Widget> = ArrayList()

    fun addWidget(widget: Widget) {
        widgets.add(widget)
    }
}
