package de.hhn.aib.labsw.blackmirror.lists

import android.os.Build
import android.view.View
import android.widget.TextView
import de.hhn.aib.labsw.blackmirror.R
import de.hhn.aib.labsw.blackmirror.dataclasses.TodoItem
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * @author Markus Marewitz
 * @version 2022-06-02
 */
class TodoListItem(itemView: View) : RecyclerViewList.ItemView<TodoItem>() {
    private val dateCreatedTextView = itemView.findViewById<TextView>(R.id.dateCreatedTextView)
    private val todoTextView = itemView.findViewById<TextView>(R.id.todoTextView)

    override fun onBind() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            dateCreatedTextView.text = model.date.format(formatter)
        }
        todoTextView.text = model.text
    }
}
