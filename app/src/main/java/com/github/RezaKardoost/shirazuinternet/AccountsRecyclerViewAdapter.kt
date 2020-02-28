package com.github.RezaKardoost.shirazuinternet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AccountsRecyclerViewAdapter(var accounts:MutableList<Account>,var onItemListener:OnItemListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val IS_LOGIN_TYPE = 0
    val IS_NOT_LOGIN_TYPE = 1
    inner class IsLoginAccountViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val username = itemView.findViewById<TextView>(R.id.username)!!
        val capacity = itemView.findViewById<TextView>(R.id.capacity)!!
        val removeAccount = itemView.findViewById<ImageButton>(R.id.removeAccount)!!

        init {
            removeAccount.setOnClickListener {
                onItemListener.remove(accounts[adapterPosition].username)
                accounts.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

    }

    inner class IsNotLoginAccountViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val username = itemView.findViewById<TextView>(R.id.username)!!
        val removeAccount = itemView.findViewById<ImageButton>(R.id.removeAccount)!!

        init {
            removeAccount.setOnClickListener {
                onItemListener.remove(accounts[adapterPosition].username)
                accounts.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }

    }

    fun update(accounts:MutableList<Account>){

        this.accounts = accounts
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val is_Login_itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_row,parent,false)
        val is_Not_Login_itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.account_row_not_logedin,parent,false)
        return if (viewType == IS_LOGIN_TYPE){
            IsLoginAccountViewHolder(is_Login_itemView)
        }else{
            IsNotLoginAccountViewHolder(is_Not_Login_itemView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(accounts[position].isLogin){
            IS_LOGIN_TYPE
        }else{
            IS_NOT_LOGIN_TYPE
        }
    }

    override fun getItemCount(): Int = accounts.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(holder is IsLoginAccountViewHolder){
            holder.username.text = "نام کاربری: ${accounts[position].username}"
            val creditInGigaByte =  "%.2f".format(accounts[position].credit / 1024)
            holder.capacity.text = "حجم باقیمانده: $creditInGigaByte"
        }else if(holder is IsNotLoginAccountViewHolder){
            holder.username.text = "نام کاربری: ${accounts[position].username}"
        }

    }

    interface OnItemListener{
        fun remove(username:String)
    }
}