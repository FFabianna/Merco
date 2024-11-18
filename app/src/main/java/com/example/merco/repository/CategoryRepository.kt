package com.example.merco.repository

import android.net.Uri
import android.util.Log
import com.example.merco.domain.model.Category
import com.example.merco.domain.model.DTO.CategoryDTO
import com.example.merco.domain.model.Seller
import com.example.merco.domain.model.User
import com.example.merco.service.CategoryService
import com.example.merco.service.CategoryServicesImpl
import com.example.merco.service.UserServices
import com.example.merco.service.UserServicesImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID


interface CategoryRepository {


    //suspend fun createCategory(category: Category)
    suspend fun getCurrentCategory(): Category?
    suspend fun getCategoryByName(name: String): Category?
    suspend fun getAllCategories(): List<Category>
    suspend fun addCategory(name: String, image: Uri)


}

class CategoryRepositoryImpl(
    private val categoryService: CategoryService = CategoryServicesImpl(),


    ) : CategoryRepository {

    override suspend fun addCategory(name: String, image: Uri) {
        val categoryDto = CategoryDTO(name = name)
        categoryService.createCategory(categoryDto,image)
    }

    /*override suspend fun createCategory(category: Category){
        Log.d("CategoryRepositoryImpl", "llega a createCategory")
        try {
            categoryService.createCategory(category)
        } catch (e: Exception) {

            throw Exception("Error creating category: ${e.message}")
        }
    }*/



    override suspend fun getCurrentCategory(): Category? {
        val category = categoryService.loadCurrentCategory(Firebase.auth.uid!!)
        Log.v("CategoryRepositoryImpl", "Category: $category")
        return category


    }

    override suspend fun getCategoryByName(name: String): Category? {
        return categoryService.getCategoryByName(name)
    }
/*
    override suspend fun getAllCategories(): List<Category> {
        Log.d("CategoryRepository", "llega a getAllCategories")
        return try {
            val querySnapshot = Firebase.firestore
                .collection("categories")
                .get()
                .await()

            querySnapshot.documents.mapNotNull { it.toObject(Category::class.java) }
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error fetching categories: ${e.message}")
            emptyList()
        }
    }*/

    override suspend fun getAllCategories(): List<Category> {
        Log.d("CategoryRepository", "Fetching all categories")
        return try {
            // Obtén las categorías desde Firestore
            val querySnapshot = Firebase.firestore
                .collection("categories")
                .get()
                .await()

            // Obtén las URLs de las imágenes desde Firebase Storage
            val storage = Firebase.storage
            querySnapshot.documents.mapNotNull { document ->
                val id = document.id
                val name = document.getString("name") ?: "Sin nombre"

                try {
                    val imageUrl = storage.reference
                        .child("categoriesImages/$id") // Asegúrate de que el path coincida con Firebase Storage
                        .downloadUrl
                        .await()
                        .toString()

                    // Devuelve el objeto Category
                    Category(id = id, name = name, imageId = imageUrl)
                } catch (e: Exception) {
                    Log.e("CategoryRepository", "Error fetching image URL for category $id: ${e.message}")
                    null // Ignora categorías con problemas para obtener la imagen
                }
            }
        } catch (e: Exception) {
            Log.e("CategoryRepository", "Error fetching categories: ${e.message}")
            emptyList()
        }
    }


}

