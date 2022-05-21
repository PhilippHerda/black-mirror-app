package de.hhn.aib.labsw.blackmirror

import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.iterator
import androidx.gridlayout.widget.GridLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hhn.aib.labsw.blackmirror.dataclasses.MyPage
import de.hhn.aib.labsw.blackmirror.dataclasses.Widget

/**
 * This activity initializes an interface where different widgets can be dragged on a
 * simulated mirror. These layouts can be saved.
 * This allows customizable mirror layouts.
 *
 * @author Selim Özdemir, Niklas Binder
 * @version 12-05-2022
 */
class WidgetLayoutActivity : AppCompatActivity() {

    private val widgets: MutableList<String?> = ArrayList()
    lateinit var myGridLayout: GridLayout
    private lateinit var widgetList: LinearLayout
    private lateinit var savedPage: MyPage

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_layout)
        init()
        placeWidgetItems()
    }

    /**
     * This function places boxes on a scrollbar which represent the different
     * widgets shown on the mirror. These are draggable.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun placeWidgetItems() {
        widgets.addAll(listOf("weather", "calendar", "clock", "mail", "reminder"))
        widgetList = findViewById(R.id.widgetList)

        for (i in 0 until widgets.size) {
            val widget = ImageView(this)
            when (widgets[i]) {
                "weather" -> {
                    widget.background = AppCompatResources.getDrawable(this, R.drawable.widget_box)
                    widget.foreground = AppCompatResources.getDrawable(
                        this,
                        R.drawable.weather_widget_icon_foreground
                    )
                }
                "calendar" -> {
                    widget.background =
                        AppCompatResources.getDrawable(this, R.drawable.widget_box)
                    widget.foreground = AppCompatResources.getDrawable(
                        this,
                        R.drawable.calendar_widget_icon_foreground
                    )
                }
                "clock" -> {
                    widget.background =
                        AppCompatResources.getDrawable(this, R.drawable.widget_box)
                    widget.foreground = AppCompatResources.getDrawable(
                        this,
                        R.drawable.clock_widget_icon_foreground
                    )
                }
                "mail" -> {
                    widget.background =
                        AppCompatResources.getDrawable(this, R.drawable.widget_box)
                    widget.foreground = AppCompatResources.getDrawable(
                        this,
                        R.drawable.mail_widget_icon_foreground
                    )
                }
                "reminder" -> {
                    widget.background =
                        AppCompatResources.getDrawable(this, R.drawable.widget_box)
                    widget.foreground = AppCompatResources.getDrawable(
                        this,
                        R.drawable.reminder_widget_icon_foreground
                    )
                }
            }
            widget.setOnLongClickListener {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(widget)
                widget.startDragAndDrop(data, shadowBuilder, widget, 0)
            }
            widgetList.addView(widget, i)
        }
    }

    /**
     * Initializes the grid functionality and the buttons.
     */
    private fun init() {
        myGridLayout = findViewById(R.id.widgetGrid)
        for (box in myGridLayout) {
            box.setOnDragListener(MyDragListener())
            box.setOnLongClickListener {
                if (box.foreground != null) {
                    for (widget in widgetList) {
                        if (box.foreground == widget.foreground) {
                            widget.performLongClick()
                        }
                    }
                    box.background =
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.box
                        )
                    box.foreground = null
                }
                return@setOnLongClickListener true
            }
        }

        val saveButton: MaterialButton = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            val page = MyPage()
            var pos = 1
            for (box in myGridLayout) {
                if (box.foreground != null) {
                    when (box.foreground.constantState) {
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.mail_widget_icon_foreground
                        )?.constantState -> {
                            page.addWidget(Widget("mail", pos % 3, pos / 3 + 1))
                            println("saved")
                        }
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.calendar_widget_icon_foreground
                        )?.constantState -> {
                            page.addWidget(Widget("calendar", pos % 3, pos / 3 + 1))
                            println("saved")
                        }
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.weather_widget_icon_foreground
                        )?.constantState -> {
                            page.addWidget(Widget("weather", pos % 3, pos / 3 + 1))
                            println("saved")
                        }
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.clock_widget_icon_foreground
                        )?.constantState -> {
                            page.addWidget(Widget("clock", pos % 3, pos / 3 + 1))
                            println("saved")
                        }
                        AppCompatResources.getDrawable(
                            this,
                            R.drawable.reminder_widget_icon_foreground
                        )?.constantState -> {
                            page.addWidget(Widget("reminder", pos % 3, pos / 3 + 1))
                            println("saved")
                        }
                    }
                    println(box.foreground.constantState)
                }
                println(pos)
                pos++
            }
            savedPage = page
            val myToast =
                Toast.makeText(applicationContext, "Successfully saved!", Toast.LENGTH_SHORT)
            myToast.show()
        }

        val clearButton: MaterialButton = findViewById(R.id.clearButton)
        clearButton.setOnClickListener {
            val builder = AlertDialog.Builder(this@WidgetLayoutActivity)
            builder.setMessage(resources.getString(R.string.Str_widgetConfirmClearTxt))
                .setCancelable(false)
                .setPositiveButton(
                    resources.getString(R.string.Str_widgetConfirmClearYesTxt)
                ) { dialog, id -> clearWidgetGrid() }
                .setNegativeButton(
                    resources.getString(R.string.Str_widgetConfirmClearNoTxt)
                ) { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }

        val configPagesButton: MaterialButton = findViewById(R.id.configPagesButton)
        configPagesButton.setOnClickListener {
            saveCurrentPage()
            //    TODO: Create a PageActivity which uses the given MyPage extra
            //    intent = Intent(this, PagesActivity::class.java)
            //    intent.putExtra("newPage", savedPage)
            //    startActivity(intent)
        }

        val navigateLeftButton = findViewById<FloatingActionButton>(R.id.navigateLeft_fab)
        navigateLeftButton.setOnClickListener {
            saveCurrentPage()
            clearWidgetGrid()
        }

        val navigateRightButton = findViewById<FloatingActionButton>(R.id.navigateRight_fab)
        navigateRightButton.setOnClickListener {
            saveCurrentPage()
            clearWidgetGrid()
        }
    }

    private fun saveCurrentPage() {
        // TODO: Safe Positions of the widgets on the page
    }

    private fun clearWidgetGrid() {
        for (box in myGridLayout) {
            box.foreground = null
            box.background = AppCompatResources.getDrawable(this, R.drawable.box)
        }
    }

    /**
     * This listener is used by the cells in the gridlayout of the mirror. This gives
     * them the ability to store the widget data and change their appearance
     * accordingly.
     */
    inner class MyDragListener : View.OnDragListener {
        override fun onDrag(v: View?, event: DragEvent?): Boolean {
            v as ImageView
            when (event!!.action) {
                DragEvent.ACTION_DRAG_STARTED -> {}
                DragEvent.ACTION_DRAG_ENTERED -> {
                    v.background = AppCompatResources.getDrawable(
                        this@WidgetLayoutActivity,
                        R.drawable.selectable_box
                    )
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    if (v.foreground == null) {
                        v.background = AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.box
                        )
                    } else {
                        v.background = AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.selectable_box
                        )
                    }
                }
                DragEvent.ACTION_DROP
                -> {
                    val view = event.localState as ImageView
                    for (box in myGridLayout) {
                        if (box.foreground == view.foreground) {
                            box.background = AppCompatResources.getDrawable(
                                this@WidgetLayoutActivity,
                                R.drawable.box
                            )
                            box.foreground = null
                        }
                    }
                    v.background = view.background
                    v.foreground = view.foreground
                }
                else -> {}
            }
            return true
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@WidgetLayoutActivity)
        builder.setMessage(resources.getString(R.string.Str_widgetConfirmExitApplicationTxt))
            .setCancelable(false)
            .setPositiveButton(
                resources.getString(R.string.Str_widgetConfirmClearYesTxt)
            ) { dialog, id -> this@WidgetLayoutActivity.finishAffinity() }
            .setNegativeButton(
                resources.getString(R.string.Str_widgetConfirmClearNoTxt)
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }
}
