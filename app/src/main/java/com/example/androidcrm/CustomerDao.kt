package com.example.androidcrm

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CustomerDao {
    // New: Inserts a list of customers. Used when fetching from Firebase.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(customers: List<Customer>)

    @Insert
    suspend fun insertCustomer(customer: Customer)

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    // Updated: Now fetches customers based on the logged-in user's ID.
    @Query("SELECT * FROM customers WHERE userId = :userId ORDER BY name ASC")
    fun getAllCustomers(userId: String): LiveData<List<Customer>>

    // New: Gets a simple list (not LiveData) for uploading to Firebase.
    @Query("SELECT * FROM customers WHERE userId = :userId")
    suspend fun getAllCustomersList(userId: String): List<Customer>

    // New: Deletes all local customers for the current user.
    @Query("DELETE FROM customers WHERE userId = :userId")
    suspend fun clearAllForUser(userId: String)
}