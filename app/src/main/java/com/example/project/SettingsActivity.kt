package com.example.project

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText

class SettingsActivity : AppCompatActivity() {
    private lateinit var defaultUsername: EditText
    private lateinit var passwordLength: EditText
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        defaultUsername = findViewById(R.id.defaultUsername)
        passwordLength = findViewById(R.id.passwordLength)
        prefs = getSharedPreferences(getString(R.string.shared_pref_file), Context.MODE_PRIVATE)

        defaultUsername.setText(prefs.getString(getString(R.string.default_username_key), ""))
        passwordLength.setText(prefs.getInt(getString(R.string.password_length_key), 10).toString())
    }

    fun updateSettings(@Suppress("UNUSED_PARAMETER") view: View?) {
        with(prefs.edit()) {
            putString(getString(R.string.default_username_key), defaultUsername.text.toString())
            val length = try {
                passwordLength.text.toString().toInt()
            } catch (_: NumberFormatException) {
                10
            }
            putInt(getString(R.string.password_length_key), length)
            apply()
        }
        finish()
    }
}