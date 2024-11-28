package com.example.merco.service
import android.net.Uri
import android.util.Log
import androidx.room.util.copy
import com.google.firebase.firestore.FirebaseFirestore

import com.example.merco.domain.model.Category
import com.example.merco.domain.model.DTO.CategoryDTO
import com.example.merco.domain.model.Seller
import com.example.merco.domain.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await


interface  CategoryService {
    suspend fun createCategory(categoryDto: CategoryDTO,image:Uri)
    suspend fun getCategoryById(id:String): Category?
    suspend fun getCategoryByName(name: String): Category?
    suspend fun loadCurrentCategory(uid: String): Category?
    suspend fun addImageCategory(imageUri: Uri, categoryId: String)

}

class CategoryServicesImpl:CategoryService {

    override suspend fun addImageCategory(imageUri: Uri, categoryId: String) {
        try {

            val storageReference = Firebase.storage.reference
                .child("categoriesImages/$categoryId")

            val uploadTask = storageReference.putFile(imageUri).await()

            val downloadUrl = storageReference.downloadUrl.await()

            Log.d("FirebaseService", "Image uploaded successfully: $downloadUrl")
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error uploading image: ${e.message}", e)
            throw e
        }
    }


    override suspend fun createCategory(categoryDto: CategoryDTO, image: Uri) {
        Log.d("CategoryServicesImpl", "Creando categoría con nombre: ${categoryDto.name}")
        try {

            val documentReference = Firebase.firestore
                .collection("categories")
                .document()

            val generatedId = documentReference.id

            val category = Category(id = generatedId,name = categoryDto.name,imageId = generatedId)


            documentReference
                .set(category)
                .await()

            Log.d("CategoryServicesImpl", "Categoría creada con éxito con ID: $generatedId")

            addImageCategory(image, generatedId)

        } catch (e: Exception) {
            Log.e("CategoryServicesImpl", "Error al crear la categoría: ${e.message}")
        }
    }


    override suspend fun loadCurrentCategory(categoryId: String): Category? {
        Log.d("CategoryServicesImpl", "Cargando categoría con ID: $categoryId")

        val category = Firebase.firestore
            .collection("categories")
            .document(categoryId)
            .get()
            .await()
        val categoryObject = category.toObject(Category::class.java)
        return categoryObject
    }


    override suspend fun getCategoryById(id: String): Category? {
        Log.d("CategoryServicesImpl", "getCategoryById: $id")

        val category = Firebase.firestore
            .collection("categories")
            .document(id)
            .get()
            .await()
        val categoryObject = category.toObject(Category::class.java)
        return categoryObject
    }


    override suspend fun getCategoryByName(name: String): Category? {
        val category = Firebase.firestore
            .collection("categories")
            .document(name)
            .get()
            .await()

        val categoryObject = category.toObject(Category::class.java)
        return categoryObject
    }
}



