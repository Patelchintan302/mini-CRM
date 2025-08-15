package com.example.androidcrm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcrm.Customer
import com.example.androidcrm.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerAdapter(private val listener: onClickListener) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {
    private var customerList: List<Customer> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_customer, parent, false)
        return CustomerViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        val customer = customerList[position]
        holder.tvId.text = customer.id.toString()
        holder.tvName.text = customer.name
        holder.tvCompany.text = customer.company
        holder.tvEmail.text = customer.email
        holder.tvPhone.text = customer.phone
        holder.tvCreatedAt.text = formatDate(customer.created_at)
    }

    override fun getItemCount(): Int = customerList.size

    inner class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvCompany: TextView = itemView.findViewById(R.id.tvCompany)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvPhone: TextView = itemView.findViewById(R.id.tvPhone)
        val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClickItem(customerList[adapterPosition])
                }
            }
        }
    }

    fun setCustomers(customers: List<Customer>) {
        this.customerList = customers
        notifyDataSetChanged()
    }

    fun getCustomerAt(position: Int): Customer {
        return customerList[position]
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    interface onClickListener {
        fun onClickItem(customer: Customer)
    }
}