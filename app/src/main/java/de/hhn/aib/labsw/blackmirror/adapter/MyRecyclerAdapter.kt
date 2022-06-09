package de.hhn.aib.labsw.blackmirror.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import de.hhn.aib.labsw.blackmirror.R
import de.hhn.aib.labsw.blackmirror.dataclasses.Mirror
import de.hhn.aib.labsw.blackmirror.dataclasses.Page
import de.hhn.aib.labsw.blackmirror.dataclasses.Widget
import de.hhn.aib.labsw.blackmirror.dataclasses.WidgetType
import de.hhn.aib.labsw.blackmirror.helper.ItemTouchHelperAdapter
import de.hhn.aib.labsw.blackmirror.helper.OnStartDragListener
import java.util.*
import kotlin.collections.ArrayList

class MyRecyclerAdapter(
    private var context: Context,
    private var stringList:ArrayList<Page>,
    private var listener: OnStartDragListener,
    private var mirror: Mirror,
    private var contextParent: Context
) : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>(), ItemTouchHelperAdapter {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var number: TextView
        var grid: androidx.gridlayout.widget.GridLayout
        init {
            number = itemView.findViewById(R.id.txt_number) as TextView
            grid = itemView.findViewById(R.id.PageItemGrid)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.page_item,parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.number.text = (position+1).toString()
        for (widget in mirror.pages[position].widgets) {
            val pos: Int = (widget.x - 1) + (widget.y - 1) * 3
            holder.grid[pos].foreground = getDrawableForWidget(widget)
            holder.grid[pos].background = AppCompatResources.getDrawable(contextParent, R.drawable.selectable_box_small)
        }

        holder.itemView.setOnLongClickListener {
            listener.onStartDrag(holder)
            return@setOnLongClickListener true
        }

        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return stringList.size
    }

    override fun onItemMove(fromPos: Int, toPos: Int): Boolean {
        Collections.swap(stringList, fromPos, toPos)
        mirror.swapPages(fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
        return true
    }

    override fun onItemDismiss(pos: Int) {
        stringList.removeAt(pos)
        notifyItemRemoved(pos)
    }

    private fun getDrawableForWidget(widget: Widget): Drawable {
        return when (widget.type) {
            WidgetType.CALENDAR -> {
                AppCompatResources.getDrawable(
                    contextParent,
                    R.drawable.calendar_widget_icon_foreground
                )!!
            }
            WidgetType.CLOCK -> {
                AppCompatResources.getDrawable(
                    contextParent,
                    R.drawable.clock_widget_icon_foreground
                )!!
            }
            WidgetType.MAIL -> {
                AppCompatResources.getDrawable(
                    contextParent,
                    R.drawable.mail_widget_icon_foreground
                )!!
            }
            WidgetType.REMINDER -> {
                AppCompatResources.getDrawable(
                    contextParent,
                    R.drawable.reminder_widget_icon_foreground
                )!!
            }
            WidgetType.WEATHER -> {
                AppCompatResources.getDrawable(
                    contextParent,
                    R.drawable.weather_widget_icon_foreground
                )!!
            }
        }
    }
}