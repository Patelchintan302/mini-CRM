package com.example.androidcrm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Orders::class , Customer::class], version = 1, exportSchema = false)
abstract class OrderDB : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun customerDao(): CustomerDao

    companion object {
        private var INSTANCE: OrderDB? = null
        fun getInstance(context: Context): OrderDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OrderDB::class.java,
                    "order_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}