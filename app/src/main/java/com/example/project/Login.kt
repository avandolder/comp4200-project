package com.example.project

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Entity(tableName = "logins")
data class Login(val site: String, val username: String, val password: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    override fun toString() = """
        $site
            username: $username
            password: $password
    """.trimIndent()
}

@Dao
interface LoginDao {
    @Insert
    suspend fun insertLogin(login: Login)

    @Update
    suspend fun updateLogin(login: Login)

    @Delete
    suspend fun deleteLogin(login: Login)

    @Query("SELECT * FROM logins ORDER BY id DESC")
    fun getLogins(): LiveData<List<Login>>
}

class LoginRepository(application: Application) {
    private val loginDb = LoginDb.getInstance(application)
    private val loginDao = loginDb.loginDao()
    val logins = loginDao.getLogins()

    fun insertLogin(login: Login) {
        CoroutineScope(Dispatchers.IO).launch {
            loginDao.insertLogin(login)
        }
    }

    fun updateLogin(login: Login) {
        CoroutineScope(Dispatchers.IO).launch {
            loginDao.updateLogin(login)
        }
    }

    fun deleteLogin(login: Login) {
        CoroutineScope(Dispatchers.IO).launch {
            loginDao.deleteLogin(login)
        }
    }
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LoginRepository(application)
    val loginList = repository.logins

    fun insert(login: Login) = repository.insertLogin(login)
    fun update(login: Login) = repository.updateLogin(login)
    fun delete(login: Login) = repository.deleteLogin(login)
}