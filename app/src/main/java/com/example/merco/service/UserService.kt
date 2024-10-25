package com.example.merco.service


import android.util.Log
import com.example.merco.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


//


interface UserServices {
    suspend fun createUser(user: User)
    suspend fun getUserById(id:String):User?
    suspend fun loadCurrentUser(uid: String): User?
}

class UserServicesImpl:UserServices{
    override suspend fun createUser(user: User) {
        try {
            Log.d("UserServicesImpl", "Creando usuario con ID: ${user.id}")

            // Referencia a la colección "users", creando o actualizando un documento con el ID del usuario
            Firebase.firestore
                .collection("users")           // Colección "users"
                .document(user.id)             // El nombre del documento será el user.id
                .set(user)                     // Guardar el objeto "user" en Firestore
                .await()                       // Espera a que la operación termine (como estás usando `suspend`)

            Log.d("UserServicesImpl", "Usuario creado con éxito")
        } catch (e: Exception) {
            // Manejo de errores si algo sale mal
            Log.e("UserServicesImpl", "Error al crear el usuario: ${e.message}")
        }
    }

    override suspend fun loadCurrentUser(userId: String): User? {
        val user = Firebase.firestore
            .collection("users")
            .document(userId)
            .get()
            .await()
        val userObject = user.toObject(User::class.java)
        return userObject
    }

    override suspend fun getUserById(id: String): User? {
        val user = Firebase.firestore
            .collection("users")
            .document(id)
            .get()
            .await()
        val userObject = user.toObject(User::class.java)
        return userObject
    }

}
