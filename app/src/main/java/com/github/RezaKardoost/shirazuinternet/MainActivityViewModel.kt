package com.github.RezaKardoost.shirazuinternet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.RezaKardoost.shirazuinternet.Room.User
import com.github.RezaKardoost.shirazuinternet.util.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivityViewModel(application:Application):AndroidViewModel(application) {

    private val _accounts = MutableLiveData<MutableList<Account>>()
    private val _isLoading = MutableLiveData<Boolean>()
    private val _networkError = MutableLiveData<Event<String>>()
    val accounts:LiveData<MutableList<Account>> = _accounts
    val isLoading:LiveData<Boolean> = _isLoading
    val networkError:LiveData<Event<String>> = _networkError

    private val mainRepository = MainActivityRepository(getApplication())
    private lateinit var users:MutableList<User>

    init {

        getOrUpdateAccounts()
    }

    fun addNewUser(user:User){

        viewModelScope.launch {
            mainRepository.addNewUser(user)
            getOrUpdateAccounts()
        }

    }

    fun removeUser(username:String){

        viewModelScope.launch {
            mainRepository.removeUser(username)
        }

    }

    fun getOrUpdateAccounts(){

        viewModelScope.launch {
            users = mainRepository.getAllUsers()
            if (users.isNotEmpty()){

                _isLoading.value = true

                try {
                    val pages = withContext(Dispatchers.IO){
                        mainRepository.getPages(users)
                    }
                    _accounts.value = mainRepository.getAccounts(pages,users)

                }catch (e:Exception){
                    _networkError.value = Event("خطا اینترنت رخ داده است")
                    if(_accounts.value == null){
                        _accounts.value = mutableListOf()
                    }
                }

            }
            _isLoading.value = false

        }

    }

    fun validateAccount(user: User): Boolean {
        if (user.userName == "" || user.passWord == ""){
            return false
        }
        return true
    }

}