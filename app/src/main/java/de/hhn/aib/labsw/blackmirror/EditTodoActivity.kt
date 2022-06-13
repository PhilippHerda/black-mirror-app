package de.hhn.aib.labsw.blackmirror

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import de.hhn.aib.labsw.blackmirror.EditTodoActivity.Constants.TODO_TEXT_EXTRA

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

    object Constants {
        const val TODO_TEXT_EXTRA = "todo_text"
    }

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

    override fun finish() = cancel()

    private fun confirm() {
        if (todoTextArea.editText?.text?.isEmpty() == true) {
            AlertDialog.Builder(this).run {
                setTitle(null)
                setMessage(getString(R.string.empty_todo_dialog_msg))
                setPositiveButton(getString(R.string.empty_todo_dialog_confirm)) { dialog, _ ->
                    dialog.dismiss()
                    runOnUiThread(this@EditTodoActivity::returnAndRemoveItem)
                }
                setNegativeButton(getString(R.string.empty_todo_dialog_cancel)) { dialog, _ ->
                    dialog.dismiss()
                }
                create().show()
            }
        } else {
            commitChanges()
        }
    }

    private fun cancel() {
        val oldText = intent.getStringExtra(TODO_TEXT_EXTRA)
        val newText = todoTextArea.editText!!.text!!.toString()

        if (oldText == null) {
            // item was recently created and is edited the first time
            if (newText.isEmpty()) {
                returnAndRemoveItem()
            } else {
                askDicardChanges(oldText)
            }
        } else if (oldText != newText) {
            // item was changed
            askDicardChanges(oldText)
        } else {
            // item was not changed
            revertChanges()
        }
    }

    private fun askDicardChanges(oldText: String?) {
        AlertDialog.Builder(this).run {
            setTitle(null)
            setMessage(getString(R.string.edit_todo_cancel_dialog_msg))
            setPositiveButton(getString(R.string.edit_todo_cancel_dialog_confirm)) { dialog, _ ->
                if (oldText == null) {
                    runOnUiThread(this@EditTodoActivity::returnAndRemoveItem)
                } else {
                    runOnUiThread(this@EditTodoActivity::revertChanges)
                }
                dialog.dismiss()
            }
            setNegativeButton(getString(R.string.edit_todo_cancel_dialog_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            create().show()
        }
    }

    private fun returnAndRemoveItem() {
        setResult(RESULT_OK, Intent())
        super.finish()
    }

    private fun revertChanges() {
        setResult(RESULT_CANCELED)
        super.finish()
    }

    private fun commitChanges() {
        val data = Intent()
        data.putExtra(TODO_TEXT_EXTRA, todoTextArea.editText!!.text.toString())
        setResult(RESULT_OK, data)
        super.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                cancel()
                true
            }
            R.id.confirmItem -> {
                confirm()
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
