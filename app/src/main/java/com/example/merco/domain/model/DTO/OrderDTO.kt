package com.example.merco.domain.model.DTO

data class OrderDTO (
    val id: String = "",  // Firestore usa un ID autogenerado o manual
    val productId: String ="",
    val customerId: String ="",
    val sellerId: String="",
    val quantity: Int,
    val totalPrice: Double,
    val storeName: String="",
    val imageId: String="",
    val productName: String="",
    val storeAddress: String="",
    var status: String=""

)