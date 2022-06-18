package de.hhn.aib.labsw.blackmirror.helper

/**
 * Interface for custom recyclerview adapter.
 */
interface ItemTouchHelperAdapter {
    fun onItemMove(fromPos: Int, toPos: Int): Boolean

    fun onItemDismiss(pos: Int)
}