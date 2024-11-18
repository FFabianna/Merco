package com.example.merco.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.merco.viewmodel.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileUserScreen(navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {
    Log.e(">>>", "LLega A profile screen")
    val userState by profileViewModel.user.observeAsState()
    val isAutenticated by remember { mutableStateOf(Firebase.auth.currentUser != null) }

    if (isAutenticated) {
        LaunchedEffect(true) {
            profileViewModel.getCurrentUser()
        }
        // Diseñar la pantalla de perfil
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de usuario centrada
            /* Image(
                 painter = rememberImagePainter(data = userState?.photoUrl),
                 contentDescription = "Foto de usuario",
                 modifier = Modifier
                     .size(120.dp)
                     .clip(CircleShape)
                     .background(MaterialTheme.colors.primary)
                     .padding(4.dp),
                 contentScale = ContentScale.Crop
             )*/

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de usuario
            Text(
                text = "${userState?.name}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Correo en gris claro
            Text(
                text = userState?.email ?: "",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Nombre completo
            Text(
                text = "Nombre completo: ${userState?.name ?: ""} ${userState?.lastname ?: ""}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Teléfono
            Text(
                text = "Teléfono: ${userState?.celphone ?: ""}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))



            // Botón para eliminar cuenta
            /*
            Button(
                onClick = {
                    profileViewModel.deleteAccount()
                    navController.navigate("signup")
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(text = "Eliminar cuenta", color = Color.White)
            }*/

            // Botón para regresar al login
            Button(onClick = {
                Firebase.auth.signOut()
                navController.navigate("login")
            }) {
                Text(text = "Cerrar sesión")
            }
        }
    }

    /*// Si no hay usuario, redirigir al login
    if (userState == null) {
        navController.navigate("login")
    } else {
        Log.e(">>>", userState.toString())
        Log.e(">>>", "LLega A profile")

    }*/
}