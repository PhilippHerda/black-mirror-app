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
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.hhn.aib.labsw.blackmirror.EditTodoActivity.Constants.TODO_TEXT_EXTRA
import de.hhn.aib.labsw.blackmirror.TodoListActivity.Constants.FETCH_TODOS_TOPIC
import de.hhn.aib.labsw.blackmirror.TodoListActivity.Constants.TODOS_TOPIC
import de.hhn.aib.labsw.blackmirror.dataclasses.APITodoData
import de.hhn.aib.labsw.blackmirror.dataclasses.APITodoEntry
import de.hhn.aib.labsw.blackmirror.dataclasses.TodoItem
import de.hhn.aib.labsw.blackmirror.lists.RecyclerViewList
import de.hhn.aib.labsw.blackmirror.lists.TodoListItem
import org.json.JSONObject
import java.time.Instant
import java.time.ZoneId
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
        const val FETCH_TODOS_TOPIC = "fetchTodoList"
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
            requireAPIVersion(Build.VERSION_CODES.O) {
                val item = TodoItem("", ZonedDateTime.now())
                todoList.add(item)
                todoList.recentlyClickedItem = item
                activityResultLauncher.launch(
                    Intent(this@TodoListActivity, EditTodoActivity::class.java)
                )
            }
        }
        subscribe(TODOS_TOPIC,this)
        publishToRemotes(FETCH_TODOS_TOPIC, Unit)
    }

    override fun dataReceived(topic: String, node: JsonNode) {
        if (topic != TODOS_TOPIC) {
            throw IllegalArgumentException("topic must be $TODOS_TOPIC")
        }
        try {
            requireAPIVersion(Build.VERSION_CODES.O) {
                val data = nodeToObject(node, APITodoData::class.java)
                data.entries.forEach { (createdTimestamp, text) ->
                    todoList.add(
                        TodoItem(
                            text,
                            ZonedDateTime.ofInstant(
                                Instant.ofEpochMilli(createdTimestamp),
                                ZoneId.systemDefault()
                            )
                        )
                    )
                }
            }
        } catch (e: JsonProcessingException) {
            runOnUiThread {
                Toast.makeText(this, R.string.fetch_todos_failed, Toast.LENGTH_SHORT).show()
            }
            e.printStackTrace()
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
        requireAPIVersion(Build.VERSION_CODES.O) {
            val data =
                APITodoData(todoList.data.map { APITodoEntry(it.date.toEpochSecond(), it.text) })
            publishToRemotes(TODOS_TOPIC, data)
            Toast.makeText(this, R.string.todos_sent_successfully, Toast.LENGTH_SHORT).show()
        }
    }
}
