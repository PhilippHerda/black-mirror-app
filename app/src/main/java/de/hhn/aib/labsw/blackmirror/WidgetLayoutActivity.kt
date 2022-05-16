package de.hhn.aib.labsw.blackmirror

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.GestureDetector
import android.view.MotionEvent
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
import de.hhn.aib.labsw.blackmirror.dataclasses.MyPage
import de.hhn.aib.labsw.blackmirror.dataclasses.Widget


/**
 * This activity initializes an interface where different widgets can be dragged on a
 * simulated mirror. These layouts can be saved.
 * This allows customizable mirror layouts.
 *
 * @author Selim Özdemir
 * @version 09-05-2022
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
    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        myGridLayout = findViewById(R.id.widgetGrid)
        for (box in myGridLayout) {
            box.setOnClickListener {
                if (box.foreground != null) {
                    intent = null
                    when (box.foreground) {
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.mail_widget_icon_foreground
                        ) -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) mail configuration
                        }
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.calendar_widget_icon_foreground
                        ) -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) calendar configuration
                        }
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.weather_widget_icon_foreground
                        ) -> {
                            intent = Intent(
                                this@WidgetLayoutActivity,
                                WeatherLocationActivity::class.java
                            )
                        }
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.clock_widget_icon_foreground
                        ) -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) clock configuration
                        }
                        AppCompatResources.getDrawable(
                            this@WidgetLayoutActivity,
                            R.drawable.reminder_widget_icon_foreground
                        ) -> {
                            // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) reminder configuration
                        }
                    }
                    startActivity(intent)
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
            val page = MyPage()
            var pos = 1
            for (box in myGridLayout) {
                when (box.foreground) {
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.mail_widget_icon_foreground
                    ) -> {
                        page.addWidget(Widget("mail", pos % 3, pos / 3 + 1))
                    }
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.calendar_widget_icon_foreground
                    ) -> {
                        page.addWidget(Widget("calendar", pos % 3, pos / 3 + 1))
                    }
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.weather_widget_icon_foreground
                    ) -> {
                        page.addWidget(Widget("weather", pos % 3, pos / 3 + 1))
                    }
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.clock_widget_icon_foreground
                    ) -> {
                        page.addWidget(Widget("clock", pos % 3, pos / 3 + 1))
                    }
                    AppCompatResources.getDrawable(
                        this,
                        R.drawable.reminder_widget_icon_foreground
                    ) -> {
                        page.addWidget(Widget("reminder", pos % 3, pos / 3 + 1))
                    }
                }
                pos++
            }
            savedPage = page
            val myToast =
                Toast.makeText(applicationContext, "Successfully saved!", Toast.LENGTH_SHORT)
            myToast.show()
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

    inner class MyBoxClickListener(v: ImageView) : GestureDetector.OnGestureListener {
        var view: ImageView = v

        override fun onDown(e: MotionEvent?): Boolean {
            return false
        }

        override fun onShowPress(e: MotionEvent?) {
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            if (view.foreground != null) {
                intent = null
                when (view.foreground) {
                    AppCompatResources.getDrawable(
                        this@WidgetLayoutActivity,
                        R.drawable.mail_widget_icon_foreground
                    ) -> {
                        // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) mail configuration
                    }
                    AppCompatResources.getDrawable(
                        this@WidgetLayoutActivity,
                        R.drawable.calendar_widget_icon_foreground
                    ) -> {
                        // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) calendar configuration
                    }
                    AppCompatResources.getDrawable(
                        this@WidgetLayoutActivity,
                        R.drawable.weather_widget_icon_foreground
                    ) -> {
                        intent = Intent(
                            this@WidgetLayoutActivity,
                            WeatherLocationActivity::class.java
                        )
                    }
                    AppCompatResources.getDrawable(
                        this@WidgetLayoutActivity,
                        R.drawable.clock_widget_icon_foreground
                    ) -> {
                        // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) clock configuration
                    }
                    AppCompatResources.getDrawable(
                        this@WidgetLayoutActivity,
                        R.drawable.reminder_widget_icon_foreground
                    ) -> {
                        // intent = Intent(this@WidgetLayoutActivity, Activity::class.java) reminder configuration
                    }
                }
                startActivity(intent)
                return true
            }
            return false
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            if (view.foreground != null) {
                for (widget in widgetList) {
                    if (view.foreground == widget.foreground) {
                        widget.performLongClick()
                    }
                }
                view.background =
                    AppCompatResources.getDrawable(
                        this@WidgetLayoutActivity,
                        R.drawable.box
                    )
                view.foreground = null
            }
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return false
        }
    }
}

