package com.example.project

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Login::class], version = 1, exportSchema = false)
abstract class LoginDb : RoomDatabase() {
    abstract fun loginDao(): LoginDao

    companion object {
        @Volatile
        private var INSTANCE: LoginDb? = null

        fun getInstance(context: Context): LoginDb {
            return INSTANCE ?: synchronized(LoginDb::class.java) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        LoginDb::class.java,
                        "login_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}