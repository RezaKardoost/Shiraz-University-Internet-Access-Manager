package com.github.RezaKardoost.shirazuinternet

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.github.RezaKardoost.shirazuinternet.Room.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        model.accounts.observe(this, Observer {
            if (accountsRecyclerView.adapter == null){
                initialRecyclerView(it)
            }else{
                (accountsRecyclerView.adapter as AccountsRecyclerViewAdapter).update(it)
            }

        })

        model.isLoading.observe(this, Observer {
            loadingView.isRefreshing = it
        })

        addFab.setOnClickListener {
            showBottomSheet()
        }

        loadingView.setOnRefreshListener {
            model.getOrUpdateAccounts()
        }

    }

    private fun showBottomSheet(){
        val bottomSheetView = View.inflate(this,R.layout.add_account_bottomsheet,null)
        val addAccountBTN = bottomSheetView.findViewById<Button>(R.id.addAccount)
        val username = bottomSheetView.findViewById<EditText>(R.id.username)
        val password = bottomSheetView.findViewById<EditText>(R.id.password)
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()

        addAccountBTN.setOnClickListener {
            val newUser = User(userName = username.text.toString(),passWord = password.text.toString())
            if(!model.validateAccount(newUser)){
                Toast.makeText(this@MainActivity,"نام کاربری یا رمز عبور اشتباه است",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            model.addNewUser(newUser)
            model.getOrUpdateAccounts()
            bottomSheetDialog.dismiss()
        }
    }

    private fun initialRecyclerView(accounts: List<Account>) {

        accountsRecyclerView.adapter = AccountsRecyclerViewAdapter(accounts)

    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }
}

