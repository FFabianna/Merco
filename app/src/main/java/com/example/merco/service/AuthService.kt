package com.example.merco.service

import android.util.Log
import com.example.merco.domain.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface AuthService {

    suspend fun createUser(email: String, password: String)
    suspend fun createSeller(email: String, password: String)
    suspend fun deleteAccount(userId: String)

    suspend fun loginWithEmailAndPassword(email: String, password: String)
}

class AuthServiceImpl: AuthService {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    override suspend fun createUser(email: String, password: String) {
        // Crear el usuario con correo y contraseña en Firebase Authentication
        Log.e("AuthServiceImpl", "Creando usuario con email: $email")
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun createSeller(email: String, password: String) {
        // Crear el usuario con correo y contraseña en Firebase Authentication
        Log.e("AuthServiceImpl", "Creando vendedor con email: $email")
        auth.createUserWithEmailAndPassword(email, password).await()
    }


    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        // Iniciar sesión con correo y contraseña en Firebase Authentication
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun deleteAccount(userId: String) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.delete()?.await() ?: throw Exception("Usuario no autenticado")
    }
}