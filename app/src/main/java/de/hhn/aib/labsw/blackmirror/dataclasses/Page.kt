package de.hhn.aib.labsw.blackmirror.dataclasses

import java.io.Serializable

/**
 * This class represents the pages on the mirror.
 *
 * @author Niklas Binder
 * @version 28-05-2022
 */
data class Page(val widgets : ArrayList<Widget>) : Serializable
