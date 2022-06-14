package de.hhn.aib.labsw.blackmirror


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.gridlayout.widget.GridLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hhn.aib.labsw.blackmirror.WidgetLayoutActivity.LayoutConstants.PAGE_UPDATE_TOPIC
import de.hhn.aib.labsw.blackmirror.dataclasses.Mirror
import de.hhn.aib.labsw.blackmirror.dataclasses.Page
import de.hhn.aib.labsw.blackmirror.dataclasses.Widget
import de.hhn.aib.labsw.blackmirror.dataclasses.WidgetType


/**
 * This activity initializes an interface where different widgets can be dragged on a
 * simulated mirror. These layouts can be saved.
 * This allows customizable mirror layouts.
 *
 * @author Selim Özdemir, Niklas Binder
 * @version 28-05-2022
 */
class WidgetLayoutActivity : AbstractActivity() {

    private val widgets: MutableList<String?> = ArrayList()
    lateinit var myGridLayout: GridLayout
    private lateinit var widgetList: LinearLayout
    private lateinit var mirror: Mirror

    object LayoutConstants {
        const val PAGE_UPDATE_TOPIC = "pageUpdate"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_layout)
        init()
        placeWidgetItems()

        // Initializing Mirror object
        val pages = ArrayList<Page>()
        val widgets = ArrayList<Widget>()
        pages.add(Page(widgets))
        pages.add(Page(widgets))
        pages.add(Page(widgets))
        mirror = Mirror(pages)
        findViewById<TextView>(R.id.pageIndicatorTextView).text = "1"
        findViewById<TextView>(R.id.pageAmountTextView).text = mirror.pages.size.toString()
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
                    widget.tag = WidgetType.WEATHER
                }
                "calendar" -> {
                    widget.background =
                        AppCompatResources.getDrawable(this, R.drawable.widget_box)
                    widget.foreground = AppCompatResources.getDrawable(
                        this,
                        R.drawable.calendar_widget_icon_foreground
                    )
                    widget.tag = WidgetType.CALENDAR
                }
                "clock" -> {
                    widget.background =
                        AppCompatResources.getDrawable(this, R.drawable.widget_box)
                    widget.foreground = AppCompatResources.getDrawable(
                        this,
                        R.drawable.clock_widget_icon_foreground
                    )
                    widget.tag = WidgetType.CLOCK
                }
                "mail" -> {
                    widget.background =
                        AppCompatResources.getDrawable(this, R.drawable.widget_box)
                    widget.foreground = AppCompatResources.getDrawable(
                        this,
                        R.drawable.mail_widget_icon_foreground
                    )
                    widget.tag = WidgetType.MAIL
                }
                "reminder" -> {
                    widget.background =
                        AppCompatResources.getDrawable(this, R.drawable.widget_box)
                    widget.foreground = AppCompatResources.getDrawable(
                        this,
                        R.drawable.reminder_widget_icon_foreground
                    )
                    widget.tag = WidgetType.REMINDER
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
     * Initializes the grid functionality and the onClick functionality for the widget configuration.
     *
     * @Team Add your intents at the commented out lines.
     */
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun init() {
        myGridLayout = findViewById(R.id.widgetGrid)
        for (box in myGridLayout) {
            box.setOnClickListener {
                if (box.foreground != null) {
                    when (box.tag) {
                        WidgetType.CALENDAR -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) calendar configuration
                        }
                        WidgetType.CLOCK -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) clock configuration
                        }
                        WidgetType.MAIL -> {
                            startActivity(
                                Intent(
                                    this@WidgetLayoutActivity,
                                    EmailDataActivity::class.java
                                )
                            )
                        }
                        WidgetType.WEATHER -> {
                            startActivity(
                                Intent(
                                    this@WidgetLayoutActivity,
                                    WeatherLocationActivity::class.java
                                )
                            )
                        }
                        WidgetType.REMINDER -> {
                            startActivity(
                                Intent(
                                    this@WidgetLayoutActivity,
                                    TodoListActivity::class.java
                                )
                            )

                        }
                        else -> {
                            Toast.makeText(
                                applicationContext,
                                R.string.Str_widgetNoConfigAvailableToastMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            box.setOnLongClickListener {
                if (box.foreground != null) {
                    for (widget in widgetList) {
                        if (box.tag == widget.tag) {
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
            sendWidgetLayout()
        }

        val clearButton: MaterialButton = findViewById(R.id.clearButton)
        clearButton.setOnClickListener {
            val builder = AlertDialog.Builder(this@WidgetLayoutActivity)
            builder.setMessage(resources.getString(R.string.Str_widgetConfirmClearTxt))
                .setCancelable(false)
                .setPositiveButton(
                    resources.getString(R.string.Str_widgetConfirmClearYesTxt)
                ) { dialog, id -> deleteConfiguration() }
                .setNegativeButton(
                    resources.getString(R.string.Str_widgetConfirmClearNoTxt)
                ) { dialog, id -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }

        val configPagesButton: MaterialButton = findViewById(R.id.configPagesButton)
        configPagesButton.setOnClickListener {
            startActivity(Intent(this@WidgetLayoutActivity, PagesActivity::class.java))
        }

        val navigateLeftButton = findViewById<FloatingActionButton>(R.id.navigateLeft_fab)
        navigateLeftButton.setOnClickListener {
            mirror.goToPreviousPage()
            clearWidgetGrid()
            displayPage()
            findViewById<TextView>(R.id.pageIndicatorTextView).text =
                (mirror.currentPageIndex + 1).toString()
        }

        val navigateRightButton = findViewById<FloatingActionButton>(R.id.navigateRight_fab)
        navigateRightButton.setOnClickListener {
            mirror.goToNextPage()
            clearWidgetGrid()
            displayPage()
            findViewById<TextView>(R.id.pageIndicatorTextView).text =
                (mirror.currentPageIndex + 1).toString()
        }
    }

    /**
     * Method to send the current layout to the mirror.
     */
    private fun sendWidgetLayout() {
        publishToRemotes(PAGE_UPDATE_TOPIC, mirror)
        val myToast =
            Toast.makeText(
                applicationContext,
                R.string.Str_widgetSuccessfullySavedToastMessage,
                Toast.LENGTH_SHORT
            )
        myToast.show()
    }

    /**
     * Method to save the current page.
     * Gets called everytime the page gets switched.
     */
    private fun saveCurrentPage() {
        val widgets = ArrayList<Widget>()
        val page = Page(widgets)
        var pos = 1
        for (box in myGridLayout) {
            if (box.foreground != null) {
                when (box.tag) {
                    WidgetType.MAIL -> {
                        setPosition(pos, WidgetType.MAIL, page)
                    }
                    WidgetType.CALENDAR -> {
                        setPosition(pos, WidgetType.CALENDAR, page)
                    }
                    WidgetType.WEATHER -> {
                        setPosition(pos, WidgetType.WEATHER, page)
                    }
                    WidgetType.CLOCK -> {
                        setPosition(pos, WidgetType.CLOCK, page)
                    }
                    WidgetType.REMINDER -> {
                        setPosition(pos, WidgetType.REMINDER, page)
                    }
                }
            }
            pos++
        }
        mirror.pages[mirror.currentPageIndex] = page
    }

    private fun setPosition(pos: Int, type: WidgetType, page: Page) {
        if (pos % 3 == 0) {
            page.widgets.add(Widget(type, 3, pos / 3))
        } else {
            page.widgets.add(Widget(type, pos % 3, pos / 3 + 1))
        }
    }

    /**
     * Method to load all widgets from the myMirror object and display it on the grid.
     */
    private fun displayPage() {
        for (widget in mirror.pages[mirror.currentPageIndex].widgets) {
            val pos: Int = (widget.x - 1) + (widget.y - 1) * 3
            myGridLayout[pos].foreground = getDrawableForWidget(widget)
            myGridLayout[pos].background =
                AppCompatResources.getDrawable(this, R.drawable.widget_box)
            myGridLayout[pos].tag = widget.type
        }
    }

    private fun getDrawableForWidget(widget: Widget): Drawable {
        return when (widget.type) {
            WidgetType.CALENDAR -> {
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.calendar_widget_icon_foreground
                )!!
            }
            WidgetType.CLOCK -> {
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.clock_widget_icon_foreground
                )!!
            }
            WidgetType.MAIL -> {
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.mail_widget_icon_foreground
                )!!
            }
            WidgetType.REMINDER -> {
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.reminder_widget_icon_foreground
                )!!
            }
            WidgetType.WEATHER -> {
                AppCompatResources.getDrawable(
                    this,
                    R.drawable.weather_widget_icon_foreground
                )!!
            }
        }
    }

    private fun deleteConfiguration() {
        clearWidgetGrid()
        saveCurrentPage()
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
                DragEvent.ACTION_DRAG_STARTED -> {
                }
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
                        if (box.tag == view.tag) {
                            box.background = AppCompatResources.getDrawable(
                                this@WidgetLayoutActivity,
                                R.drawable.box
                            )
                            box.foreground = null
                        }
                    }
                    v.background = view.background
                    v.foreground = view.foreground
                    v.tag = view.tag
                    saveCurrentPage()
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

