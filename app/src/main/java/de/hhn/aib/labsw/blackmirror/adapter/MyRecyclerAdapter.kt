package de.hhn.aib.labsw.blackmirror.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hhn.aib.labsw.blackmirror.R
import de.hhn.aib.labsw.blackmirror.dataclasses.Page
import de.hhn.aib.labsw.blackmirror.helper.ItemTouchHelperAdapter
import de.hhn.aib.labsw.blackmirror.helper.OnStartDragListener
import java.util.*
import kotlin.collections.ArrayList

class MyRecyclerAdapter(var context: Context,
                        var stringList:ArrayList<Page>,
                        var listener: OnStartDragListener
) : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>(), ItemTouchHelperAdapter {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txtnumber: TextView
        init {
            txtnumber = itemView.findViewById(R.id.txt_number) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.page_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtnumber.text = (position+1).toString()

        holder.itemView.setOnLongClickListener {
            listener.onStartDrag(holder)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return stringList.size
    }

    override fun onItemMove(fromPos: Int, toPos: Int): Boolean {
        Collections.swap(stringList, fromPos, toPos)
        notifyItemMoved(fromPos, toPos)
        return true
    }

    override fun onItemDismiss(pos: Int) {
        stringList.removeAt(pos)
        notifyItemRemoved(pos)
    }
}