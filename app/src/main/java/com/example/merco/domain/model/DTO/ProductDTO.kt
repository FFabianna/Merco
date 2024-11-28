package com.example.merco.domain.model.DTO

data class ProductDTO (
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val discount: Double= 0.0,
    val newPrice: Double = 0.0,
    val stock: Int = 0,
    val reason: String = "",
    val sellerId: String = "",
)
