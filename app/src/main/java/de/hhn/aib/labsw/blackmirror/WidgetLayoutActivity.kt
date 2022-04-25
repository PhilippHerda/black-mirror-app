package de.hhn.aib.labsw.blackmirror

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.iterator
import androidx.gridlayout.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.hhn.aib.labsw.blackmirror.adapter.MyRecyclerAdapter
import de.hhn.aib.labsw.blackmirror.helper.MyItemTouchHelperCallback
import de.hhn.aib.labsw.blackmirror.helper.OnStartDragListener
import java.lang.reflect.Array
import java.security.AccessController.getContext


class WidgetLayoutActivity : AppCompatActivity() {

    lateinit var myViews: Array
    lateinit var myGridLayout: GridLayout

    lateinit var itemTouchHelper: ItemTouchHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_layout)
        init()
        placeWidgetItems()
    }

    private fun placeWidgetItems() {
        val widgets: MutableList<String?> = ArrayList()
        widgets.addAll(listOf("Wetter", "Kalendar"))
        val adapter = MyRecyclerAdapter(this, widgets, object : OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
                itemTouchHelper.startDrag(viewHolder!!)
            }
        })
        val widgetRecyclerView = findViewById<RecyclerView>(R.id.widgetRecyclerView)
        widgetRecyclerView.adapter = adapter
        val callback = MyItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(widgetRecyclerView)
    }

    private fun init() {
        val widgetRecyclerView = findViewById<RecyclerView>(R.id.widgetRecyclerView)
        val layoutManagerWidgets = GridLayoutManager(this, 4)
        widgetRecyclerView.setHasFixedSize(true)
        widgetRecyclerView.layoutManager = layoutManagerWidgets

        myGridLayout = findViewById<GridLayout>(R.id.widgetGrid)
        for(box in myGridLayout) {
        }
    }
}