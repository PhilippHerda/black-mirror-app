package de.hhn.aib.labsw.blackmirror.listener

import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder

import android.content.ClipData
import android.os.Build
import androidx.annotation.RequiresApi


class MyOnTouchListener : View.OnTouchListener {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return if (event!!.action == MotionEvent.ACTION_DOWN) {
            val data = ClipData.newPlainText("", "")
            val shadowBuilder = DragShadowBuilder(v)
            v!!.startDragAndDrop(data, shadowBuilder, v, 0)
            v.visibility = View.INVISIBLE
            true
        } else {
            false
        }
    }
}