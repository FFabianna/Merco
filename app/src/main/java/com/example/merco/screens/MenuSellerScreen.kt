package com.example.merco.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter

import coil.request.ImageRequest
import coil.compose.rememberImagePainter
import com.example.merco.viewmodel.CategoryViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSellerScreen(
    navController: NavController,
    viewmodel: CategoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val categories by viewmodel.categories.observeAsState(emptyList())
    var expanded by remember { mutableStateOf(false) }
    val currentSellerId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    if (currentSellerId.isEmpty()) {
        Text("Error: No se pudo obtener el ID del vendedor.")
        return
    }

    LaunchedEffect(Unit) {
        viewmodel.fetchCategories()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Merco",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black
                ),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF9E9E9E),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(categories) { category ->
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clickable {
                                    val categoryJson = Uri.encode(Json.encodeToString(category))
                                    navController.navigate("productsScreen/$categoryJson")
                                },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {

                                Image(
                                    painter = rememberAsyncImagePainter(model = category.imageId),
                                    contentDescription = "Imagen de ${category.name}",
                                    modifier = Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    item {
                        Button(
                            onClick = { navController.navigate("addCategory") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Añadir Categoría",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(280.dp)
                    .background(Color.White)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Perfil",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        expanded = false
                        navController.navigate("profileSeller")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color.Red
                        )
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            "Pedidos",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    },
                    onClick = { expanded = false
                        navController.navigate("orderSeller")},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocalGroceryStore,
                            contentDescription = "Pedidos",
                            tint = Color.Red
                        )
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            "Mis productos",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        expanded = false
                        navController.navigate("productSeller/$currentSellerId")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = "Productos",
                            tint = Color.Red
                        )
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            "Info",
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    },
                    onClick = { expanded = false
                        navController.navigate("info")
                              },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Info",
                            tint = Color.Red
                        )
                    }
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFFE0E0E0)
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            "Cerrar sesión",
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        expanded = false
                        navController.navigate("login")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = Color.Red
                        )
                    }
                )
            }
        }
    }
}






