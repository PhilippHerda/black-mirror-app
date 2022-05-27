package de.hhn.aib.labsw.blackmirror.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hhn.aib.labsw.blackmirror.R
import de.hhn.aib.labsw.blackmirror.helper.ItemTouchHelperAdapter
import de.hhn.aib.labsw.blackmirror.helper.OnStartDragListener
import java.util.*

class MyRecyclerAdapter(var context: Context,
                        var stringList:MutableList<String?>,
                        var listener: OnStartDragListener
) : RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>(), ItemTouchHelperAdapter {

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //var txt_title:TextView
        init {
            // txt_title = itemView.txt_title as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.pages_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.txt_title!!.text = stringList[position]

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