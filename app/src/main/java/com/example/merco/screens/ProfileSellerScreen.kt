package com.example.merco.screens
import android.util.Log
import androidx.compose.foundation.layout.*
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
fun ProfileSellerScreen(navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {
    Log.e(">>>", "LLega a Seller profile screen")
    val sellerState by profileViewModel.seller.observeAsState()
    val isAuthenticated by remember { mutableStateOf(Firebase.auth.currentUser != null) }

    if (isAuthenticated) {
        LaunchedEffect(true) {
            profileViewModel.getCurrentSeller() // Método para obtener los datos del vendedor
        }

        // Diseñar la pantalla de perfil del vendedor
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Nombre de la tienda
            Text(
                text = "${sellerState?.storename ?: "Nombre de la tienda"}",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Dirección de la tienda
            Text(
                text = "Dirección: ${sellerState?.address ?: "Dirección de la tienda"}",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Ciudad
            Text(
                text = "Ciudad: ${sellerState?.city ?: "Ciudad de la tienda"}",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Nombre completo del vendedor
            Text(
                text = "Vendedor: ${sellerState?.name ?: ""} ${sellerState?.lastname ?: ""}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Teléfono del vendedor
            Text(
                text = "Teléfono: ${sellerState?.celphone ?: ""}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón para cerrar sesión
            Button(onClick = {
                Firebase.auth.signOut()
                navController.navigate("login")
            }) {
                Text(text = "Cerrar sesión")
            }
        }
    } else {
        // Redirigir al login si no hay autenticación
        navController.navigate("login")
    }
}
