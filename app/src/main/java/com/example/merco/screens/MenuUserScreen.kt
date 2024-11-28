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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter

import coil.request.ImageRequest
import coil.compose.rememberImagePainter
import com.example.merco.domain.model.Product
import com.example.merco.viewmodel.CategoryViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuUserScreen(
    navController: NavController,
    viewmodel: CategoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val currentSellerId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    var expanded by remember { mutableStateOf(false) }
    val categoriesWithProducts by viewmodel.categoriesWithProducts.observeAsState(emptyList())

    if (currentSellerId.isEmpty()) {
        Text("Error: No se pudo obtener el ID del vendedor.")
        return
    }

    LaunchedEffect(Unit) {
        viewmodel.fetchCategoriesAndProducts()
    }

    // Envolvemos todo en un Box para controlar el posicionamiento del DropdownMenu
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
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
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Lista de categorías y productos
                items(categoriesWithProducts) { (category, products) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            items(products) { product ->
                                // Tarjetas de productos
                                ProductCard(product, navController)
                            }
                        }
                        TextButton(
                            onClick = {
                                val categoryJson = Uri.encode(Json.encodeToString(category))
                                navController.navigate("productsUserScreen/$categoryJson")
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Ver todos", color = Color.Gray)
                        }
                    }
                }
            }
        }

        // DropdownMenu se muestra arriba del contenido principal
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(280.dp)
                .background(Color.White)
                .align(Alignment.TopStart) // Se alinea en la parte superior izquierda
        ) {
            DropdownMenuItem(
                text = { Text("Perfil", color = Color.Black, fontSize = 16.sp) },
                onClick = {
                    expanded = false
                    navController.navigate("profileUser")
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
                text = { Text("Mis Pedidos", color = Color.Black, fontSize = 16.sp) },
                onClick = {
                    expanded = false
                    navController.navigate("ordersUser")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocalGroceryStore,
                        contentDescription = "Pedidos",
                        tint = Color.Red
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("Ayuda", color = Color.Black, fontSize = 16.sp) },
                onClick = { expanded = false
                          navController.navigate("info")},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Ayuda",
                        tint = Color.Red
                    )
                }
            )
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFFE0E0E0)
            )
            DropdownMenuItem(
                text = { Text("Cerrar sesión", color = Color.Red, fontSize = 16.sp) },
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

@Composable
fun ProductCard(product: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .clickable {
                val productJson = Uri.encode(Json.encodeToString(product))
                navController.navigate("productDetailUser/$productJson")
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {

            Image(
                painter = rememberAsyncImagePainter(product.imageId),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


            if (product.discount > 0) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(40.dp)
                        .background(
                            color = Color(0xFF4CAF50),
                            shape = CircleShape
                        )
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${product.discount}%",
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    text = "$${product.price}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = "$${product.newPrice}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}























