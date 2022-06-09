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

class MyRecyclerAdapter(
    private var context: Context,
    private var stringList:ArrayList<Page>,
    private var listener: OnStartDragListener,
    private var mirror: Mirror,
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

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.number.text = (position+1).toString()
        holder.itemView.tag = position
        for (widget in mirror.pages[position].widgets) {
            val pos: Int = (widget.x - 1) + (widget.y - 1) * 3
            holder.grid[pos].foreground = getDrawableForWidget(widget)
            holder.grid[pos].background = AppCompatResources.getDrawable(context, R.drawable.selectable_box_small)
        }

        holder.itemView.setOnLongClickListener {
            listener.onStartDrag(holder)
            return@setOnLongClickListener true
        }

        holder.itemView.setOnClickListener {
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(context)
            builder.setMessage("Do you want to delete this page?")



            builder.setPositiveButton("Yes") { dialog, _ ->
                mirror.removePage((holder.itemView.tag as Int).toInt())
                notifyDataSetChanged()
                dialog.dismiss()
            }
            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog: android.app.AlertDialog? = builder.create()
            dialog?.show()
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
                    context,
                    R.drawable.calendar_widget_icon_foreground
                )!!
            }
            WidgetType.CLOCK -> {
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.clock_widget_icon_foreground
                )!!
            }
            WidgetType.MAIL -> {
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.mail_widget_icon_foreground
                )!!
            }
            WidgetType.REMINDER -> {
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.reminder_widget_icon_foreground
                )!!
            }
            WidgetType.WEATHER -> {
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.weather_widget_icon_foreground
                )!!
            }
        }
    }
}