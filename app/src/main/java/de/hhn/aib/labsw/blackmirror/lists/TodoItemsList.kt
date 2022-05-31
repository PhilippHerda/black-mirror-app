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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class TodoItemViewHolder(
    itemView: View,
    private val context: Context
) : RecyclerView.ViewHolder(itemView) {
    companion object {
        lateinit var onTodoItemClickedCallback: (TodoItem, Int) -> Unit
    }

    private val dateCreatedTextView = itemView.findViewById<TextView>(R.id.dateCreatedTextView)
    private val todoTextView = itemView.findViewById<TextView>(R.id.todoTextView)

    private lateinit var item: TodoItem
    private var pos: Int = -1

    init {
        itemView.setOnClickListener {
            onTodoItemClickedCallback(item, pos)
        }
    }

    fun bind(item: TodoItem, position: Int) {
        this.item = item
        this.pos = position
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            dateCreatedTextView.text = item.date.format(formatter)
        }
        todoTextView.text = item.text
    }
}

class TodoItemsAdapter(
    private val items: MutableList<TodoItem>
) : RecyclerView.Adapter<TodoItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        return TodoItemViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item, parent, false), parent.context)
    }

    override fun onBindViewHolder(viewHolder: TodoItemViewHolder, position: Int) {
        viewHolder.bind(items[position], position)
    }

    override fun getItemCount() = items.size
}
