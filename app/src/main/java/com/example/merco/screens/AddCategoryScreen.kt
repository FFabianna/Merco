package com.example.merco.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.example.merco.viewmodel.CategoryViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*

import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.merco.domain.model.Category
import com.example.merco.domain.model.User
import com.example.merco.repository.CategoryRepositoryImpl
import com.example.merco.viewmodel.CategoryViewModelFactory

@Composable
fun AddCategoryScreen(
    navController: NavController,
    categoryViewModel: CategoryViewModel = viewModel()
) {
    val repository = CategoryRepositoryImpl()
    val factory = CategoryViewModelFactory(repository)
    val viewModel: CategoryViewModel = viewModel(factory = factory)

    val authState by categoryViewModel.authState.observeAsState()

    val categories by viewModel.categories.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    var name by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedUri = uri
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nueva Categoría",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campo para el nombre
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la categoría") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            // Mostrar la imagen seleccionada (si existe)
            selectedUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(vertical = 8.dp)
                )
            }

            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Seleccionar Imagen")
            }

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
                        navController.navigate("menuSeller")
                    }
                }
            }

            val context = LocalContext.current

            Button(
                onClick = {
                    if (selectedUri != null) {
                        categoryViewModel.uploadImageAndAddCategory(name, selectedUri!!)
                    } else {
                        Toast.makeText(
                            context, // Usa 'context' aquí
                            "Por favor selecciona una imagen",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(text = "Agregar", color = Color.White)
            }

        }
    }
}


