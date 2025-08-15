package com.example.androidcrm

import androidx.lifecycle.LiveData

class CustomerRepository(private val customerDao: CustomerDao) {
    val allCustomers: LiveData<List<Customer>> = customerDao.getAllCustomers()

    suspend fun insert(customer: Customer) {
        customerDao.insertCustomer(customer)
    }

    suspend fun update(customer: Customer) {
        customerDao.updateCustomer(customer)
    }

    suspend fun delete(customer: Customer) {
        customerDao.deleteCustomer(customer)
    }

    suspend fun deleteAllCustomers() {
        customerDao.deleteAllCustomers()
    }
}