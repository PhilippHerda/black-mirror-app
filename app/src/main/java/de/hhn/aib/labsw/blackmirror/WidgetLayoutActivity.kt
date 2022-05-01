package de.hhn.aib.labsw.blackmirror

import android.content.ClipData
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.iterator
import androidx.gridlayout.widget.GridLayout
import android.view.ViewGroup
import androidx.annotation.RequiresApi


class WidgetLayoutActivity : AppCompatActivity() {

    private val widgets: MutableList<String?> = ArrayList()
    lateinit var myGridLayout: GridLayout
    lateinit var widgetList: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_layout)
        init()
        placeWidgetItems()
    }

    private fun placeWidgetItems() {
        widgets.addAll(listOf("Wetter", "Kalendar"))
        widgetList = findViewById(R.id.widgetList)

        for (i in 0 until widgets.size) {
            val widget = ImageView(this)
            when (widgets[i]) {
                "Wetter" -> widget.background = AppCompatResources.getDrawable(this, R.drawable.box)
                "Kalendar" -> widget.background =
                    AppCompatResources.getDrawable(this, R.drawable.box)
            }
            widget.setOnTouchListener(MyOnTouchListener())
            widgetList.addView(widget, i)
        }
    }

    private fun init() {
        myGridLayout = findViewById(R.id.widgetGrid)
        for (box in myGridLayout) {
            box.setOnDragListener(MyDragListener())
        }
    }

    inner class MyDragListener : View.OnDragListener {

        override fun onDrag(v: View?, event: DragEvent?): Boolean {
            when (event!!.action) {
                DragEvent.ACTION_DRAG_STARTED -> {}
                DragEvent.ACTION_DRAG_ENTERED -> {}
                DragEvent.ACTION_DRAG_EXITED -> {}
                DragEvent.ACTION_DROP
                -> {
                    val view = event?.localState as View

                    when (view.background) {
                        AppCompatResources.getDrawable(this@WidgetLayoutActivity, R.drawable.box) -> view.background =
                            AppCompatResources.getDrawable(this@WidgetLayoutActivity, R.drawable.box)
                        AppCompatResources.getDrawable(this@WidgetLayoutActivity, R.drawable.box) -> view.background =
                            AppCompatResources.getDrawable(this@WidgetLayoutActivity, R.drawable.box)
                    }
                    v!!.background = AppCompatResources.getDrawable(this@WidgetLayoutActivity, R.drawable.selected_box)

                }
                else -> {}
            }
            return true
        }

        fun checkIfWidgetOnBox(dragEvent: DragEvent): Boolean {
           return true
        }
    }

    inner class MyOnTouchListener : View.OnTouchListener {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            return if (event!!.action == MotionEvent.ACTION_DOWN) {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(v)
                v!!.startDragAndDrop(data, shadowBuilder, v, 0)
                true
            } else {
                false
            }
        }
    }
}