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
import android.widget.TextView
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
 * @version 09-06-2022
 */
class WidgetLayoutActivity : AppCompatActivity() {

    private val widgets: MutableList<String?> = ArrayList()
    lateinit var myGridLayout: GridLayout
    private lateinit var widgetList: LinearLayout
    private lateinit var mirror: Mirror


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget_layout)
        init()
        placeWidgetItems()

        findViewById<TextView>(R.id.pageIndicatorTextView).text = "1"
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
     * Initializes the grid functionality and the onClick functionality for the widget configuration.
     *
     * @Team Add your intents at the commented out lines.
     */
    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    private fun init() {
        myGridLayout = findViewById(R.id.PageItemGrid)
        for (box in myGridLayout) {
            box.setOnClickListener {
                if (box.foreground != null) {

                    println("test2 " + box.foreground.constantState.toString())
                    println(
                        "test2 " + AppCompatResources.getDrawable(
                            this,
                            R.drawable.weather_widget_icon_foreground
                        )?.constantState.toString()
                    )

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
                    if (intent == null) {
                        Toast.makeText(
                            applicationContext,
                            R.string.Str_widgetNoConfigAvailableToastMessage,
                            Toast.LENGTH_SHORT
                        ).show()
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
            sendWidgetLayout()
        }

        val clearButton: MaterialButton = findViewById(R.id.clearButton)
        clearButton.setOnClickListener {
            val builder = AlertDialog.Builder(this@WidgetLayoutActivity)
            builder.setMessage(resources.getString(R.string.Str_widgetConfirmClearTxt))
                .setCancelable(false)
                .setPositiveButton(
                    resources.getString(R.string.Str_widgetConfirmClearYesTxt)
                ) { _, _ -> clearWidgetGrid() }
                .setNegativeButton(
                    resources.getString(R.string.Str_widgetConfirmClearNoTxt)
                ) { dialog, _ -> dialog.cancel() }
            val alert = builder.create()
            alert.show()
        }

        val configPagesButton: MaterialButton = findViewById(R.id.configPagesButton)
        configPagesButton.setOnClickListener {
            val intent = Intent(this, PagesActivity::class.java)
            intent.putExtra("myMirror", mirror)
            startActivity(intent)
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
        //TODO: Send the data to the mirror.
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

                println("test1 " + box.foreground.constantState.toString())
                println(
                    "test1 " + AppCompatResources.getDrawable(
                        this,
                        R.drawable.weather_widget_icon_foreground
                    )?.constantState.toString()
                )

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
        mirror.pages[mirror.currentPageIndex] = page
    }

    /**
     * Method to load all widgets from the myMirror object and display it on the grid.
     */
    private fun displayPage() {
        println("Current Page: " + mirror.currentPageIndex)
        for (widget in mirror.pages[mirror.currentPageIndex].widgets) {
            val pos: Int = (widget.x - 1) + (widget.y - 1) * 3
            myGridLayout[pos].foreground = getDrawableForWidget(widget)
            myGridLayout[pos].background =
                AppCompatResources.getDrawable(this, R.drawable.widget_box)
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
                    saveCurrentPage()
                }
                else -> {}
            }
            return true
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onResume() {
        super.onResume()
        mirror = if (intent.hasExtra("myMirror")) {
            intent.getSerializableExtra("myMirror") as Mirror
        } else {
            val pages = ArrayList<Page>()
            val widgets = ArrayList<Widget>()
            pages.add(Page(widgets))
            pages.add(Page(widgets))
            pages.add(Page(widgets))
            Mirror(pages)
        }

        findViewById<TextView>(R.id.pageAmountTextView).text = mirror.pages.size.toString()
        clearWidgetGrid()
        displayPage()
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this@WidgetLayoutActivity)
        builder.setMessage(resources.getString(R.string.Str_widgetConfirmExitApplicationTxt))
            .setCancelable(false)
            .setPositiveButton(
                resources.getString(R.string.Str_widgetConfirmClearYesTxt)
            ) { _, _ -> this@WidgetLayoutActivity.finishAffinity() }
            .setNegativeButton(
                resources.getString(R.string.Str_widgetConfirmClearNoTxt)
            ) { dialog, _ -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }
}

