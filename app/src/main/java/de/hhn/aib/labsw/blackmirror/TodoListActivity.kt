package de.hhn.aib.labsw.blackmirror

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import de.hhn.aib.labsw.blackmirror.dataclasses.TodoItem
import de.hhn.aib.labsw.blackmirror.lists.*
import java.time.ZonedDateTime

class TodoListActivity : AppCompatActivity() {
    private lateinit var todoListView: RecyclerView

    private lateinit var lastItem: TodoItem
    private lateinit var lastItemViewHolder: TodoItemViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != RESULT_OK) return@registerForActivityResult
                val todoText = it.data?.getStringExtra(TODO_TEXT_EXTRA)
                lastItem.text = todoText ?: return@registerForActivityResult
                runOnUiThread { lastItemViewHolder.bind(lastItem) }
            }
        TodoItemViewHolder.onTodoItemClickedCallback = { item, viewHolder ->
            lastItem = item
            lastItemViewHolder = viewHolder
            val intent = Intent(this@TodoListActivity, EditTodoActivity::class.java)
            intent.putExtra(TODO_TEXT_EXTRA, item.text)
            activityResultLauncher.launch(intent)
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