package de.hhn.aib.labsw.blackmirror.dataclasses

import java.io.Serializable
import java.util.ArrayList

class MyMirror : Serializable {
    var pages: ArrayList<Page> = ArrayList();

    fun addPage(page: Page) {
        pages.add(page)
    }
}