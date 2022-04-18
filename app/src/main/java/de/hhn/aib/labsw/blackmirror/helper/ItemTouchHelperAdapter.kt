package de.hhn.aib.labsw.blackmirror.helper

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition:Int)
    fun onItemDismiss(position: Int)
}