package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import de.hhn.aib.labsw.blackmirror.dataclasses.Email

/**
 * @author Lukas Michalsky
 * @version 30.05.2022
 */
class EmailDataActivity : AbstractActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_data)

        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etImap = findViewById<EditText>(R.id.et_imap)
        val btnSave = findViewById<Button>(R.id.btn_email_save)

        btnSave.setOnClickListener {
            var email = etEmail.text.toString()
            var password = etPassword.text.toString()
            var imap = etImap.text.toString()

            Log.i(password, email)
            val emailData = Email(imap, 993, email, password)
            publishToRemotes("emailData", emailData)
        }
    }


}