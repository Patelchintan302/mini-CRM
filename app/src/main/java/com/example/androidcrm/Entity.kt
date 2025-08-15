package com.example.androidcrm

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude

@Entity(tableName = "customers")
data class Customer(
    // 1. Default values added for Firebase compatibility
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var company: String = "",
    var created_at: Long = System.currentTimeMillis(),

    // 2. userId field added to link data to a user
    var userId: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    // 3. This helper function prepares the object for Firebase
    @Exclude // This tells Firebase to ignore this function when reading data
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "company" to company,
            "created_at" to created_at,
            "userId" to userId
            // Note: The local 'id' is NOT included here
        )
    }
}

@Entity(
    tableName = "orders",
    foreignKeys = [ForeignKey(
        entity = Customer::class,
        parentColumns = ["id"],
        childColumns = ["customer_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["customer_id"])]
)
data class Orders(
    var customer_id: Int = 0,
    var order_title: String = "",
    var order_amount: Double = 0.0, // Corrected data type
    var order_date: Long = System.currentTimeMillis(),
    var userId: String = ""
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "customer_id" to customer_id,
            "order_title" to order_title,
            "order_amount" to order_amount,
            "order_date" to order_date,
            "userId" to userId
        )
    }
}