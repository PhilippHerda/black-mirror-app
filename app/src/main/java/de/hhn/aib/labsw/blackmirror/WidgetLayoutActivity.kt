package de.hhn.aib.labsw.blackmirror

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.iterator
import androidx.core.view.marginRight
import androidx.gridlayout.widget.GridLayout
import de.hhn.aib.labsw.blackmirror.listener.MyDragListener
import de.hhn.aib.labsw.blackmirror.listener.MyOnTouchListener
import java.lang.reflect.Array
import android.view.ViewGroup





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

        for(i in 0 until widgets.size) {
            val widget = ImageView(this)
            when (widgets[i]) {
                "Wetter" -> widget.background = AppCompatResources.getDrawable(this, R.drawable.box)
                "Kalendar" -> widget.background = AppCompatResources.getDrawable(this, R.drawable.box)
            }
            widget.setOnTouchListener(MyOnTouchListener())
            widgetList.addView(widget, i)
        }
    }

    private fun init() {
        myGridLayout = findViewById(R.id.widgetGrid)
        for(box in myGridLayout) {
            box.setOnDragListener(MyDragListener())
        }
    }
}