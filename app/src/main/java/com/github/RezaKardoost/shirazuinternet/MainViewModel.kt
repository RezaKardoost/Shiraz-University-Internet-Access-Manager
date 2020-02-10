package com.github.RezaKardoost.shirazuinternet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.RezaKardoost.shirazuinternet.Room.AccountsDatabase
import com.github.RezaKardoost.shirazuinternet.Room.User
import kotlinx.coroutines.launch

class MainViewModel(application:Application):AndroidViewModel(application) {

    val dbInstance = AccountsDatabase.getInstance(getApplication())

    val accounts : LiveData<List<User>> = dbInstance.userDao().getAll()


    fun addNewAccount(user:User){

        viewModelScope.launch {
            dbInstance.userDao().insert(user)
        }

    }

    fun validateAccount(user: User): Boolean {
        if (user.userName == "" || user.passWord == ""){
            return false
        }
        return true
    }

    override fun onCleared() {
        super.onCleared()
        dbInstance.close()
    }

}