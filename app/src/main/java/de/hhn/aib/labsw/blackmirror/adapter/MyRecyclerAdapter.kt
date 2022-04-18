package de.hhn.aib.labsw.blackmirror.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hhn.aib.labsw.blackmirror.R
import de.hhn.aib.labsw.blackmirror.helper.ItemTouchHelperAdapter
import  de.hhn.aib.labsw.blackmirror.helper.OnStartDragListener
import java.util.*

class MyRecyclerAdapter(
    var context: Context,
    var strings: MutableList<String?>,
    var listener: OnStartDragListener
) : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>(), ItemTouchHelperAdapter {

    inner class MyViewHolder(itemView: View) : RecyclerView
    .ViewHolder(itemView) {
        var widgetItem: TextView = itemView.findViewById(R.id.widgetItem) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.widget_layout_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return strings.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.widgetItem.text = strings[position]

        holder.itemView.setOnLongClickListener {
            listener.onStartDrag(holder)
            false
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(strings, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        strings.removeAt(position)
        notifyItemRemoved(position)
    }
}