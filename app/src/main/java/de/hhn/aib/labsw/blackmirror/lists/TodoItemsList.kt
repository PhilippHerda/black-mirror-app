package de.hhn.aib.labsw.blackmirror.lists

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hhn.aib.labsw.blackmirror.R
import de.hhn.aib.labsw.blackmirror.dataclasses.TodoItem
import java.util.*

class TodoItemViewHolder(
    itemView: View,
    private val context: Context
) : RecyclerView.ViewHolder(itemView) {
    companion object {
        lateinit var onTodoItemClickedCallback: (TodoItem) -> Unit
    }

    private val dateCreatedTextView = itemView.findViewById<TextView>(R.id.dateCreatedTextView)
    private val todoTextView = itemView.findViewById<TextView>(R.id.todoTextView)

    private lateinit var item: TodoItem

    init {
        itemView.setOnClickListener {
            onTodoItemClickedCallback(item)
        }
    }

    fun bind(item: TodoItem) {
        this.item = item
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateCreatedTextView.text = DateFormat
                .getDateFormat(context)
                .format(Date.from(item.date.toInstant()))
        }
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
