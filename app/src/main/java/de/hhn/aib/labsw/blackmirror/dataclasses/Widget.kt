package de.hhn.aib.labsw.blackmirror.dataclasses

import java.io.Serializable

/**
 * This class represents the widgets on the mirror.
 *
 * @author Selim Özdemir, Niklas Binder
 * @version 24-05-2022
 */
data class Widget(val type: WidgetType, val x: Int, val y: Int) : Serializable