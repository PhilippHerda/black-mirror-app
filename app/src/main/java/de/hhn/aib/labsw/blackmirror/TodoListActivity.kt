package de.hhn.aib.labsw.blackmirror

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import de.hhn.aib.labsw.blackmirror.dataclasses.TodoItem
import de.hhn.aib.labsw.blackmirror.lists.TodoItemViewHolder
import de.hhn.aib.labsw.blackmirror.lists.TodoItemsAdapter
import java.time.ZonedDateTime

class TodoListActivity : AppCompatActivity() {
    private lateinit var todoListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        TodoItemViewHolder.onTodoItemClickedCallback = {
            runOnUiThread {
                AlertDialog.Builder(this@TodoListActivity)
                    .setTitle("Todo item clicked!")
                    .setMessage("'${it.text}'")
                    .show()
            }
        }

        todoListView = findViewById(R.id.todoListView)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            todoListView.adapter = TodoItemsAdapter(
                listOf(
                    TodoItem("Test todo item", ZonedDateTime.now()),
                    TodoItem("item 2", ZonedDateTime.now()),
                )
            )
        }
    }
}