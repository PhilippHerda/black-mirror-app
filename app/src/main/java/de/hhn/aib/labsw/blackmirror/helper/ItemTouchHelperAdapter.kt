package de.hhn.aib.labsw.blackmirror.helper

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPos: Int, toPos: Int)

    fun onItemDismiss(pos: Int)
}