package com.example.merco.repository

import android.util.Log
import com.example.merco.domain.model.Category
import com.example.merco.domain.model.Seller
import com.example.merco.domain.model.User
import com.example.merco.service.AuthService
import com.example.merco.service.AuthServiceImpl
import com.example.merco.service.CategoryService
import com.example.merco.service.CategoryServicesImpl
import com.example.merco.service.UserServices
import com.example.merco.service.UserServicesImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun signup(user:User, password:String)
    suspend fun signupseller(seller:Seller, password:String)
    suspend fun signin(email:String, password:String)
    suspend fun deleteUserAccount(userId: String): Boolean
    //suspend fun addCategory(category:Category)


    suspend fun getCurrentRole(): String
}

class AuthRepositoryImpl(
    val authService: AuthService = AuthServiceImpl(),
    val userRepository: UserRepository = UserRepositoryImpl(),
    val sellerRepository: UserRepository = UserRepositoryImpl(),
    val categoryRepository: CategoryRepository = CategoryRepositoryImpl(),
    val userServices: UserServices = UserServicesImpl()

) : AuthRepository{
    override suspend fun signup(user: User, password: String) {
        //1. Registro en modulo de autenticación
        authService.createUser(user.email, password)
        Log.e("AuthRepositoryImpl", "Usuario registrado con éxito")
        //2. Obtenemos el UID
        val uid = Firebase.auth.currentUser?.uid
        Log.v("AuthRepositoryImpl", "UID: $uid")
        //3. Crear el usuario en Firestore
        uid?.let {
            user.id = it
            Log.e("AuthRepositoryImpl   USEDID", user.id)
            userRepository.createUser(user)
        }
        Log.e("AuthRepositoryImpl", "Usuario registrado con éxito")
    }

    override suspend fun signupseller(seller: Seller, password: String) {
        //1. Registro en modulo de autenticación
        authService.createSeller(seller.email, password)
        Log.e("AuthRepositoryImpl", "Vendedor registrado con éxito")
        //2. Obtenemos el UID
        val uid = Firebase.auth.currentUser?.uid
        Log.v("AuthRepositoryImpl", "UID: $uid")
        //3. Crear el usuario en Firestore
        uid?.let {
            seller.id = it
            Log.e("AuthRepositoryImpl   USEDID", seller.id)
            sellerRepository.createSeller(seller)
        }
        Log.e("AuthRepositoryImpl", "Usuario registrado con éxito")
    }


    override suspend fun signin(email: String, password: String) {
        authService.loginWithEmailAndPassword(email, password)
    }

    override suspend fun getCurrentRole(): String {
        val role = userServices.loadCurrentRole(Firebase.auth.uid!!)
        Log.v("UserRepositoryImpl", "Role: $role")
        return role
    }

    /*override suspend fun addCategory(category:Category){
        categoryRepository.createCategory(category)
        Log.e("AuthRepositoryImpl", "Categoria registrada con éxito")
    }*/
    override suspend fun deleteUserAccount(userId: String): Boolean {
        return try {
            authService.deleteAccount(userId) // Llamada al servicio
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }




}

