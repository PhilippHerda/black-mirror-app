package de.hhn.aib.labsw.blackmirror.dataclasses

import java.io.Serializable

class MyPage: Serializable{
    var widgets: ArrayList<Widget> = ArrayList<Widget>()

    fun addWidget(widget: Widget) {
        widgets.add(widget)
    }
}
