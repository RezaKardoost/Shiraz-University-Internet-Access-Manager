package com.github.RezaKardoost.shirazuinternet.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey val userName:String,
    val passWord:String
)