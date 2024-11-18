package com.example.merco.viewmodel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.merco.domain.model.AuthSate
import com.example.merco.domain.model.Seller
import com.example.merco.domain.model.User
import com.example.merco.repository.AuthRepository
import com.example.merco.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.auth.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//mejor authViewModel
class SignupViewModel(
    private val repo: AuthRepository = AuthRepositoryImpl()
) : ViewModel() {

    val authState = MutableLiveData(0)
    val errorMessage = MutableLiveData<String?>()
    val userRole = MutableLiveData<String?>()// Para mostrar errores al usuario

    //private var _authSate:MutableLiveData<AuthSate> = MutableLiveData(AuthState.Idle)
//val authState: MutableLiveData()
    //0. Idle
    //1. Loading
    //2. Error
    //3. Success

    // Validar formato de email
    fun isValidEmail(email: String): Boolean {
        Log.e("SignupViewModel", "Validando email: $email")
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun signup(user: User, password: String) {
        // Validar correo electrónico antes de proceder
        if (!isValidEmail(user.email)) {
            errorMessage.postValue("El correo electrónico tiene un formato incorrecto.")
            authState.postValue(2) // Estado de error
            Log.e("SignupViewModel", "Correo electrónico inválido")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState.value = 1 } // Estado cargando
            try {
                repo.signup(user, password)
                withContext(Dispatchers.Main) { authState.value = 3 } // Estado éxito
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) {
                    authState.value = 2 // Estado error
                    Log.e("SignupViewModel", "Error de autenticación: ${ex.message}")
                    errorMessage.value = ex.message // Mostrar error de Firebase
                }
            }
        }
    }

    fun signupseller(seller: Seller, password: String) {
        // Validar correo electrónico antes de proceder
        if (!isValidEmail(seller.email)) {
            errorMessage.postValue("El correo electrónico tiene un formato incorrecto.")
            authState.postValue(2) // Estado de error
            Log.e("SignupViewModel", "Correo electrónico inválido")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState.value = 1 } // Estado cargando
            try {
                repo.signupseller(seller, password)
                withContext(Dispatchers.Main) { authState.value = 3 } // Estado éxito
            } catch (ex: FirebaseAuthException) {
                withContext(Dispatchers.Main) {
                    authState.value = 2 // Estado error
                    Log.e("SignupViewModel", "Error de autenticación: ${ex.message}")
                    errorMessage.value = ex.message // Mostrar error de Firebase
                }
            }
        }
    }

    suspend fun getCurrentRole(): String {
        return withContext(Dispatchers.IO) {
            repo.getCurrentRole()
        }
    }


    fun signin(email: String, password: String) {
        // Validar correo electrónico antes de proceder
        if (!isValidEmail(email)) {
            errorMessage.postValue("El correo electrónico tiene un formato incorrecto.")
            authState.postValue(2) // Estado error
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { authState.value = 1 } // Estado cargando
            try {
                repo.signin(email, password)
                withContext(Dispatchers.Main) { authState.value = 3 } // Estado éxito
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    authState.value = 2 // Estado error
                    errorMessage.value = ex.message // Mostrar error
                }
            }
        }
    }
}
