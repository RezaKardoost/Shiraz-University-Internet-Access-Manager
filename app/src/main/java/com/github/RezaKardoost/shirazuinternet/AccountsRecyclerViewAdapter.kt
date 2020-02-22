package com.github.RezaKardoost.shirazuinternet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AccountsRecyclerViewAdapter(var accounts:List<Account>): RecyclerView.Adapter<AccountsRecyclerViewAdapter.AccountViewHolder>() {

    class AccountViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val username = itemView.findViewById<TextView>(R.id.username)!!
        val capacity = itemView.findViewById<TextView>(R.id.capacity)!!

    }

    fun update(accounts:List<Account>){

        this.accounts = accounts
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_row,parent,false)
        return AccountViewHolder(itemView)
    }

    override fun getItemCount(): Int = accounts.size

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.username.text = "نام کاربری: ${accounts[position].username}"
        val creditInGigaByte =  "%.2f".format(accounts[position].capacity / 1024)
        holder.capacity.text = "حجم باقیمانده: $creditInGigaByte"
    }
}