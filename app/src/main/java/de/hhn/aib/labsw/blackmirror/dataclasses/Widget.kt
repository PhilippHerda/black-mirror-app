package de.hhn.aib.labsw.blackmirror.dataclasses

/**
 * This class represents the widgets on the mirror.
 *
 * @author Selim Özdemir, Niklas Binder
 * @version 24-05-2022
 */
class Widget(type: WidgetType, x: Int, y: Int) {
    private var xVal: Int = x
    private var yVal: Int = y
    private var widgetType: WidgetType = type

    fun getX(): Int {
        return xVal
    }

    fun getY(): Int {
        return yVal
    }

    fun getWidgetType(): WidgetType {
        return widgetType
    }
}
