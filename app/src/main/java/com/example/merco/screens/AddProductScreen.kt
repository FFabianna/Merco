package com.example.merco.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.merco.domain.model.DTO.ProductDTO
import com.example.merco.repository.CategoryRepositoryImpl
import com.example.merco.repository.ProductRepositoryImpl
import com.example.merco.viewmodel.CategoryViewModel
import com.example.merco.viewmodel.CategoryViewModelFactory
import com.example.merco.viewmodel.ProductViewModel
import com.example.merco.viewmodel.ProductViewModelFactory
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavController,
    categoryId: String,
    productViewModel: ProductViewModel,
    sellerId: String
) {
    val repository = ProductRepositoryImpl()
    val factory = ProductViewModelFactory(repository)
    val viewModel: ProductViewModel = viewModel(factory = factory)

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf(0) }
    var price by remember { mutableStateOf(0.0) }
    var discount by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var newPrice by remember { mutableStateOf(0.0) }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var authState by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.fetchProducts(categoryId)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
    }

    fun calculateNewPrice() {
        val discountValue = discount.toDoubleOrNull() ?: 0.0
        newPrice = price - (price * discountValue / 100)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Regresar",
                            )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor = Color.Gray
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
            )

            TextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Descripción del producto") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor = Color.Gray
                ),
                textStyle = LocalTextStyle.current.copy(color = Color.Black)
            )

            // Control de cantidad con flechas
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Cantidad disponible",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.Gray)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stock.toString(),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .width(30.dp)
                                    .fillMaxHeight()
                            ) {
                                IconButton(
                                    onClick = { stock++ },
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = "Incrementar",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { if (stock > 0) stock-- },
                                    modifier = Modifier
                                        .weight(1f)
                                        .size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Decrementar",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }


            TextField(
                value = price.toString(),
                onValueChange = { value ->

                    price = value.replace(",", ".").toDoubleOrNull() ?: 0.0
                },
                placeholder = { Text("Precio por unidad") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            TextField(
                value = discount,

                onValueChange = { value ->
                    discount = value
                    calculateNewPrice()
                },
                placeholder = { Text("Descuento en %") },
                modifier = Modifier.fillMaxWidth(),

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Text(
                text = "Precio con descuento: $ ${String.format("%.2f", newPrice)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

            TextField(
                value = reason,
                onValueChange = { reason = it },
                placeholder = { Text("Motivo del descuento") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor = Color.Gray
                )
            )

            // Selector de imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (selectedUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedUri),
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoCamera,
                            contentDescription = "Seleccionar imagen",
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                    }
                }
            }

            // Estado de carga
            when (authState) {
                1 -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                2 -> Text(
                    text = "Ocurrió un error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                3 -> LaunchedEffect(Unit) {
                    returnBack(navController) }
            }

            // Botón de agregar
            val context = LocalContext.current
            Button(
                onClick = {
                    if (selectedUri != null) {
                        val productDTO = ProductDTO(
                            name = name,
                            description = description,
                            price = price,
                            discount = discount.toDoubleOrNull() ?: 0.0,
                            newPrice = newPrice,
                            stock = stock,
                            reason = reason,
                            sellerId = sellerId
                        )
                        productViewModel.uploadImageAndAddProduct(
                            categoryId,
                            productDTO,
                            selectedUri!!
                        )
                        navController.popBackStack()

                    } else {
                        Toast.makeText(
                            context,
                            "Por favor selecciona una imagen",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC3545)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Agregar")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

fun returnBack(navController: NavController) {
    navController.popBackStack()
}