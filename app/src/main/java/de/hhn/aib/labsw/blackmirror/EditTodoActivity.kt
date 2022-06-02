package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

const val TODO_TEXT_EXTRA = "todo_text"

/**
 * Activity to edit an to do item.
 * Returns [android.app.Activity.RESULT_OK] when the text has been changed
 * or [android.app.Activity.RESULT_CANCELED] when the user has canceled editing as
 * an activity result.
 *
 * The edited to do text is passed as an intent extra with the key [TODO_TEXT_EXTRA]
 * if the text is not empty. Otherwise this extra is not passed and the caller should
 * discard the to do item.
 * @author Markus Marewitz
 * @version 2022-06-02
 */
class EditTodoActivity : AppCompatActivity() {
    private lateinit var todoTextArea: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_todo)

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.todo_list_title)
        }

        todoTextArea = findViewById(R.id.todoTextArea)
        val prevText = intent.getStringExtra(TODO_TEXT_EXTRA)
        if (prevText != null) {
            todoTextArea.editText?.setText(prevText)
        }
    }

    override fun finish() {
        if (todoTextArea.editText?.text?.isEmpty() == true) {
            AlertDialog.Builder(this).run {
                setTitle(null)
                setMessage(getString(R.string.empty_todo_dialog_msg))
                setPositiveButton(getString(R.string.empty_todo_dialog_confirm)) { dialog, _ ->
                    dialog.dismiss()
                    runOnUiThread(this@EditTodoActivity::onFinishConfirmed)
                }
                setNegativeButton(getString(R.string.empty_todo_dialog_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                create().show()
            }
        } else {
            onFinishConfirmed()
        }
    }

    private fun onFinishConfirmed() {
        val data = Intent()
        if (todoTextArea.editText?.text?.isNotEmpty() == true) {
            data.putExtra(TODO_TEXT_EXTRA, todoTextArea.editText?.text.toString())
        }
        setResult(RESULT_OK, data)

        super.finish()
    }

    private fun cancel() {
        val oldText = intent.getStringExtra(TODO_TEXT_EXTRA)
        val newText = todoTextArea.editText?.text?.toString()
        if (oldText != null && newText != null && oldText != newText) {
            AlertDialog.Builder(this).run {
                setTitle(null)
                setMessage(getString(R.string.edit_todo_cancel_dialog_msg))
                setPositiveButton(getString(R.string.edit_todo_cancel_dialog_confirm)) { dialog, _ ->
                    runOnUiThread(this@EditTodoActivity::onCancelConfirmed)
                    dialog.dismiss()
                }
                setNegativeButton(getString(R.string.edit_todo_cancel_dialog_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                create().show()
            }
        } else {
            onCancelConfirmed()
        }
    }

    private fun onCancelConfirmed() {
        setResult(RESULT_CANCELED)
        super.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                cancel()
                true
            }
            R.id.confirmItem -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.edit_todo_menu, menu)
        return true
    }
}
