package com.example.merco.domain.model

sealed class AuthSate {
    object Idle : AuthSate()
    object Loading : AuthSate()
    data class Error(var message: String) : AuthSate()
    object Success : AuthSate()

}