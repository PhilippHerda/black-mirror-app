package de.hhn.aib.labsw.blackmirror

import android.app.AlertDialog
import android.content.ClipData
import android.graphics.drawable.Drawable
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
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.gridlayout.widget.GridLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hhn.aib.labsw.blackmirror.dataclasses.MyMirror
import de.hhn.aib.labsw.blackmirror.dataclasses.Page
import de.hhn.aib.labsw.blackmirror.dataclasses.Widget
import de.hhn.aib.labsw.blackmirror.dataclasses.WidgetType

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
    private lateinit var myMirror: MyMirror

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_layout)
        init()
        placeWidgetItems()
        myMirror = MyMirror()
        myMirror.addPage(Page())
        myMirror.addPage(Page())
        myMirror.addPage(Page())
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
            saveConfiguration()
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
            //    TODO: Create a PageActivity which uses the given MyMirror
            //    intent = Intent(this, PagesActivity::class.java)
            //    intent.putExtra("newPage", savedPage)
            //    startActivity(intent)
        }

        val navigateLeftButton = findViewById<FloatingActionButton>(R.id.navigateLeft_fab)
        navigateLeftButton.setOnClickListener {
            saveCurrentPage()
            myMirror.goToPreviousPage()
            clearWidgetGrid()
            loadCurrentPage()
        }

        val navigateRightButton = findViewById<FloatingActionButton>(R.id.navigateRight_fab)
        navigateRightButton.setOnClickListener {
            saveCurrentPage()
            myMirror.goToNextPage()
            clearWidgetGrid()
            loadCurrentPage()
        }
    }

    private fun saveConfiguration() {
        // TODO: Send Serializable MyMirror object to the blackmirror
        val myToast =
            Toast.makeText(applicationContext, "Successfully saved!", Toast.LENGTH_SHORT)
        myToast.show()
    }

    private fun saveCurrentPage() {
        val page = Page()
        var pos = 1
        for (box in myGridLayout) {
            when (box.foreground) {
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.mail_widget_icon_foreground
                ) -> {
                    page.addWidget(Widget(WidgetType.MAIL, pos % 3, pos / 3 + 1))
                }
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.calendar_widget_icon_foreground
                ) -> {
                    page.addWidget(Widget(WidgetType.CALENDAR, pos % 3, pos / 3 + 1))
                }
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.weather_widget_icon_foreground
                ) -> {
                    page.addWidget(Widget(WidgetType.WEATHER, pos % 3, pos / 3 + 1))
                }
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.clock_widget_icon_foreground
                ) -> {
                    page.addWidget(Widget(WidgetType.CLOCK, pos % 3, pos / 3 + 1))
                }
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.reminder_widget_icon_foreground
                ) -> {
                    page.addWidget(Widget(WidgetType.REMINDER, pos % 3, pos / 3 + 1))
                }
            }
            pos++
        }
        myMirror.replaceCurrentPage(page)
    }

    private fun loadCurrentPage() {
        val allWidgets: ArrayList<Widget> = myMirror.getCurrentPage().widgets
        for (widget in allWidgets) {
            val pos: Int = widget.getX() + widget.getY() * 3
            myGridLayout.get(pos).foreground = getDrawableForWidget(widget)
        }
    }

    private fun getDrawableForWidget(widget: Widget): Drawable {
        lateinit var drawable: Drawable
        when {
            widget.getWidgetType() == WidgetType.CALENDAR -> {
                drawable = AppCompatResources.getDrawable(
                    this,
                    R.drawable.calendar_widget_icon_foreground
                )!!
            }
            widget.getWidgetType() == WidgetType.CLOCK -> {
                drawable = AppCompatResources.getDrawable(
                    this,
                    R.drawable.clock_widget_icon_foreground
                )!!
            }
            widget.getWidgetType() == WidgetType.MAIL -> {
                drawable = AppCompatResources.getDrawable(
                    this,
                    R.drawable.mail_widget_icon_foreground
                )!!
            }
            widget.getWidgetType() == WidgetType.REMINDER -> {
                drawable = AppCompatResources.getDrawable(
                    this,
                    R.drawable.reminder_widget_icon_foreground
                )!!
            }
            widget.getWidgetType() == WidgetType.WEATHER -> {
                drawable = AppCompatResources.getDrawable(
                    this,
                    R.drawable.weather_widget_icon_foreground
                )!!
            }
        }
        return drawable;
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
