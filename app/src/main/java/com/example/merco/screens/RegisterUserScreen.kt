package com.example.merco.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.merco.domain.model.User
import com.example.merco.viewmodel.ProfileViewModel
import com.example.merco.viewmodel.SignupViewModel

@Composable
fun RegisterUserScreen(navController: NavController, signupViewModel: SignupViewModel = viewModel()
) {

    val authState by signupViewModel.authState.observeAsState()

    // Declarar las variables para los nuevos campos
    var name by remember { mutableStateOf("") }
    var lastname by remember { mutableStateOf("") }
    var celphone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Centramos horizontalmente
        ) {
            Text(
                text = "Registrarse",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campo para el nombre
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Campo para el apellido
            TextField(
                value = lastname,
                onValueChange = { lastname = it },
                label = { Text("Apellido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Campo para el número de celular
            TextField(
                value = celphone,
                onValueChange = { celphone = it },
                label = { Text("Celular") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // Solo números
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Campo para el correo electrónico
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // Teclado de email
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Campo para la contraseña
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Log.e("Autenticacion ",authState.toString())

            // Estado de autenticación
            when (authState) {
                1 -> {
                    CircularProgressIndicator(modifier = Modifier.padding(bottom = 16.dp))
                }
                2 -> {
                    Text(
                        text = "Hubo un error, que no podemos ver todavía",
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                3 -> {
                    LaunchedEffect(Unit) {
                        navController.navigate("menuUser")
                    }
                }
            }

            // Botón de registro
            Button(
                onClick = {
                    signupViewModel.signup(
                        User("", name, lastname, celphone, email, role = "Customer"),
                        password
                    )

                },

                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(50.dp), // Ajustar tamaño del botón
                shape = RoundedCornerShape(24.dp) // Esquinas redondeadas
            ) {
                Text(text = "Regístrate", color = Color.White)
            }

            // Opción para iniciar sesión si ya tiene cuenta
            TextButton(
                onClick = { navController.navigate("login") },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "¿Ya tienes una cuenta?", color = Color.Black)
            }

            // Sección para registrar con Google o Facebook
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("O regístrate con una cuenta de:")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                /*
                IconButton(onClick = { /* Registro con Google */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google",
                        tint = Color.Unspecified // Para usar el color original
                    )
                }
                IconButton(onClick = { /* Registro con Facebook */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "Facebook",
                        tint = Color.Unspecified
                    )
                }*/
            }
        }
    }
}