package com.example.androidcrm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    val allOrders: LiveData<List<Orders>>
    private val repository: OrderRepository

    init {
        val dao = AppDatabase.getInstance(application).orderDao()
        repository = OrderRepository(dao)
        allOrders = repository.allOrders
    }

    fun insertOrder(order: Orders) = viewModelScope.launch {
        repository.insert(order)
    }

    fun updateOrder(order: Orders) = viewModelScope.launch {
        repository.update(order)
    }

    fun deleteOrder(order: Orders) = viewModelScope.launch {
        repository.delete(order)
    }

    fun deleteAllOrders() = viewModelScope.launch {
        repository.deleteAllOrders()
    }
}