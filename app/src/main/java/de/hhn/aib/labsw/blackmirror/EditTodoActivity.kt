package de.hhn.aib.labsw.blackmirror

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

const val TODO_TEXT_EXTRA = "todo_text"

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }

    override fun onCreateOptionsMenu(menu: Menu?) = true
}
