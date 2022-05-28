package de.hhn.aib.labsw.blackmirror.dataclasses

import java.io.Serializable
import java.util.ArrayList

/**
 * This class represents the mirror.
 *
 * @author Niklas Binder
 * @version 24-05-2022
 */
data class Mirror(val pages : ArrayList<Page>) : Serializable {
    var currentPageIndex = 0

    fun goToPreviousPage(){
        if (currentPageIndex - 1 < 0) {
            currentPageIndex = pages.size - 1
        } else {
            currentPageIndex--
        }
    }

    fun goToNextPage(){
        if (currentPageIndex + 1 > pages.size - 1) {
            currentPageIndex = 0
        } else {
            currentPageIndex++
        }
    }
}