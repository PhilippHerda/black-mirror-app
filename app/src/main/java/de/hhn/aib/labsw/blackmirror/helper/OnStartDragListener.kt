package de.hhn.aib.labsw.blackmirror.helper

import androidx.recyclerview.widget.RecyclerView

/**
 * Custom interface for pages recyclerview adapter.
 */
interface OnStartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder?)
}