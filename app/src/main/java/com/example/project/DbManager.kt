package com.example.project

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DbManager(context: Context) : SQLiteOpenHelper(context, "Logins.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $table (${BaseColumns._ID} INTEGER NOT NULL PRIMARY KEY, site TEXT, username TEXT, password TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $table")
        onCreate(db)
    }

    fun getLogins(): List<Login> {
        val db = readableDatabase
        val projection = arrayOf("site", "username", "password")
        val logins = mutableListOf<Login>()

        db.query(
            table,
            projection,
            null,
            null,
            null,
            null,
            null
        ).use {
            while (it.moveToNext()) {
                val site = it.getString(it.getColumnIndex("site"))
                val username = it.getString(it.getColumnIndex("username"))
                val password = it.getString(it.getColumnIndex("password"))
                logins.add(Login(site, username, password))
            }
        }

        return logins
    }

    fun addLogin(login: Login) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("site", login.site)
            put("username", login.username)
            put("password", login.password)
        }
        db.insert(table, null, values)
    }

    fun changeLogin(oldLogin: Login, newLogin: Login) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("site", newLogin.site)
            put("username", newLogin.username)
            put("password", newLogin.password)
        }
        db.update(
            table,
            values,
            "site = ?, username = ?, password = ?",
            arrayOf(oldLogin.site, oldLogin.username, oldLogin.password),
        )
    }

    companion object {
        const val table = "logins"
    }
}