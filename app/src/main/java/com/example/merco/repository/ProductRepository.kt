package com.example.merco.repository

import android.net.Uri
import android.util.Log
import com.example.merco.domain.model.Product
import com.example.merco.domain.model.DTO.CategoryDTO
import com.example.merco.domain.model.DTO.ProductDTO
import com.example.merco.domain.model.Order
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
    suspend fun getProductById(id: String): Product?

    suspend fun getProductsBySeller(sellerId: String): List<Product>

    suspend fun getById(productId: String): Product?
    suspend fun changeStock(product: Product, quantity: Int)

}

class ProductRepositoryImpl(
    private val productService: ProductService = ProductServicesImpl(),


    ) : ProductRepository {
/*
    override suspend fun changeStock(product:Product, quantity: Int) {
        try{
            Log.d("ProductRepository", "Changing stock for product: $product")
            if (product != null) {
            }
            if (product!= null) {
                Firebase.firestore
                    .collection("categories")
                    .document()
                    .collection("products")
                    .document(product.stock).set(newStock)
            }
        } catch (e: Exception) {
            throw e
        }

    }*/


    override suspend fun changeStock(product: Product, quantity: Int) {
        try {
            val firestore = Firebase.firestore

            val categoriesSnapshot = firestore.collection("categories").get().await()


            for (category in categoriesSnapshot.documents) {

                val productsSnapshot = category.reference.collection("products")
                    .whereEqualTo("id", product.id)
                    .get().await()

                if (!productsSnapshot.isEmpty) {

                    val productDoc = productsSnapshot.documents.first()
                    val currentStock = productDoc.getLong("stock") ?: 0

                    val newStock = currentStock - quantity
                    if (newStock < 0) {
                        throw Exception("Stock insuficiente para el producto con ID: ${product.id}")
                    }

                    productDoc.reference.update("stock", newStock).await()
                    Log.d("ProductRepository", "Stock actualizado para el producto: ${product.id}")
                    return
                }
            }

            // Si no se encontró el producto en ninguna categoría
            throw Exception("Producto no encontrado con ID: ${product.id}")
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error al cambiar el stock: ${e.message}", e)
            throw e
        }
    }





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

    override suspend fun getProductById(id: String): Product? {
        return try {
            val product = productService.getProductById(id)
            if (product == null) {
                Log.w("ProductRepositoryImpl", "No se encontró el producto con ID: $id")
            }
            product
        } catch (e: Exception) {
            Log.e("ProductRepositoryImpl", "Error al obtener el producto por ID: $id", e)
            null
        }
    }


    override suspend fun getById(productId: String): Product? {
        return try {
            val product= productService.getProductById(productId)
            if (product == null) {
                Log.w("ORDERRepositoryImpl", "No se encontró el ORDER con ID: $productId")
            }
            product
        } catch (e: Exception) {
            Log.e("ORDERRepositoryImpl", "Error al obtener el ORDER por ID: $productId", e)
            null
        }
    }


    override suspend fun getProductsByCategory(categoryId: String): List<Product> {
        Log.d("ProductRepository", "Fetching all products for category $categoryId")

        return try {

            val querySnapshot = Firebase.firestore
                .collection("categories") // Colección principal de categorías
                .document(categoryId)    // Documento de la categoría seleccionada
                .collection("products")  // Subcolección de productos
                .get()
                .await()


            val storage = Firebase.storage


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


                    val newPrice = price - (price * (discount / 100))


                    val imageUrl = storage.reference
                        .child("productsImages/$id")
                        .downloadUrl
                        .await()
                        .toString()


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
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching products: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getProductsBySeller(sellerId: String): List<Product> {
        Log.d("ProductRepository", "Fetching all products for seller $sellerId")
        return try {
            val categoriesSnapshot = Firebase.firestore
                .collection("categories")
                .get()
                .await()
            val storage = Firebase.storage
            val products = mutableListOf<Product>()

            for (categoryDocument in categoriesSnapshot.documents) {
                val categoryId = categoryDocument.id
                val productsSnapshot = Firebase.firestore
                    .collection("categories")
                    .document(categoryId)
                    .collection("products")
                    .whereEqualTo("sellerId", sellerId)
                    .get()
                    .await()
                products.addAll(
                    productsSnapshot.documents.mapNotNull { document ->
                        try {
                            val id = document.id
                            val name = document.getString("name") ?: "Sin nombre"
                            val description = document.getString("description") ?: ""
                            val price = document.getDouble("price") ?: 0.0
                            val discount = document.getDouble("discount") ?: 0.0
                            val stock = document.getLong("stock")?.toInt() ?: 0
                            val reason = document.getString("reason") ?: ""
                            val newPrice = price - (price * (discount / 100))

                            val imageUrl = storage.reference
                                .child("productsImages/$id")
                                .downloadUrl
                                .await()
                                .toString()

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
                            //Log.e("ProductRepository", "Error fetching image URL for product $SellerId: ${e.message}")
                            null
                        }
                    }
                )
            }
            products
        } catch (e: Exception) {
            Log.e("ProductRepository", "Error fetching products: ${e.message}")
            emptyList()
        }
    }



}















