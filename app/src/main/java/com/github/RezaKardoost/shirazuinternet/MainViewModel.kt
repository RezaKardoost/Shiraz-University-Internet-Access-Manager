package com.github.RezaKardoost.shirazuinternet

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.RezaKardoost.shirazuinternet.Room.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.coroutines.CoroutineContext

class MainViewModel(application:Application):AndroidViewModel(application) {

    private val _accounts = MutableLiveData<List<Account>>()
    private val _isLoading = MutableLiveData<Boolean>()
    val accounts:LiveData<List<Account>> = _accounts
    val isLoading:LiveData<Boolean> = _isLoading

    private val mainRepository = MainRepository(getApplication())
    private lateinit var users:List<User>

    init {

        getOrUpdateAccounts()
    }

    fun addNewUser(user:User){

        viewModelScope.launch {
            mainRepository.addNewUser(user)
        }

    }

    fun getOrUpdateAccounts(){

        viewModelScope.launch {
            users = mainRepository.getAllUsers()
            if (users.isNotEmpty()){

                _isLoading.value = true

                val pages = withContext(Dispatchers.IO){
                    mainRepository.getPages(users)
                }

                _accounts.value = mainRepository.getAccounts(pages)
                _isLoading.value = false


            }

        }

    }

    fun validateAccount(user: User): Boolean {
        if (user.userName == "" || user.passWord == ""){
            return false
        }
        return true
    }

}