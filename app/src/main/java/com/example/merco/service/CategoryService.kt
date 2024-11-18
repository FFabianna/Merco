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
            // Crear una referencia a la ubicación deseada en Firebase Storage
            val storageReference = Firebase.storage.reference
                .child("categoriesImages/$categoryId")

            // Subir el archivo a Firebase Storage
            val uploadTask = storageReference.putFile(imageUri).await()

            // Obtener la URL de descarga
            val downloadUrl = storageReference.downloadUrl.await()

            // Aquí puedes usar la URL para actualizar Firestore si es necesario
            Log.d("FirebaseService", "Image uploaded successfully: $downloadUrl")
        } catch (e: Exception) {
            Log.e("FirebaseService", "Error uploading image: ${e.message}", e)
            throw e // Propaga el error para que el repositorio lo maneje
        }
    }


    override suspend fun createCategory(categoryDto: CategoryDTO, image: Uri) {
        Log.d("CategoryServicesImpl", "Creando categoría con nombre: ${categoryDto.name}")

        try {
            // Genera un ID único automáticamente
            val documentReference = Firebase.firestore
                .collection("categories")
                .document() // Esto crea un DocumentReference con un ID único, pero no escribe todavía.

            val generatedId = documentReference.id // Obtienes el ID autogenerado


            val category = Category(id = generatedId,name = categoryDto.name,imageId = generatedId)

            // Guarda el documento con el ID autogenerado y el campo `id` actualizado
            documentReference
                .set(category)
                .await()

            Log.d("CategoryServicesImpl", "Categoría creada con éxito con ID: $generatedId")

            // Sube la imagen asociada
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



