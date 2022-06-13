package de.hhn.aib.labsw.blackmirror

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
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
 * This activity is used as a configuration page which allows the user to
 * change the order of pages, delete pages or add pages.
 *
 * @author Selim Özdemir
 * @version 12-06-2022
 */
class PagesActivity : AppCompatActivity() {
    var itemTouchHelper: ItemTouchHelper? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var unsavedMirror: Mirror
    private lateinit var myMirror: Mirror

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pages)
        init()
        generateItem()
    }

    /**
     * Generates the items in the recyclerview.
     */
    private fun generateItem() {
        unsavedMirror = intent.getSerializableExtra("myMirror") as Mirror
        myMirror = unsavedMirror
        val adapter = MyRecyclerAdapter(this, recyclerView, object : OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
                itemTouchHelper!!.startDrag(viewHolder!!)
            }
        }, unsavedMirror)
        recyclerView.adapter = adapter
        val callback = MyItemTouchHelperCallback(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper!!.attachToRecyclerView(recyclerView)
    }

    /**
     * Initiates all buttons and sets the layout of the recyclerview.
     */
    private fun init() {
        recyclerView = findViewById(R.id.pagesRecyclerView)
        recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager

        val actionButton = findViewById<FloatingActionButton>(R.id.addPageActionButton)
        actionButton.setOnClickListener {
            unsavedMirror.pages.add(Page(ArrayList()))
            recyclerView.adapter?.notifyItemInserted(unsavedMirror.pages.size - 1)
            resetAdapterState()
        }

        val saveButton = findViewById<MaterialButton>(R.id.saveButton)
        saveButton.setOnClickListener {
            savePages()
        }

        val exitButton = findViewById<MaterialButton>(R.id.exitButton)
        exitButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun resetAdapterState() {
        val myAdapter = recyclerView.adapter
        recyclerView.adapter = myAdapter
    }

    /**
     *
     */
    private fun sendPagesToMirror() {
        TODO("Not yet implemented")
    }

    /**
     * Reads all pages in the recyclerview and saves them as a mirror object.
     */
    private fun savePages() {
        val mirror = Mirror(ArrayList())
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
            mirror.pages.add(page)
        }
        if(mirror.pages.isNotEmpty()) {
            unsavedMirror = mirror
            myMirror = unsavedMirror
            val myToast =
                Toast.makeText(
                    applicationContext,
                    "Successfully saved!",
                    Toast.LENGTH_SHORT
                )
            myToast.show()
        } else {
            val myToast =
                Toast.makeText(
                    applicationContext,
                    "Cannot save an empty mirror!",
                    Toast.LENGTH_SHORT
                )
            myToast.show()
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@PagesActivity)
        builder.setMessage("Do you want to save the pages for exiting?")
            .setCancelable(false)
            .setPositiveButton(
                resources.getString(R.string.Str_widgetConfirmClearYesTxt)
            ) { _, _ ->
                savePages()
                intent = Intent(this, WidgetLayoutActivity::class.java)
                intent.putExtra("myMirror", myMirror)
                startActivity(intent)
                this@PagesActivity.finish() }
            .setNegativeButton(
                resources.getString(R.string.Str_widgetConfirmClearNoTxt)
            ) { _, _ ->
                intent = Intent(this, WidgetLayoutActivity::class.java)
                intent.putExtra("myMirror", myMirror)
                startActivity(intent)
                this@PagesActivity.finish() }
        val alert = builder.create()
        alert.show()
    }
}
