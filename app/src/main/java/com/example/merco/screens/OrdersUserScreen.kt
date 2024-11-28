package com.example.merco.screens


import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.merco.viewmodel.OrderViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import java.lang.reflect.Modifier


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun OrdersUserScreen(
    navController: NavController,
    viewmodel: OrderViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) {
    var expanded by remember { mutableStateOf(false) }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val orders by viewmodel.orders.observeAsState(emptyList())

    if (currentUserId.isEmpty()) {
        Text("Error: No se pudo obtener el ID del User.")
        return
    }

    LaunchedEffect(currentUserId) {
        viewmodel.fetchOrdersU(currentUserId)
    }

    Scaffold(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mis pedidos",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                       ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "atrás",
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = Color.Black
                ),
                modifier = androidx.compose.ui.Modifier.statusBarsPadding()
            )
        } ) { paddingValues ->
        Box(
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF9E9E9E),
                    fontWeight = FontWeight.Medium,
                    modifier = androidx.compose.ui.Modifier.padding(vertical = 16.dp)
                )

                LazyColumn(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(orders) { order ->
                        OutlinedCard(
                            modifier = androidx.compose.ui.Modifier
                                .fillMaxSize()
                                .height(120.dp)
                                .clickable {
                                    val orderJson = Uri.encode(Json.encodeToString(order))
                                    navController.navigate("orderDetailScreen/$orderJson")
                                },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Row(
                                modifier = androidx.compose.ui.Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Imagen del producto
                                Image(
                                    painter = rememberAsyncImagePainter(model = order.imageId),
                                    contentDescription = "Imagen de ${order.productName}",
                                    modifier = androidx.compose.ui.Modifier
                                        .size(76.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )


                                Column(
                                    modifier = androidx.compose.ui.Modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {

                                    Text(
                                        text = order.productName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = "Stock: ${order.quantity}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Light
                                    )
                                    // Precio total con el signo de pesos
                                    Text(
                                        text = "$${"%.2f".format(order.totalPrice)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Light
                                    )
                                }
                            }
                        }
                    }


            }


            }
        }
    }




}






