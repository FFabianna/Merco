package com.example.merco.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.merco.viewmodel.SignupViewModel


@Composable
fun LoginScreen(navController: NavController, authViewModel: SignupViewModel = viewModel()) {
    val authState by authViewModel.authState.observeAsState()
    val userRole by authViewModel.userRole.observeAsState()

    var email by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Centrando y estilizando el diseño
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo de texto para el email
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        // Campo de texto para la contraseña
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        // Estado de autenticación
        when (authState) {
            1 -> {
                CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
            }
            2 -> {
                Text(
                    text = "Hubo un error, verifica los datos",
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            3 -> {

                LaunchedEffect(Unit) {
                    val role = authViewModel.getCurrentRole() // obtener el rol directamente

                    Log.e("LoginScreen", "Role: $role")
                    when (role) {
                        "Seller" -> navController.navigate("menuSeller")
                        "Customer" -> navController.navigate("menuUser")
                        else -> Log.e("LoginScreen", "Unknown role: $role")
                    }
                }
            }
        }
        // Botón de inicio de sesión
        Button(
            onClick = { authViewModel.signin(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Iniciar Sesión")
        }

        // Opción de registro (por si quieres añadirla)
        TextButton(onClick = { navController.navigate("selectTypeUser") }) {
            Text(text = "¿No tienes una cuenta? Regístrate")
        }
    }
}