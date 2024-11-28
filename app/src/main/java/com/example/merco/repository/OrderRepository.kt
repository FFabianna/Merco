package com.example.merco.repository

import android.util.Log
import com.example.merco.domain.model.Category
import com.example.merco.domain.model.DTO.OrderDTO
import com.example.merco.domain.model.Order
import com.example.merco.domain.model.Product
import com.example.merco.service.OrderService
import com.example.merco.service.OrderServicesImpl
import com.example.merco.viewmodel.ProductViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


interface OrderRepository {

    suspend fun createOrder(order: Order)
    suspend fun getOrdersByUserId(userId: String): List<Order>
    suspend fun getAllOrders(userId: String): List<Order>
    suspend fun getOrderById(id: String): Order?
    suspend fun getAllOrdersU(userId: String): List<Order>
    suspend fun changeStatus(order: Order)
}

class OrderRepositoryImpl(
    private val orderService: OrderService = OrderServicesImpl(),
    private val firestoree: FirebaseFirestore = Firebase.firestore,
    private val repository: ProductRepository = ProductRepositoryImpl()


    ) : OrderRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun changeStatus(order: Order) {
        try{
            Log.d("OrderRepository", "Changing status for order: $order")
            if (order != null) {
                order.status = "Entregado"
            }
            if (order != null) {
                firestore.collection("orders").document(order.id).set(order)
            }
        } catch (e: Exception) {
            throw e
        }
    }


    override suspend fun createOrder(order: Order) {
        try {
            val documentReference = Firebase.firestore
                .collection("orders")
                .document()
            val generatedId = documentReference.id
            val orderData = mapOf(
                "id" to generatedId,
                "productId" to order.productId,
                "customerId" to order.customerId,
                "sellerId" to order.sellerId,
                "quantity" to order.quantity,
                "totalPrice" to order.totalPrice,
                "storeName" to order.storeName,
                "storeAddress" to order.storeAddress,
                "imageId" to order.imageId,
                "productName" to order.productName,
                "status" to order.status
            )
            documentReference
                .set(orderData)
                .await()



        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getOrdersByUserId(userId: String): List<Order> {
        val snapshot = firestoree.collection("orders")
            .whereEqualTo("customerId", userId)
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Order::class.java)?.copy(
                id = doc.id ,
                imageId = orderService.getImageUrl(doc.getString("imageId") ?: "")
            )
        }
    }

    override suspend fun getAllOrders(userId: String): List<Order> {
        Log.d("OrderRepository", "Fetching all orders for user: $userId")
        Log.d("OrderRepository", "Fetching all orders")

        return try {

            val querySnapshot = Firebase.firestore
                .collection("orders")
                .whereEqualTo("sellerId", userId)
                .get()
                .await()

            val storage = Firebase.storage

            querySnapshot.documents.mapNotNull { document ->
                try {
                    val id = document.id
                    val productName = document.getString("productName") ?: "Sin nombre"
                    val quantity = document.getLong("quantity")?.toInt() ?: 0
                    val totalPrice = document.getDouble("totalPrice")?: 0.0
                    val storeName = document.getString("storeName") ?: "Sin nombre"
                    val storeAddress = document.getString("storeAddress") ?: "Sin dirección"
                    val imageId = document.getString("imageId") ?: ""
                    val sellerId = document.getString("sellerId") ?: ""
                    val customerId = document.getString("customerId") ?: ""
                    val productId = document.getString("productId") ?: ""
                    val status = document.getString("status") ?: "pendiente"


                    val imageUrl = storage.reference
                        .child("productsImages/$imageId")
                        .downloadUrl
                        .await()
                        .toString()

                    Order(
                        id = id,
                        productName = productName,
                        quantity = quantity,
                        totalPrice = totalPrice,
                        storeName = storeName,
                        storeAddress = storeAddress,
                        imageId = imageUrl,
                        sellerId = sellerId,
                        customerId = customerId,
                        productId = productId,
                        status = status
                    )
                } catch (e: Exception) {
                    Log.e("OrderRepository", "Error fetching order $document.id: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching orders: ${e.message}")
            emptyList()
        }


    }

    override suspend fun getAllOrdersU(userId: String): List<Order> {
        Log.d("OrderRepository", "Fetching all orders for user: $userId")
        Log.d("OrderRepository", "Fetching all orders")

        return try {

            val querySnapshot = Firebase.firestore
                .collection("orders")
                .whereEqualTo("customerId", userId)
                .get()
                .await()

            val storage = Firebase.storage

            querySnapshot.documents.mapNotNull { document ->
                try {
                    val id = document.id
                    val productName = document.getString("productName") ?: "Sin nombre"
                    val quantity = document.getLong("quantity")?.toInt() ?: 0
                    val totalPrice = document.getDouble("totalPrice")?: 0.0
                    val storeName = document.getString("storeName") ?: "Sin nombre"
                    val storeAddress = document.getString("storeAddress") ?: "Sin dirección"
                    val imageId = document.getString("imageId") ?: ""
                    val sellerId = document.getString("sellerId") ?: ""
                    val customerId = document.getString("customerId") ?: ""
                    val productId = document.getString("productId") ?: ""
                    val status = document.getString("status") ?: "pendiente"


                    val imageUrl = storage.reference
                        .child("productsImages/$imageId")
                        .downloadUrl
                        .await()
                        .toString()

                    Order(
                        id = id,
                        productName = productName,
                        quantity = quantity,
                        totalPrice = totalPrice,
                        storeName = storeName,
                        storeAddress = storeAddress,
                        imageId = imageUrl,
                        sellerId = sellerId,
                        customerId = customerId,
                        productId = productId,
                        status = status
                    )
                } catch (e: Exception) {
                    Log.e("OrderRepository", "Error fetching order $document.id: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching orders: ${e.message}")
            emptyList()
        }


    }

    override suspend fun getOrderById(id: String): Order? {
        return try {
            val order = orderService.getOrderById(id)
            if (order == null) {
                Log.w("ORDERRepositoryImpl", "No se encontró el ORDER con ID: $id")
            }
            order
        } catch (e: Exception) {
            Log.e("ORDERRepositoryImpl", "Error al obtener el ORDER por ID: $id", e)
            null
        }
    }

}