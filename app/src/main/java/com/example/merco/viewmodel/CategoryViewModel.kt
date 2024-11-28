package com.example.merco.viewmodel
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.merco.domain.model.Category
import com.example.merco.domain.model.Product
import com.example.merco.repository.AuthRepository
import com.example.merco.repository.AuthRepositoryImpl
import com.example.merco.repository.CategoryRepository
import com.example.merco.repository.CategoryRepositoryImpl
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.util.UUID

class CategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepositoryImpl(),

    private val productViewModel: ProductViewModel= ProductViewModel()


) : ViewModel() {

    val authState = MutableLiveData(0)
    val errorMessage = MutableLiveData<String?>()

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

    private val _categoriesWithProducts = MutableLiveData<List<Pair<Category, List<com.example.merco.domain.model.Product>>>>()
    val categoriesWithProducts: LiveData<List<Pair<Category, List<com.example.merco.domain.model.Product>>>> = _categoriesWithProducts

    init {
        fetchCategories()
    }

    fun uploadImageAndAddCategory(name: String, imageUri: Uri) {
        Log.d("CategoryViewModel", "Subiendo imagen y agregando categoría")
        authState.value = 1 // Loading
        viewModelScope.launch {
            try {
                categoryRepository.addCategory(name, imageUri)
                authState.value = 3 // Success
            } catch (e: Exception) {
                authState.value = 2 // Error
            }
        }
    }

    fun fetchCategories() = viewModelScope.launch {

        try {
            val categoriesList = categoryRepository.getAllCategories()
            _categories.postValue(categoriesList)
        } catch (e: Exception) {
            Log.e("CategoryViewModel", "Error fetching categories: ${e.message}")
        }
    }

    fun fetchCategoriesAndProducts() {
        viewModelScope.launch {
            try {
                val categories = categoryRepository.getAllCategories()
                val categoriesWithProductsList = coroutineScope {
                    categories.map { category ->
                        async {
                            val products = productViewModel.fetchProductss(category.id) // Ahora devuelve la lista
                            category to products
                        }
                    }.awaitAll()
                }
                _categoriesWithProducts.postValue(categoriesWithProductsList)
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error fetching categories and products: ${e.message}")
            }
        }
    }


}






