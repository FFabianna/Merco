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


    suspend fun getImageUrl(imageId: String): String
    suspend fun getOrderById(id:String): Order?

}

class OrderServicesImpl(
    private val storage: FirebaseStorage = Firebase.storage)
    :OrderService {



    override suspend fun getImageUrl(imageId: String): String {
        return storage.reference.child("images/$imageId").downloadUrl.await().toString()
    }


    override suspend fun getOrderById(id: String): Order? {
        Log.d("ORDERServicesImpl", "getORDERById: $id")


        val order = Firebase.firestore
            .collection("orders")
            .document(id)
            .get()
            .await()


        return if (order.exists()) {

            order.toObject(Order::class.java)
        } else {

            Log.e("ORDERServicesImpl", "Pedido con ID $id no encontrado.")
            null
        }
    }



}