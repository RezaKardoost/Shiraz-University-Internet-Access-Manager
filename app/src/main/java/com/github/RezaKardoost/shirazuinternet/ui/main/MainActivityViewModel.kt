package com.github.RezaKardoost.shirazuinternet.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.RezaKardoost.shirazuinternet.Account
import com.github.RezaKardoost.shirazuinternet.Room.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainActivityViewModel(application:Application):AndroidViewModel(application) {

    private val _accountsLiveData = MutableLiveData<AccountsResult<MutableList<Account>>>()
    val accountsLiveData:LiveData<AccountsResult<MutableList<Account>>> = _accountsLiveData

    private var accounts:MutableList<Account>? = null


    private val mainRepository =
        MainActivityRepository(
            getApplication()
        )
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

        if (accounts!!.size == 0){
            _accountsLiveData.value = AccountsResult.Empty
        }

    }

    fun getOrUpdateAccounts(){

        viewModelScope.launch {
            users = mainRepository.getAllUsers()
            if (users.isNotEmpty()){

                _accountsLiveData.value = AccountsResult.Loading(accounts)

                try {
                    val pages = withContext(Dispatchers.IO){
                        mainRepository.getPages(users)
                    }
                    accounts = mainRepository.getAccounts(pages,users)
                    _accountsLiveData.value = AccountsResult.Success(accounts!!)

                }catch (e:Exception){
                    _accountsLiveData.value = AccountsResult.Error(e,accounts)
                }

            }else{
                _accountsLiveData.value = AccountsResult.Empty
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