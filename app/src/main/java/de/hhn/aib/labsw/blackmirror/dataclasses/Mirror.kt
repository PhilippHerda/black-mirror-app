package de.hhn.aib.labsw.blackmirror.dataclasses

import java.io.Serializable
import java.util.ArrayList

/**
 * This class represents the mirror.
 *
 * @author Niklas Binder, Selim Özdemir
 * @version 09-06-2022
 */
data class Mirror(val pages : ArrayList<Page>) : Serializable {
    var currentPageIndex = 0

    /**
     * Method to navigate to the previous page in the mirror model.
     */
    fun goToPreviousPage(){
        if (currentPageIndex - 1 < 0) {
            currentPageIndex = pages.size - 1
        } else {
            currentPageIndex--
        }
    }

    /**
     * Method to navigate to the previous page in the mirror model.
     */
    fun goToNextPage(){
        if (currentPageIndex + 1 > pages.size - 1) {
            currentPageIndex = 0
        } else {
            currentPageIndex++
        }
    }

    /**
     * Method to swap two pages in the mirror model by two indexes.
     *
     * @param index     index of the first page to be swapped.
     * @param target    index of the second page to be swapped.
     */
    fun swapPages(index: Int, target: Int){
        val firstPage: Page = pages[index]
        pages[index] = pages[target]
        pages[target] = firstPage
    }

    /**
     * Method to remove a page by the given index.
     *
     * @param index index of the page to be removed.
     */
    fun removePage(index: Int) {
        pages.removeAt(index)
    }
}