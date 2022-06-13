package de.hhn.aib.labsw.blackmirror.helper

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPos: Int, toPos: Int): Boolean

    fun onItemDismiss(pos: Int)
}