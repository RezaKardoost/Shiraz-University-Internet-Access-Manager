package com.github.RezaKardoost.shirazuinternet.ui.main

import java.lang.Exception

sealed class AccountsResult<out T>(
    val data:T? = null,
    val exception: Exception? = null
    ) {
    class Success<out T>(data:T):
        AccountsResult<T>(data)
    object Empty:
        AccountsResult<Nothing>()
    class Loading<out T>(data:T? = null):
        AccountsResult<T>(data)
    class Error<out T>(exception: Exception,data:T? = null):
        AccountsResult<T>(data,exception)
}