package de.hhn.aib.labsw.blackmirror

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class TodoListActivity : AppCompatActivity() {
    private lateinit var todoListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        todoListView = findViewById(R.id.todoListView)
        todoListView.adapter = TodoItemsAdapter(listOf(
            TodoItem("Test todo item", ZonedDateTime.now())
        ))
    }
}