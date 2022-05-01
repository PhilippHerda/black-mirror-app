package de.hhn.aib.labsw.blackmirror.listener

import android.view.DragEvent
import android.view.View
import android.widget.LinearLayout
import android.view.ViewGroup
import android.R

import android.graphics.drawable.Drawable

import android.view.View.OnDragListener





class MyDragListener : View.OnDragListener {

    override fun onDrag(v: View?, event: DragEvent?): Boolean {
        when (event!!.action) {
            DragEvent.ACTION_DRAG_STARTED -> {}
            DragEvent.ACTION_DROP -> {
                val view = event?.localState as View
                val owner = view.parent as ViewGroup
                owner.removeView(view)
                val container = v as LinearLayout
                container.addView(view)
                view.visibility = View.VISIBLE
            }
            else -> {}
        }
        return true
    }

}