package de.hhn.aib.labsw.blackmirror


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.app.AlertDialog
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
import de.hhn.aib.labsw.blackmirror.dataclasses.*


/**
 * This activity initializes an interface where different widgets can be dragged on a
 * simulated mirror. These layouts can be saved.
 * This allows customizable mirror layouts.
 *
 * @author Selim Özdemir, Niklas Binder
 * @version 24-05-2022
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
     * Initializes the grid functionality and the onClick funcionality for the widget configuration.
     *
     * @Team Add your intents at the commented out lines.
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        myGridLayout = findViewById(R.id.widgetGrid)
        for (box in myGridLayout) {
            box.setOnClickListener {
                if (box.foreground != null) {
                    intent = null
                    when (box.foreground.constantState) {
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.mail_widget_icon_foreground
                        )?.constantState -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) mail configuration
                        }
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.calendar_widget_icon_foreground
                        )?.constantState -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) calendar configuration
                        }
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.weather_widget_icon_foreground
                        )?.constantState -> {
                            intent = Intent(
                                this@WidgetLayoutActivity,
                                WeatherLocationActivity::class.java
                            )
                        }
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.clock_widget_icon_foreground
                        )?.constantState -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) clock configuration
                        }
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.reminder_widget_icon_foreground
                        )?.constantState -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) reminder configuration
                        }
                    }
                    if(intent == null) {
                        Toast.makeText(applicationContext, "There is no configuration available for this widget!", Toast.LENGTH_SHORT).show()
                    } else {
                        startActivity(intent)
                    }
                }
            }

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

            box.setOnDragListener(MyDragListener())
        }

        val saveButton: MaterialButton = findViewById(R.id.saveButton)
        saveButton.setOnClickListener {
            sendConfiguration()
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

    /**
     * Method to send the current configuration to the mirror.
     */
    private fun sendConfiguration() {
        saveCurrentPage()
        //TODO: Send the data to the mirror.
        val myToast =
            Toast.makeText(applicationContext, "Successfully saved!", Toast.LENGTH_SHORT)
        myToast.show()
    }

    /**
     * Method to save the current page.
     * Gets called everytime the page gets switched.
     */
    private fun saveCurrentPage() {
        val page = Page()
        var pos = 1
        for (box in myGridLayout) {
            if (box.foreground != null) {
                when (box.foreground.constantState) {
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.mail_widget_icon_foreground
                    )?.constantState -> {
                        page.addWidget(Widget(WidgetType.MAIL, pos % 3, pos / 3 + 1))
                    }
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.calendar_widget_icon_foreground
                    )?.constantState -> {
                        page.addWidget(Widget(WidgetType.CALENDAR, pos % 3, pos / 3 + 1))
                    }
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.weather_widget_icon_foreground
                    )?.constantState -> {
                        page.addWidget(Widget(WidgetType.WEATHER, pos % 3, pos / 3 + 1))
                    }
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.clock_widget_icon_foreground
                    )?.constantState -> {
                        page.addWidget(Widget(WidgetType.CLOCK, pos % 3, pos / 3 + 1))
                    }
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.reminder_widget_icon_foreground
                    )?.constantState -> {
                        page.addWidget(Widget(WidgetType.REMINDER, pos % 3, pos / 3 + 1))
                    }
                }
            }
            pos++
        }
        myMirror.replaceCurrentPage(page)
    }

    /**
     * Method to load all widgets from the myMirror object and display it on the grid.
     */
    private fun loadCurrentPage() {
        println("Current Page: " + myMirror.getPageIndex())
        val allWidgets: ArrayList<Widget> = myMirror.getCurrentPage().widgets
        for (widget in allWidgets) {
            val pos: Int = (widget.getX() - 1) + (widget.getY() - 1) * 3
            myGridLayout[pos].foreground = getDrawableForWidget(widget)
            myGridLayout[pos].background = AppCompatResources.getDrawable(this, R.drawable.widget_box)
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

    /**
     * Method to clear the grid.
     * Gets called everytime the page gets changed.
     */
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

