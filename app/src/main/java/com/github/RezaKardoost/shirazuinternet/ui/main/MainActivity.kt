package com.github.RezaKardoost.shirazuinternet.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.github.RezaKardoost.shirazuinternet.Account
import com.github.RezaKardoost.shirazuinternet.R
import com.github.RezaKardoost.shirazuinternet.Room.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

class MainActivity : AppCompatActivity(),
    AccountsRecyclerViewAdapter.OnItemListener {

    private val model: MainActivityViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null){
            emptyView.visibility = savedInstanceState.getInt("emptyViewVisibility")
            accountsRecyclerView.visibility = savedInstanceState.getInt("accountsRecyclerViewVisibility")
        }


        model.accountsLiveData.observe(this, Observer {
            if(it.data != null){
                if(accountsRecyclerView.adapter == null){
                    initialRecyclerView(it.data)
                }else{
                    (accountsRecyclerView.adapter as AccountsRecyclerViewAdapter).update(it.data)
                }
            }

            when(it){
                is AccountsResult.Success -> {
                    loadingView.isRefreshing = false
                    emptyView.visibility = View.INVISIBLE
                    accountsRecyclerView.visibility = View.VISIBLE
                    addFab.isEnabled = true
                }
                AccountsResult.Empty -> {
                    loadingView.isRefreshing = false
                    emptyView.visibility = View.VISIBLE
                    accountsRecyclerView.visibility = View.INVISIBLE
                    addFab.isEnabled = true
                }
                is AccountsResult.Loading -> {
                    loadingView.isRefreshing = true
                    addFab.isEnabled = false

                }
                is AccountsResult.Error -> {
                    loadingView.isRefreshing = false
                    Toast.makeText(this@MainActivity,it.exception?.message,Toast.LENGTH_SHORT).show()
                    addFab.isEnabled = true
                }
            }

        })

        addFab.setOnClickListener {
            showBottomSheet()
        }

        loadingView.setOnRefreshListener {
            model.getOrUpdateAccounts()
        }

    }

    private fun showBottomSheet(){
        val bottomSheetView = View.inflate(this,
            R.layout.add_account_bottomsheet,null)
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
            bottomSheetDialog.dismiss()
        }
    }

    private fun initialRecyclerView(accounts: MutableList<Account>) {

        accountsRecyclerView.adapter =
            AccountsRecyclerViewAdapter(
                accounts,
                this
            )
        accountsRecyclerView.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("emptyViewVisibility",emptyView.visibility)
        outState.putInt("accountsRecyclerViewVisibility",accountsRecyclerView.visibility)
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun remove(username: String) {//remove recycler item
        model.removeUser(username)
    }
}

