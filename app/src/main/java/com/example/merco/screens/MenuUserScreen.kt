package com.example.merco.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MenuUserScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra superior con botones para perfil y mapa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para ir al perfil
            Button(
                onClick = { navController.navigate("profileUser") },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(text = "Perfil", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            // Botón para ver el mapa
            Button(
                onClick = { /* Navegar al mapa (implementación futura) */ },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(text = "Mapa", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Barra de búsqueda
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            BasicTextField(
                value = "",
                onValueChange = { /* Implementación de búsqueda futura */ },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if ("".isEmpty()) { // Aquí reemplazamos `it.text.isEmpty()` con `value.isEmpty()`
                        Text("Buscar productos...", color = Color.Gray)
                    }
                    innerTextField()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de productos
        Text(
            text = "Productos",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Simulación de productos (para mostrar cómo se verá la lista)
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(5) { index ->
                ProductCard("Producto ${index + 1}")
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ProductCard(productName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Text(text = productName, color = Color.Black)
    }
}
