package com.example.merco.service


import android.util.Log
import com.example.merco.domain.model.Seller
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

    suspend fun createSeller(seller: Seller)
    suspend fun getSellerById(id:String): Seller?
    suspend fun loadCurrentSeller(uid: String): Seller?


    suspend fun loadCurrentRole(uid: String): String
}

class UserServicesImpl:UserServices{
    override suspend fun createUser(user: User) {
        try {
            Log.d("UserServicesImpl", "Creando usuario con ID: ${user.id} y rol: ${user.role}")

            Firebase.firestore
                .collection("users")           // Colección "users"
                .document(user.id)             // El nombre del documento será el user.id
                .set(user)                     // Guardar el objeto "user" en Firestore
                .await()                       // Espera a que la operación termine (como estás usando `suspend`)

            Log.d("UserServicesImpl", "Usuario creado con éxito")
        } catch (e: Exception) {

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

    override suspend fun loadCurrentRole(userId: String): String {
        val user = Firebase.firestore
            .collection("users")
            .document(userId)
            .get()
            .await()
        val userObject = user.toObject(User::class.java)
        val role = userObject?.role ?: ""
        return role
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

    override suspend fun createSeller(seller: Seller){
        try {
            Log.d("SellerServicesImpl", "Creando vendedor con ID: ${seller.id} y rol: ${seller.role}")

            Firebase.firestore
                .collection("users")
                .document(seller.id)
                .set(seller)
                .await()

            Log.d("SellerServicesImpl", "Vendedor creado con éxito")
        } catch (e: Exception) {

            Log.e("SellerServicesImpl", "Error al crear el vendedor: ${e.message}")
        }
    }
    override suspend fun loadCurrentSeller(sellerId: String): Seller? {
        val seller = Firebase.firestore
            .collection("users")
            .document(sellerId)
            .get()
            .await()
        val sellerObject = seller.toObject(Seller::class.java)
        return sellerObject
    }

    override suspend fun getSellerById(id: String): Seller? {
        val seller = Firebase.firestore
            .collection("users")
            .document(id)
            .get()
            .await()
        val sellerObject = seller.toObject(Seller::class.java)
        return sellerObject
    }


}
