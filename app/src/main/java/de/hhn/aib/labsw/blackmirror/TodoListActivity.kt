package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hhn.aib.labsw.blackmirror.EditTodoActivity.Constants.TODO_TEXT_EXTRA
import de.hhn.aib.labsw.blackmirror.TodoListActivity.Constants.TODOS_TOPIC
import de.hhn.aib.labsw.blackmirror.dataclasses.APITodoData
import de.hhn.aib.labsw.blackmirror.dataclasses.APITodoEntry
import de.hhn.aib.labsw.blackmirror.dataclasses.TodoItem
import de.hhn.aib.labsw.blackmirror.lists.RecyclerViewList
import de.hhn.aib.labsw.blackmirror.lists.TodoListItem
import java.time.ZonedDateTime

/**
 * Activity to manage (supports CRUD) a to do list.
 * @author Markus Marewitz
 * @version 2022-06-02
 */

class TodoListActivity : AbstractActivity() {
    private lateinit var todoList: RecyclerViewList<TodoListItem, TodoItem>

    object Constants {
        const val TODOS_TOPIC = "todoList"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)

        supportActionBar?.title = getString(R.string.todo_list_title)

        val activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val todoText = it.data?.getStringExtra(TODO_TEXT_EXTRA)
                    if (todoText == null) {
                        todoList.remove(todoList.recentlyClickedItem)
                    } else {
                        todoList.updateRecentlyClickedItem { item ->
                            item.text = todoText
                        }
                        todoList.scrollToItem(todoList.recentlyClickedItem)
                    }
                } else {
                    todoList.scrollToItem(todoList.recentlyClickedItem)
                }
                sendTodosToMirror()
            }

        todoList = RecyclerViewList(this, findViewById(R.id.todoListView),
            R.layout.todo_item, { TodoListItem(it) })
        todoList.setOnItemClickedListener { item ->
            val intent = Intent(this@TodoListActivity, EditTodoActivity::class.java)
            intent.putExtra(TODO_TEXT_EXTRA, item.text)
            activityResultLauncher.launch(intent)
        }
        todoList.removeItemOnSwipe(true)
        todoList.setOnItemRemovedListener { sendTodosToMirror() }

        val addItemBtn = findViewById<FloatingActionButton>(R.id.addItemButton)
        addItemBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val item = TodoItem("", ZonedDateTime.now())
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.todo_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.helpItem -> {
                showHelp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showHelp() {
        AlertDialog.Builder(this).run {
            setTitle(R.string.todo_help_title)
            setMessage(getString(R.string.todo_help_msg))
            create().show()
        }
    }

    private fun sendTodosToMirror() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val data =
                APITodoData(todoList.data.map { APITodoEntry(it.date.toEpochSecond(), it.text) })
            publishToRemotes(TODOS_TOPIC, data)
            Toast.makeText(this, R.string.todos_sent_successfully, Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(this).run {
                setTitle(R.string.feature_not_supported_dialog_title)
                setMessage(R.string.feature_not_supported_dialog_msg)
                create().show()
            }
        }
    }
}
