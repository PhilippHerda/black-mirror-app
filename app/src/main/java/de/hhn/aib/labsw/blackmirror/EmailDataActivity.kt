package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

/**
 * @author Lukas Michalsky
 * @version 12.05.2022
 */
class EmailDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_data)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etImap = findViewById<EditText>(R.id.et_imap)
        val etInbox = findViewById<EditText>(R.id.et_inbox)
        val btnSave = findViewById<Button>(R.id.btn_email_save)

        btnSave.setOnClickListener {
            var email = etEmail.text.toString()
            var password = etPassword.text.toString()
            var imap = etImap.text.toString()
            var inbox = etInbox.text.toString()
        }
    }


}