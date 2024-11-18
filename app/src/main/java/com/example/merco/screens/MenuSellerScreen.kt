package com.example.merco.screens

import android.net.Uri
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.merco.viewmodel.CategoryViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun MenuSellerScreen(
    navController: NavController,
    viewmodel: CategoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val categories by viewmodel.categories.observeAsState(emptyList())


    LaunchedEffect(Unit) {
        viewmodel.fetchCategories()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // Barra superior
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.navigate("profileSeller") }) {
                Text(text = "Perfil", color = Color.Black)
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Mi tienda",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // Título de la sección
        Text(
            text = "Categorías",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp,  vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp,vertical = 8.dp)
                .padding(bottom = 32.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(categories) { category ->

                // Card de categoría
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            // val categoryJson= Json.encodeToString(category)
                            val categoryJson = Uri.encode(Json.encodeToString(category))
                            navController.navigate("productsScreen/$categoryJson")

                        },
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(category.imageId),
                            contentDescription = "Imagen de ${category.name}",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            // Botón añadido al final de la lista
            item {
                Button(
                    onClick = { navController.navigate("addCategory") },
                    colors = ButtonDefaults.buttonColors(Color(0xFFE53935)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Añadir Categoría", color = Color.White)
                }
            }
        }


    }
}


