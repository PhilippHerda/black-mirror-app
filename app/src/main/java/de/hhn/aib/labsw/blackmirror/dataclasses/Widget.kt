package de.hhn.aib.labsw.blackmirror.dataclasses

class Widget(type: WidgetType, x: Int, y: Int) {
    private var xVal: Int = x
    private var yVal: Int = y
    private var widgetType: WidgetType = type
}
