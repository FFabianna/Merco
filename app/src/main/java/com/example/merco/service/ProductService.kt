package com.example.merco.service

import android.net.Uri
import android.util.Log
import com.example.merco.domain.model.Category
import com.example.merco.domain.model.DTO.CategoryDTO
import com.example.merco.domain.model.DTO.ProductDTO
import com.example.merco.domain.model.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


interface  ProductService {
    suspend fun createProduct(categoryId: String, productDto: ProductDTO,uid: String, image: Uri)
    suspend fun getProductById(id:String): Product?
    suspend fun getProductByName(name: String): Product?
    suspend fun loadCurrentProduct(uid: String): Product?
    suspend fun addImageProduct(imageUri: Uri, productId: String)

}

class ProductServicesImpl:ProductService {

    override suspend fun addImageProduct(imageUri: Uri, productId: String) {
        try {
            val storageReference = Firebase.storage.reference
                .child("productsImages/$productId")
            val uploadTask = storageReference.putFile(imageUri).await()

            val downloadUrl = storageReference.downloadUrl.await()
            Log.d("FirebaseService", "Image uploaded successfully: $downloadUrl")
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error uploading image: ${e.message}", e)
            throw e // Propaga el error para que el repositorio lo maneje
        }
    }


    override suspend fun createProduct(categoryId: String, updatedProduct: ProductDTO,uid: String ,image: Uri) {
        Log.d("ProductServicesImpl", "Creando producto con nombre: ${updatedProduct.name}")

        try {
            // Genera un ID único automáticamente
            val documentReference = Firebase.firestore
                .collection("categories")
                .document(categoryId)
                .collection("products")
                .document() // Esto crea un DocumentReference con un ID único, pero no escribe todavía.

            val generatedId = documentReference.id // Obtienes el ID autogenerado

            val product = Product(id = generatedId,name = updatedProduct.name,
                description = updatedProduct.description,
                price = updatedProduct.price,
                discount = updatedProduct.discount,
                newPrice = updatedProduct.price - (updatedProduct.price * (updatedProduct.discount / 100.0)),
                stock = updatedProduct.stock,
                reason = updatedProduct.reason,
                sellerId = uid,
                imageId = generatedId)


            // Guarda el documento con el ID autogenerado y el campo `id` actualizado
            documentReference
                .set(product)
                .await()

            Log.d("ProductServicesImpl", "Product creada con éxito con ID: $generatedId")

            // Sube la imagen asociada
            addImageProduct(image, generatedId)

        } catch (e: Exception) {
            Log.e("ProductServicesImpl", "Error al crear el product: ${e.message}")
        }
    }


    override suspend fun loadCurrentProduct(productId: String): Product? {
        Log.d("CategoryServicesImpl", "Cargando producto con ID: $productId")

        val product = Firebase.firestore
            .collection("categories")
            .document(productId)
            .get()
            .await()
        val productObject = product.toObject(Product::class.java)
        return productObject
    }


    override suspend fun getProductById(id: String): Product? {
        Log.d("ProductServicesImpl", "getProductyById: $id")

        val product= Firebase.firestore
            .collection("categories")
            .document(id)
            .get()
            .await()
        val productObject = product.toObject(Product::class.java)
        return productObject
    }


    override suspend fun getProductByName(name: String): Product? {
        val product = Firebase.firestore
            .collection("categories")
            .document(name)
            .get()
            .await()

        val productObject = product.toObject(Product::class.java)
        return productObject
    }
}

