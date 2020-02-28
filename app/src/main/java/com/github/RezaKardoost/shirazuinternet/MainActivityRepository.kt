package com.github.RezaKardoost.shirazuinternet

import android.app.Application
import com.github.RezaKardoost.shirazuinternet.Room.AccountsDatabase
import com.github.RezaKardoost.shirazuinternet.Room.User
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class MainActivityRepository(application: Application) {
    private val dbInstance = AccountsDatabase.getInstance(application)

    suspend fun getAllUsers(): MutableList<User> {
        return dbInstance.userDao().getAll()
    }

    suspend fun addNewUser(user: User){
        dbInstance.userDao().insert(user)
    }

    suspend fun removeUser(username: String){
        dbInstance.userDao().remove(username)
    }

    suspend fun getPages(users:MutableList<User>):MutableList<Document> =
        suspendCancellableCoroutine {  continuation ->

            val pages = mutableListOf<Document>()

            try {
                for(user in users){
                    val doc: Document = Jsoup.connect("https://account.shirazu.ac.ir/IBSng/user/")
                        .requestBody("normal_username=${user.userName}&normal_password=${user.passWord}&lang=fa&x=25&y=15")
                        .userAgent("Mozilla")
                        .post()

                    pages.add(doc)
                 }
                continuation.resume(pages)
            }catch (ex:Exception){
                continuation.resumeWithException(ex)
            }


        }

    fun getAccounts(
        pages: MutableList<Document>,
        users: MutableList<User>
    ):MutableList<Account>{
        val accounts = mutableListOf<Account>()
        for (i in 0..pages.lastIndex){
            val p = pages[i]
            if (p.title() != "IBSng | ورود به سیستم"){

                val account = Account()
                account.isLogin = true

                account.username = p.getElementsByClass("Form_Content_Row_Right_2Col_light")[0].text()
                account.credit = correctCredit(p.getElementsByClass("Form_Content_Row_Right_2Col_light")[1].text())

                accounts.add(account)

            }else{

                val account = Account()
                account.isLogin = false
                account.username = users[i].userName
                accounts.add(account)

            }
        }

        return accounts

    }

    fun correctCredit(credit:String): Float {
        val temp = credit.replace(",","")
        return temp.substring(0,temp.length-6).toFloat()
    }
}