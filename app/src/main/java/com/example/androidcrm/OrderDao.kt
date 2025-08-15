package com.example.androidcrm

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderDao {
    @Insert
    suspend fun insertOrder(order: Orders)

    @Update
    suspend fun updateOrder(order: Orders)

    @Delete
    suspend fun deleteOrder(order: Orders)

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()

    @Query("SELECT * FROM orders ORDER BY order_date DESC")
    fun getAllOrders(): LiveData<List<Orders>>
}