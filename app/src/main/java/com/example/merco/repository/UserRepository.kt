package com.example.merco.repository

import android.util.Log
import com.example.merco.domain.model.Seller
import com.example.merco.domain.model.User
import com.example.merco.service.UserServices
import com.example.merco.service.UserServicesImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentSnapshot


interface UserRepository {


    suspend fun createUser(user: User)
    suspend fun getCurrentUser(): User?

    suspend fun createSeller(seller: Seller)
    suspend fun getCurrentSeller(): Seller?
}

class UserRepositoryImpl(
    private val sellerServices: UserServices = UserServicesImpl(),
    private val userServices: UserServices = UserServicesImpl()

) : UserRepository {

    override suspend fun createUser(user: User) {
        try {
            userServices.createUser(user)
        } catch (e: Exception) {
            // Maneja el error de creación de usuario, por ejemplo, logueando o lanzando una excepción
            throw Exception("Error creating user: ${e.message}")
        }
    }

    override suspend fun getCurrentUser(): User? {
        val user = userServices.loadCurrentUser(Firebase.auth.uid!!)
        Log.v("UserRepositoryImpl", "User: $user")
        return user


        /*val firebaseUser = Firebase.auth.currentUser
        return if (firebaseUser != null) {
            try {
                userServices.getUserById(firebaseUser.uid)
            } catch (e: Exception) {
                // Maneja el error de obtención de usuario, por ejemplo, logueando o lanzando una excepción
                throw Exception("Error fetching user: ${e.message}")
            }
        } else {
            null // No hay un usuario autenticado
        }*/
    }

    override suspend fun createSeller(seller: Seller) {
        try {
            sellerServices.createSeller(seller)
        } catch (e: Exception) {

            throw Exception("Error creating Seller: ${e.message}")
        }
    }

    override suspend fun getCurrentSeller(): Seller? {
        val seller = sellerServices.loadCurrentSeller(Firebase.auth.uid!!)
        Log.v("SellerRepositoryImpl", "Seller: $seller")
        return seller


        /*val firebaseUser = Firebase.auth.currentUser
        return if (firebaseUser != null) {
            try {
                userServices.getUserById(firebaseUser.uid)
            } catch (e: Exception) {
                // Maneja el error de obtención de usuario, por ejemplo, logueando o lanzando una excepción
                throw Exception("Error fetching user: ${e.message}")
            }
        } else {
            null // No hay un usuario autenticado
        }*/
    }

}


