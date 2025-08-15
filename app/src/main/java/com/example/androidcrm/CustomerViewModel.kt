package com.example.androidcrm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CustomerViewModel(application: Application) : AndroidViewModel(application) {
    val allCustomers: LiveData<List<Customer>>
    private val repository: CustomerRepository

    init {
        val dao = AppDatabase.getInstance(application).customerDao()
        repository = CustomerRepository(dao)
        allCustomers = repository.allCustomers
    }

    // --- Local Operations (No changes here) ---
    fun insertCustomer(customer: Customer) = viewModelScope.launch { repository.insert(customer) }
    fun updateCustomer(customer: Customer) = viewModelScope.launch { repository.update(customer) }
    fun deleteCustomer(customer: Customer) = viewModelScope.launch { repository.delete(customer) }
    fun deleteAllCustomers() = viewModelScope.launch { repository.deleteAllCustomers() }

    // --- Sync Functions Exposed to the UI ---
    // This function will be called by the Activity when the user presses "Save to Cloud".
    fun saveToCloud() = viewModelScope.launch {
        repository.saveAllToCloud()
    }

    // This function will be called by the Activity when the user presses "Fetch from Cloud".
    fun fetchFromCloud() = viewModelScope.launch {
        repository.fetchAllFromCloud()
    }
}