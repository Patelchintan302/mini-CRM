package com.example.androidcrm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcrm.Orders
import com.example.androidcrm.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter(private val listener: onClickListener) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    private var orderList: List<Orders> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.tvId.text = order.id.toString()
        holder.tvCid.text = order.customer_id.toString()
        holder.tvOrderTitle.text = order.order_title
        holder.tvOrderAmount.text = String.format(Locale.US, "%.2f", order.order_amount)
        holder.tvOrderDate.text = formatDate(order.order_date)
    }

    override fun getItemCount(): Int = orderList.size

    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvId)
        val tvCid: TextView = itemView.findViewById(R.id.tvCid)
        val tvOrderTitle: TextView = itemView.findViewById(R.id.tvOrderTitle)
        val tvOrderAmount: TextView = itemView.findViewById(R.id.tvOrerAmount)
        val tvOrderDate: TextView = itemView.findViewById(R.id.tvOrderDate)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onClickItem(orderList[adapterPosition])
                }
            }
        }
    }

    fun setOrders(orders: List<Orders>) {
        this.orderList = orders
        notifyDataSetChanged()
    }

    fun getOrderAt(position: Int): Orders {
        return orderList[position]
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    interface onClickListener {
        fun onClickItem(order: Orders)
    }
}