package com.example.merco.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.merco.domain.model.DTO.ProductDTO
import com.example.merco.domain.model.Product
import com.example.merco.domain.model.Seller
import com.example.merco.repository.ProductRepository
import com.example.merco.repository.ProductRepositoryImpl
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.Service
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository = ProductRepositoryImpl()

) : ViewModel() {



    val authState = MutableLiveData(0)
    val errorMessage = MutableLiveData<String?>()
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?> = _product



    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _sellerId = MutableLiveData<String>()
    val sellerId: LiveData<String> get() = _sellerId

    // Inicializa el sellerId, por ejemplo, desde un repositorio o servicio
    fun initializeSellerId(sellerId: String) {
        _sellerId.value = sellerId
    }

    init {
        fetchProducts(categoryId = "")
    }


    fun uploadImageAndAddProduct(categoryId: String, productDTO: ProductDTO, imageUri: Uri) {
        val updatedProduct = productDTO.copy(sellerId = _sellerId.value ?: "")
        Log.d("ProductViewModel", "Seller ID: ${_sellerId.value}")
        Log.d("ProductViewModel", "Subiendo imagen y agregando producto")
        authState.value = 1 // Loading
        viewModelScope.launch {
            try {
                productRepository.addProduct(categoryId, updatedProduct,imageUri)

                authState.value = 3 // Success
            } catch (e: Exception) {
                authState.value = 2 // Error
            }

        }
    }

    fun fetchProducts(categoryId: String) = viewModelScope.launch {
        try {
            val productsList = productRepository.getProductsByCategory(categoryId)
            _products.postValue(productsList)
        } catch (e: Exception) {
            Log.e("ProductViewModel", "Error fetching products: ${e.message}")
        }
    }

    suspend fun fetchProductss(categoryId: String): List<Product> {
        return try {
            productRepository.getProductsByCategory(categoryId)
        } catch (e: Exception) {
            Log.e("ProductViewModel", "Error fetching products: ${e.message}")
            emptyList() // Devuelve una lista vacía en caso de error
        }
    }
/*
    fun fetchProductsBySeller(sellerId: String)= viewModelScope.launch {
        try{
            val productsList = productRepository.getProductsBySeller(sellerId)
            _products.postValue(productsList)
        } catch (e: Exception) {
            Log.e("ProductViewModel", "Error fetching products: ${e.message}")
        }

    }*/

    fun fetchProductsBySeller(sellerId: String) = viewModelScope.launch {
        try {
            val products = productRepository.getProductsBySeller(sellerId)
            _products.value = products
        } catch (e: Exception) {
            Log.e("ProductViewModel", "Error fetching products: ${e.message}")
            _products.value = emptyList() // Manejo de errores
        }


    }

    fun loadProductById(id: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val fetchedProduct = productRepository.getProductById(id)
                if (fetchedProduct != null) {
                    _product.value = fetchedProduct
                } else {
                    _error.value = "Producto no encontrado."
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar el producto: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }




}
