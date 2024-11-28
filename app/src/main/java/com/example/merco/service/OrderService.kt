package com.example.merco.service

import android.net.Uri
import android.util.Log
import com.example.merco.domain.model.Category
import com.example.merco.domain.model.DTO.ProductDTO
import com.example.merco.domain.model.Order
import com.example.merco.domain.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


interface  OrderService {

    suspend fun createOrder(order: Order, onSuccess: () -> Unit, onFailure: (Exception) -> Unit)
    suspend fun getImageUrl(imageId: String): String
    suspend fun getOrderById(id:String): Order?

}

class OrderServicesImpl(
    private val storage: FirebaseStorage = Firebase.storage)
    :OrderService {


    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun createOrder(order: Order, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val orderMap = mapOf(
            "productId" to order.productId,
            "customerId" to order.customerId,
            "sellerId" to order.sellerId,
            "quantity" to order.quantity,
            "totalPrice" to order.totalPrice,
            "storeName" to order.storeName,
            "imageId" to order.imageId,
            "status" to order.status
        )

        firestore.collection("orders")
            .add(orderMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception)
            }
    }

    override suspend fun getImageUrl(imageId: String): String {
        return storage.reference.child("images/$imageId").downloadUrl.await().toString()
    }


    override suspend fun getOrderById(id: String): Order? {
        Log.d("ORDERServicesImpl", "getORDERById: $id")

        // Obtén el pedido desde Firestore
        val order = Firebase.firestore
            .collection("orders")
            .document(id)
            .get()
            .await()

        // Verifica si se encontró el pedido
        return if (order.exists()) {
            // Si existe, convierte el documento a un objeto de tipo Order
            order.toObject(Order::class.java)
        } else {
            // Si no existe, loguea el error o devuelve null
            Log.e("ORDERServicesImpl", "Pedido con ID $id no encontrado.")
            null
        }
    }



}