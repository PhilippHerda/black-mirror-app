package de.hhn.aib.labsw.blackmirror.lists

import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hhn.aib.labsw.blackmirror.R
import de.hhn.aib.labsw.blackmirror.dataclasses.TodoItem

class TodoItemViewHolder(
    itemView: View,
    private val context: Context
) : RecyclerView.ViewHolder(itemView) {
    private val dateCreatedTextView = itemView.findViewById<TextView>(R.id.dateCreatedTextView)
    private val todoTextView = itemView.findViewById<TextView>(R.id.todoTextView)

    fun bind(item: TodoItem) {
        dateCreatedTextView.text = DateFormat.getDateFormat(context).format(item.date)
        todoTextView.text = item.text
    }
}

class TodoItemsAdapter(
    private val items: List<TodoItem>
) : RecyclerView.Adapter<TodoItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        return TodoItemViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false), parent.context)
    }

    override fun onBindViewHolder(viewHolder: TodoItemViewHolder, position: Int) {
        viewHolder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
