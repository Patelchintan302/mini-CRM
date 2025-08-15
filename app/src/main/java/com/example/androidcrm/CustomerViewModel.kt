package com.example.androidcrm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CustomerViewModel(application: Application) : AndroidViewModel(application) {
    // FIX: Changed MutableList to List to match the repository's return type.
    val allCustomers: LiveData<List<Customer>>
    private val repository: CustomerRepository

    init {
        val dao = AppDatabase.getInstance(application).customerDao()
        repository = CustomerRepository(dao)
        allCustomers = repository.allCustomers
    }

    fun insertCustomer(customer: Customer) = viewModelScope.launch {
        repository.insert(customer)
    }

    fun updateCustomer(customer: Customer) = viewModelScope.launch {
        repository.update(customer)
    }

    fun deleteCustomer(customer: Customer) = viewModelScope.launch {
        repository.delete(customer)
    }

    fun deleteAllCustomers() = viewModelScope.launch {
        repository.deleteAllCustomers()
    }
}