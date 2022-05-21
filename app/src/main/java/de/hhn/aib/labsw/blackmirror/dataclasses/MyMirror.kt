package de.hhn.aib.labsw.blackmirror.dataclasses

import java.io.Serializable
import java.util.ArrayList

class MyMirror : Serializable {
    private var pages: ArrayList<Page> = ArrayList();
    private var currentPageIndex = 0;

    fun addPage(page: Page) {
        pages.add(page)
    }

    fun replaceCurrentPage(page: Page) {
        pages[currentPageIndex] = page
    }

    fun getCurrentPage(): Page {
        return pages[currentPageIndex]
    }

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