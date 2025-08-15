package com.example.androidcrm

import androidx.lifecycle.LiveData

class OrderRepository(private val orderDao: OrderDao) {
    val allOrders: LiveData<List<Orders>> = orderDao.getAllOrders()

    suspend fun insert(order: Orders) {
        orderDao.insertOrder(order)
    }

    suspend fun update(order: Orders) {
        orderDao.updateOrder(order)
    }

    suspend fun delete(order: Orders) {
        orderDao.deleteOrder(order)
    }

    suspend fun deleteAllOrders() {
        orderDao.deleteAllOrders()
    }
}