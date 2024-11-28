package com.example.merco.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SelectTypeUserScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "¿Quién eres?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Comprador
        Button(
            onClick =  { navController.navigate("registerUser") },
            colors = ButtonDefaults.buttonColors(Color.Red),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(text = "COMPRADOR", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de Vendedor
        Button(
            onClick = { navController.navigate("registerSeller") },
            colors = ButtonDefaults.buttonColors(Color.Red),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp) // Combinamos el padding aquí
        ) {
            Text(text = "VENDEDOR", color = Color.White)
        }
        Spacer(modifier = Modifier.height(24.dp))

    }
}
