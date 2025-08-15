package com.example.androidcrm

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

class CustomerRepository(private val customerDao: CustomerDao) {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("customers")
    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val allCustomers: LiveData<List<Customer>> = customerDao.getAllCustomers(userId)

    // --- Local Operations ---
    suspend fun insert(customer: Customer) {
        customer.userId = userId
        customerDao.insertCustomer(customer)
    }

    suspend fun update(customer: Customer) {
        // FIX: Ensure the userId is always set on the object before updating.
        // This prevents the userId from being accidentally overwritten with a blank value.
        customer.userId = userId
        customerDao.updateCustomer(customer)
    }

    suspend fun delete(customer: Customer) {
        customerDao.deleteCustomer(customer)
    }

    suspend fun deleteAllCustomers() {
        if (userId.isNotEmpty()) {
            customerDao.clearAllForUser(userId)
        }
    }

    // --- Manual Sync Functions ---
    suspend fun saveAllToCloud() {
        if (userId.isEmpty()) return
        try {
            val localCustomers = customerDao.getAllCustomersList(userId)
            val dataMap = localCustomers.associateBy { it.id.toString() }
            database.child(userId).setValue(dataMap).await()
            Log.d("CustomerRepository", "Successfully saved all customers to Firebase.")
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error saving customers to cloud", e)
        }
    }

    suspend fun fetchAllFromCloud() {
        if (userId.isEmpty()) return
        try {
            val dataSnapshot = database.child(userId).get().await()
            val customersFromFirebase = mutableListOf<Customer>()
            for (snapshot in dataSnapshot.children) {
                val customer = snapshot.getValue(Customer::class.java)
                if (customer != null) {
                    customer.id = snapshot.key?.toIntOrNull() ?: 0
                    customersFromFirebase.add(customer)
                }
            }

            customerDao.clearAllForUser(userId)

            if (customersFromFirebase.isNotEmpty()) {
                customerDao.insertAll(customersFromFirebase)
            }
            Log.d("CustomerRepository", "Successfully synced from Firebase.")
        } catch (e: Exception) {
            Log.e("CustomerRepository", "Error fetching customers from cloud", e)
        }
    }
}