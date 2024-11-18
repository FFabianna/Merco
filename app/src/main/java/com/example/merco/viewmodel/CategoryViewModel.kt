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
import com.example.merco.repository.AuthRepository
import com.example.merco.repository.AuthRepositoryImpl
import com.example.merco.repository.CategoryRepository
import com.example.merco.repository.CategoryRepositoryImpl
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

class CategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepositoryImpl()

) : ViewModel() {

    val authState = MutableLiveData(0)
    val errorMessage = MutableLiveData<String?>()

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> get() = _categories

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


}




