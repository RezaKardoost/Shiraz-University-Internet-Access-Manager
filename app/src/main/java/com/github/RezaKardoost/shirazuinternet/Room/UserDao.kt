package com.github.RezaKardoost.shirazuinternet.Room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user:User)

    @Query("SELECT * FROM users")
    suspend fun getAll():MutableList<User>

    @Query("DELETE FROM users WHERE userName=:username")
    suspend fun remove(username:String)
}