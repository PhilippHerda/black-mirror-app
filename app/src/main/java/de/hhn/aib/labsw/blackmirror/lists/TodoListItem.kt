package de.hhn.aib.labsw.blackmirror.lists

import android.os.Build
import android.view.View
import android.widget.TextView
import de.hhn.aib.labsw.blackmirror.R
import de.hhn.aib.labsw.blackmirror.dataclasses.TodoItem
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class TodoListItem(item: TodoItem) : RecyclerViewList.ListItem<TodoItem>(item) {
    private lateinit var dateCreatedTextView: TextView
    private lateinit var todoTextView: TextView

    override fun populateView(itemView: View) {
        dateCreatedTextView = itemView.findViewById(R.id.dateCreatedTextView)
        todoTextView = itemView.findViewById(R.id.todoTextView)
    }

    override fun updateView(itemView: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            dateCreatedTextView.text = model.date.format(formatter)
        }
        todoTextView.text = model.text
    }
}
