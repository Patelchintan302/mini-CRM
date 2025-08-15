package com.example.androidcrm

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

class OrderRepository(private val orderDao: OrderDao) {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("orders")
    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val allOrders: LiveData<List<Orders>> = orderDao.getAllOrders(userId)

    // --- Local Operations ---
    suspend fun insert(order: Orders) {
        order.userId = userId
        orderDao.insertOrder(order)
    }

    suspend fun update(order: Orders) {
        // FIX: Ensure the userId is always set on the object before updating.
        order.userId = userId
        orderDao.updateOrder(order)
    }

    suspend fun delete(order: Orders) {
        orderDao.deleteOrder(order)
    }

    suspend fun deleteAllOrders() {
        if (userId.isNotEmpty()) {
            orderDao.clearAllForUser(userId)
        }
    }

    // --- Manual Sync Functions ---
    suspend fun saveAllToCloud() {
        if (userId.isEmpty()) return
        try {
            val localOrders = orderDao.getAllOrdersList(userId)
            val dataMap = localOrders.associateBy { it.id.toString() }
            database.child(userId).setValue(dataMap).await()
            Log.d("OrderRepository", "Successfully saved all orders to Firebase.")
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error saving orders to cloud", e)
        }
    }

    suspend fun fetchAllFromCloud() {
        if (userId.isEmpty()) return
        try {
            val dataSnapshot = database.child(userId).get().await()
            val ordersFromFirebase = mutableListOf<Orders>()
            for (snapshot in dataSnapshot.children) {
                val order = snapshot.getValue(Orders::class.java)
                if (order != null) {
                    order.id = snapshot.key?.toIntOrNull() ?: 0
                    ordersFromFirebase.add(order)
                }
            }

            orderDao.clearAllForUser(userId)

            if (ordersFromFirebase.isNotEmpty()) {
                orderDao.insertAll(ordersFromFirebase)
            }
            Log.d("OrderRepository", "Successfully synced from Firebase.")
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching orders from cloud", e)
        }
    }
}