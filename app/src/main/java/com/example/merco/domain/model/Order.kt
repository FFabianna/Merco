package com.example.merco.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: String = "",
    val productId: String = "",
    val customerId: String = "",
    val sellerId: String = "",
    val quantity: Int = 0,
    val totalPrice: Double = 0.0,
    val storeName: String = "",
    val imageId: String = "",
    val productName: String = "",
    val storeAddress: String = "",
    var status: String = "pendiente"
)
