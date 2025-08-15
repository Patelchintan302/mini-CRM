package com.example.androidcrm

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderDao {
    // New: Inserts a list of orders.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(orders: List<Orders>)

    @Insert
    suspend fun insertOrder(order: Orders)

    @Update
    suspend fun updateOrder(order: Orders)

    @Delete
    suspend fun deleteOrder(order: Orders)

    // Updated: Now fetches orders based on the logged-in user's ID.
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY order_date DESC")
    fun getAllOrders(userId: String): LiveData<List<Orders>>

    // New: Gets a simple list for uploading.
    @Query("SELECT * FROM orders WHERE userId = :userId")
    suspend fun getAllOrdersList(userId: String): List<Orders>

    // New: Deletes all local orders for the current user.
    @Query("DELETE FROM orders WHERE userId = :userId")
    suspend fun clearAllForUser(userId: String)
}