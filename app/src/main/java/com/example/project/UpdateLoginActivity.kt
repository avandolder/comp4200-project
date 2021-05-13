package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateLoginActivity : AppCompatActivity() {
    private lateinit var siteText: EditText
    private lateinit var usernameText: EditText
    private lateinit var passwordText: EditText

    private lateinit var givenLogin: Login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_login)

        siteText = findViewById(R.id.siteText)
        usernameText = findViewById(R.id.usernameText)
        passwordText = findViewById(R.id.passwordText)

        givenLogin = Login(
            intent.getStringExtra("site")!!,
            intent.getStringExtra("username")!!,
            intent.getStringExtra("password")!!,
        )
        givenLogin.id = intent.getIntExtra("id", -1)

        siteText.setText(givenLogin.site)
        usernameText.setText(givenLogin.username)
        passwordText.setText(givenLogin.password)
    }

    fun updateLogin(@Suppress("UNUSED_PARAMETER") view: View?) {
        val login = Login(
            siteText.text.toString(),
            usernameText.text.toString(),
            passwordText.text.toString(),
        )
        login.id = givenLogin.id

        val loginDb = LoginDb.getInstance(this).loginDao()
        CoroutineScope(Dispatchers.IO).launch {
            loginDb.updateLogin(login)
        }

        finish()
    }
}