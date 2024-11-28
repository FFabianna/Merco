package com.example.merco.screens
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import com.example.merco.viewmodel.SignupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSellerScreen(
    navController: NavController,
    //signupViewModel: SignupViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val sellerState by profileViewModel.seller.observeAsState()
    val isAuthenticated by remember { mutableStateOf(Firebase.auth.currentUser != null) }

    if (isAuthenticated) {
        LaunchedEffect(true) {
            profileViewModel.getCurrentSeller()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Regresar",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo de la tienda
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = "Logo tienda",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(vertical = 16.dp),
                    tint = Color(0xFFE53935)
                )


                Text(
                    text = sellerState?.storename ?: "Nombre de la tienda",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    InfoField(
                        title = "Nombre y Apellido",
                        value = "${sellerState?.name ?: ""} ${sellerState?.lastname ?: ""}"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InfoField(
                        title = "Dirección",
                        value = sellerState?.address ?: ""
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InfoField(
                        title = "Ciudad",
                        value = sellerState?.city ?: ""
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InfoField(
                        title = "Teléfono",
                        value = sellerState?.celphone ?: ""
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botones de acción
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            Firebase.auth.signOut()
                            navController.navigate("login")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Cerrar Sesión",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } /*

                        Button(
                            onClick = {
                                signupViewModel.deleteAccount(sellerState?.id ?: "")
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE53935)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Eliminar Cuenta",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }*/
                }
            }
        }
    } else {
        navController.navigate("login")
    }
}

@Composable
private fun InfoField(title: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

