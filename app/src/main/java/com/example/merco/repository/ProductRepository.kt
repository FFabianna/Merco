package com.example.merco.repository

import android.net.Uri
import android.util.Log
import com.example.merco.domain.model.Product
import com.example.merco.domain.model.DTO.CategoryDTO
import com.example.merco.domain.model.DTO.ProductDTO
import com.example.merco.service.CategoryService
import com.example.merco.service.CategoryServicesImpl
import com.example.merco.service.ProductService
import com.example.merco.service.ProductServicesImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


interface ProductRepository {




    suspend fun getCurrentProduct(): Product?
    //suspend fun getProductByName(name: String): Product?
    //suspend fun getAllProducts(): List<Product>
    suspend fun addProduct(categoryId: String, updatedProduct: ProductDTO, image: Uri)
    suspend fun getProductsByCategory(categoryId: String): List<Product>


}

class ProductRepositoryImpl(
    private val productService: ProductService = ProductServicesImpl(),



    ) : ProductRepository {

    override suspend fun addProduct(categoryId:String, updatedProduct:ProductDTO, image: Uri) {
        val uid = Firebase.auth.currentUser?.uid
        Log.v("AuthRepositoryImpl", "UID: $uid")

        productService.createProduct(categoryId, updatedProduct,uid.toString(), image)
    }


    override suspend fun getCurrentProduct(): Product? {
        val product = productService.loadCurrentProduct(Firebase.auth.uid!!)
        Log.v("ProductRepositoryImpl", "product: $product")
        return product
    }

/*
    override suspend fun getAllProducts(): List<Product> {
        Log.d("ProductRepository", "Fetching all products")
        return try {

            val querySnapshot = Firebase.firestore
                .collection("categories")
                .get()
                .await()

            val storage = Firebase.storage
            querySnapshot.documents.mapNotNull { document ->
                val id = document.id
                val name = document.getString("name") ?: "Sin nombre"

                try {
                    val imageUrl = storage.reference
                        .child("productsImages/$id")
                        .downloadUrl
                        .await()
                        .toString()


                    Product(id = id, name = name, imageId = imageUrl)
                } catch (e: Exception) {
                    Log.e(
                        "ProductRepository",
                        "Error fetching image URL for product$id: ${e.message}"
                    )
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching products: ${e.message}")
            emptyList()
        }
    }*/
    override suspend fun getProductsByCategory(categoryId: String): List<Product> {
        Log.d("ProductRepository", "Fetching all products for category $categoryId")

        return try {
            // Obtén los productos de la categoría desde Firestore
            val querySnapshot = Firebase.firestore
                .collection("categories") // Colección principal de categorías
                .document(categoryId)    // Documento de la categoría seleccionada
                .collection("products")  // Subcolección de productos
                .get()
                .await()

            // Instancia de Firebase Storage
            val storage = Firebase.storage

            // Mapea los documentos a objetos Product
            querySnapshot.documents.mapNotNull { document ->
                try {
                    val id = document.id
                    val name = document.getString("name") ?: "Sin nombre"
                    val description = document.getString("description") ?: ""
                    val price = document.getDouble("price") ?: 0.0
                    val discount = document.getDouble("discount") ?: 0.0
                    val stock = document.getLong("stock")?.toInt() ?: 0
                    val reason = document.getString("reason") ?: ""
                    val sellerId = document.getString("sellerId") ?: ""

                    // Calcula el precio con descuento
                    val newPrice = price - (price * (discount / 100))

                    // Obtén la URL de la imagen desde Firebase Storage
                    val imageUrl = storage.reference
                        .child("productsImages/$id") // Ruta en Firebase Storage
                        .downloadUrl
                        .await()
                        .toString()

                    // Devuelve el objeto Product
                    Product(
                        id = id,
                        name = name,
                        description = description,
                        price = price,
                        discount = discount,
                        newPrice = newPrice,
                        stock = stock,
                        reason = reason,
                        sellerId = sellerId,
                        imageId = imageUrl
                    )
                } catch (e: Exception) {
                    //Log.e("ProductRepository", "Error fetching image URL for product $id: ${e.message}")
                    null // Ignora productos con problemas para obtener la imagen o datos incompletos
                }
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching products: ${e.message}")
            emptyList()
        }
    }


}















