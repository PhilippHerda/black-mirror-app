package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hhn.aib.labsw.blackmirror.dataclasses.TodoItem
import de.hhn.aib.labsw.blackmirror.lists.*
import java.time.ZonedDateTime

class TodoListActivity : AppCompatActivity() {
    private lateinit var todoList: RecyclerViewList<TodoListItem, TodoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != RESULT_OK) return@registerForActivityResult
                val todoText = it.data?.getStringExtra(TODO_TEXT_EXTRA)
                todoList.updateRecentlyClickedItem { item ->
                    item.text = todoText ?: return@updateRecentlyClickedItem
                }
            }

        todoList = RecyclerViewList(this, findViewById(R.id.todoListView),
            R.layout.todo_item, { TodoListItem(it) })
        todoList.setOnItemClickedListener { item ->
            val intent = Intent(this@TodoListActivity, EditTodoActivity::class.java)
            intent.putExtra(TODO_TEXT_EXTRA, item.text)
            activityResultLauncher.launch(intent)
        }

        val addItemBtn = findViewById<FloatingActionButton>(R.id.addItemButton)
        addItemBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val item = TodoItem("Added item ${todoList.size + 1}", ZonedDateTime.now())
                todoList.add(item)
                todoList.recentlyClickedItem = item
                activityResultLauncher.launch(
                    Intent(this@TodoListActivity, EditTodoActivity::class.java)
                )
            }
        }
    }
}