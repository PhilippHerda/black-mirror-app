package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
                if (todoText == null) {
                    todoList.remove(todoList.recentlyClickedItem)
                } else {
                    todoList.updateRecentlyClickedItem { item ->
                        item.text = todoText
                    }
                }
            }

        todoList = RecyclerViewList(this, findViewById(R.id.todoListView),
            R.layout.todo_item, { TodoListItem(it) })
        todoList.setOnItemClickedListener { item ->
            val intent = Intent(this@TodoListActivity, EditTodoActivity::class.java)
            intent.putExtra(TODO_TEXT_EXTRA, item.text)
            activityResultLauncher.launch(intent)
        }
        todoList.removeItemOnSwipe(true)

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

        // sample todos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            listOf(
                TodoItem("Müll rausbringen", ZonedDateTime.now()),
                TodoItem("Auf Klausur lernen", ZonedDateTime.now()),
                TodoItem("Am Projekt weiterarbeiten", ZonedDateTime.now()),
                TodoItem("Behörde zurückrufen", ZonedDateTime.now()),
                TodoItem("Auslagen zurückzahlen", ZonedDateTime.now()),
                TodoItem("DVD zurückgeben", ZonedDateTime.now()),
                TodoItem("Dune anschauen", ZonedDateTime.now()),
                TodoItem("Urlaub planen", ZonedDateTime.now()),
                TodoItem("Ideen sammeln, wie ich reich werde", ZonedDateTime.now()),
                TodoItem("Eine Rezension für den neuen Staubsauger schreiben", ZonedDateTime.now()),
                TodoItem("Neujahrsvorsätze umsetzen", ZonedDateTime.now()),
                TodoItem("Mehr Sport treiben", ZonedDateTime.now()),
                TodoItem("E-Mails sortieren", ZonedDateTime.now()),
                TodoItem("Milch einkaufen", ZonedDateTime.now()),
                TodoItem("Kellerlicht reparieren", ZonedDateTime.now()),
                TodoItem("Geburtstagsgeschenk kaufen", ZonedDateTime.now())
            ).forEach {
                todoList.add(it)
            }
        }
    }
}