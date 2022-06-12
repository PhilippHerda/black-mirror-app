package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hhn.aib.labsw.blackmirror.adapter.MyRecyclerAdapter
import de.hhn.aib.labsw.blackmirror.dataclasses.Mirror
import de.hhn.aib.labsw.blackmirror.dataclasses.Page
import de.hhn.aib.labsw.blackmirror.dataclasses.Widget
import de.hhn.aib.labsw.blackmirror.dataclasses.WidgetType
import de.hhn.aib.labsw.blackmirror.helper.MyItemTouchHelperCallback
import de.hhn.aib.labsw.blackmirror.helper.OnStartDragListener
import kotlin.collections.ArrayList

/**
 * This activity is used for
 *
 * @author Selim Özdemir
 * @version 12-06-2022
 */
class PagesActivity : AppCompatActivity() {

    var itemTouchHelper: ItemTouchHelper? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var myMirror: Mirror

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pages)
        init()
        generateItem()
    }

    private fun generateItem() {
        myMirror = intent.getSerializableExtra("myMirror") as Mirror
        val adapter = MyRecyclerAdapter(this, recyclerView, object : OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
                itemTouchHelper!!.startDrag(viewHolder!!)
            }
        }, myMirror)
        recyclerView.adapter = adapter
        val callback = MyItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper!!.attachToRecyclerView(recyclerView)

    }

    private fun init() {
        recyclerView = findViewById(R.id.pagesRecyclerView)
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager

        val actionButton = findViewById<FloatingActionButton>(R.id.addPageActionButton)
        actionButton.setOnClickListener {
            myMirror.pages.add(Page(ArrayList()))
            recyclerView.adapter?.notifyItemInserted(myMirror.pages.size - 1)
        }

        val saveButton = findViewById<MaterialButton>(R.id.saveButton)
        saveButton.setOnClickListener {
            savePages()
            sendPagesToMirror()
        }

        val exitButton = findViewById<MaterialButton>(R.id.exitButton)
        exitButton.setOnClickListener {
            intent = Intent(this, WidgetLayoutActivity::class.java)
            intent.putExtra("myMirror", myMirror)
            startActivity(intent)
        }
    }

    fun setMirror(mirror: Mirror) {
        myMirror = mirror
    }

    private fun sendPagesToMirror() {
        TODO("Not yet implemented")
    }

    private fun savePages() {
        for (item in recyclerView) {
            item as CardView
            val layout = item[0] as RelativeLayout
            val grid = layout[0] as androidx.gridlayout.widget.GridLayout
            val widgets = ArrayList<Widget>()
            val page = Page(widgets)
            var pos = 1
            for (box in grid) {
                if (box.foreground != null) {
                    when (box.foreground.constantState) {
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.mail_widget_icon_foreground
                        )?.constantState -> {
                            page.widgets.add(Widget(WidgetType.MAIL, pos % 3, pos / 3 + 1))
                        }
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.calendar_widget_icon_foreground
                        )?.constantState -> {
                            page.widgets.add(Widget(WidgetType.CALENDAR, pos % 3, pos / 3 + 1))
                        }
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.weather_widget_icon_foreground
                        )?.constantState -> {
                            page.widgets.add(Widget(WidgetType.WEATHER, pos % 3, pos / 3 + 1))
                        }
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.clock_widget_icon_foreground
                        )?.constantState -> {
                            page.widgets.add(Widget(WidgetType.CLOCK, pos % 3, pos / 3 + 1))
                        }
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.reminder_widget_icon_foreground
                        )?.constantState -> {
                            page.widgets.add(Widget(WidgetType.REMINDER, pos % 3, pos / 3 + 1))
                        }
                    }
                }
                pos++
            }
            myMirror.pages[recyclerView.indexOfChild(item)] = page
        }
    }
}