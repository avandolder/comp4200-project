package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewLoginsActivity : AppCompatActivity() {
    private lateinit var loginListView: LinearLayout
    private lateinit var loginViewModel: LoginViewModel

    private var selectedLogin: Login? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_logins)

        loginListView = findViewById(R.id.loginList)

        loginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application),
        ).get(LoginViewModel::class.java)

        val context = this
        loginViewModel.loginList.observe(this, { logins ->
            loginListView.removeAllViews()
            logins.forEach { login ->
                val loginText = TextView(context)
                loginText.text = login.toString()
                loginText.setOnClickListener { view ->
                    selectedLogin = login
                    loginListView.children.forEach { it.setBackgroundColor(resources.getColor(R.color.white)) }
                    view.setBackgroundColor(resources.getColor(R.color.selected))
                }
                loginListView.addView(loginText)
            }
        })
    }

    fun deleteLogin(@Suppress("UNUSED_PARAMETER") view: View?) {
        val login = selectedLogin ?: return
        CoroutineScope(Dispatchers.IO).launch {
            loginViewModel.delete(login)
        }
    }

    fun updateLogin(@Suppress("UNUSED_PARAMETER") view: View?) {
        val login = selectedLogin ?: return
        val intent = Intent(this, UpdateLoginActivity::class.java)
        intent.apply {
            putExtra("id", login.id!!)
            putExtra("site", login.site)
            putExtra("username", login.username)
            putExtra("password", login.password)
        }
        startActivity(intent)
    }
}