package com.example.project

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

const val url = "https://makemeapassword.ligos.net/api/v1/alphanumeric/json"

class AddLoginActivity : AppCompatActivity() {
    private lateinit var siteText: EditText
    private lateinit var usernameText: EditText
    private lateinit var passwordText: EditText
    private lateinit var prefs: SharedPreferences

    private val passwordReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            passwordText.setText(intent!!.getStringExtra("password"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_login)

        siteText = findViewById(R.id.siteText)
        usernameText = findViewById(R.id.usernameText)
        passwordText = findViewById(R.id.passwordText)

        prefs = getSharedPreferences(getString(R.string.shared_pref_file), Context.MODE_PRIVATE)
        usernameText.setText(prefs.getString(getString(R.string.default_username_key), ""))

        LocalBroadcastManager
            .getInstance(this)
            .registerReceiver(passwordReceiver, IntentFilter("passwordReceived"))
    }

    fun addLogin(@Suppress("UNUSED_PARAMETER") view: View?) {
        val login = Login(
            siteText.text.toString(),
            usernameText.text.toString(),
            passwordText.text.toString(),
        )

        val loginDb = LoginDb.getInstance(this).loginDao()
        CoroutineScope(Dispatchers.IO).launch {
            loginDb.insertLogin(login)
        }

        finish()
    }

    fun generatePassword(@Suppress("UNUSED_PARAMETER") view: View?) {
        val passwordLength = prefs.getInt(getString(R.string.password_length_key), 10)
        val broadcastManager = LocalBroadcastManager.getInstance(this)

        CoroutineScope(Dispatchers.IO).launch {
            val json = JSONObject(runCatching {
                URL("$url?c=1&sym=T&l=$passwordLength").readText()
            }.getOrThrow())
            val pws = json.getJSONArray("pws")
            val pw = pws.getString(0)
            val intent = Intent("passwordReceived").apply {
                putExtra("password", pw)
            }
            broadcastManager.sendBroadcast(intent)
        }
    }
}