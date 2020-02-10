package com.github.RezaKardoost.shirazuinternet.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class),version = 1)
abstract class AccountsDatabase : RoomDatabase(){

    abstract fun userDao():UserDao

    companion object{
        @Volatile
        private var instance: AccountsDatabase? = null

        fun getInstance(context:Context):AccountsDatabase{
            return instance ?: synchronized(this){
                instance ?:buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context):AccountsDatabase{
            return Room.databaseBuilder(context,AccountsDatabase::class.java,"shirazU_internet").build()
        }
    }

}