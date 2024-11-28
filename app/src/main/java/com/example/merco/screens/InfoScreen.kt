package com.example.merco.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.nio.file.WatchEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    navController: NavController
) {
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
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            ExpandableItem(
                title = "No veo ofertas. ¿Por qué?",
                content = "Es posible que no haya productos próximos a vencer en este momento, o que los Sellers aún no hayan cargado nuevas ofertas. Te recomendamos revisar más tarde o activar notificaciones para estar al tanto cuando haya nuevos productos en promoción."
            )
            ExpandableItem(
                title = "¿Puedo separar varios pedidos a la vez?",
                content = "Sí, puedes separar varios productos de diferentes Sellers en la aplicación. Recibirás una confirmación por cada producto separado, y deberás recogerlos según las indicaciones de cada tienda."
            )
            ExpandableItem(
                title = "¿Cómo saben cuál es mi pedido en la tienda?",
                content = "Cuando separas un producto, generamos un código único asociado a tu pedido. Debes mostrar este código al Seller cuando llegues a la tienda para recoger tu producto."
            )
            ExpandableItem(
                title = "¿Cómo unirme si tengo una tienda?",
                content = "Si eres un Seller, puedes registrarte fácilmente desde la opción 'Unirse como tienda'. Completa la información de tu tienda, sube tus productos próximos a vencer y comienza a recibir notificaciones cuando los usuarios separen tus productos."
            )
            ExpandableItem(
                title = "¿Cómo cancelo mi pedido?",
                content = "Puedes cancelar tu pedido desde la sección 'Mis pedidos' en la aplicación. Esto notificará al Seller, liberando el producto para que otros usuarios puedan separarlo."
            )
            ExpandableItem(
                title = "¿Por qué los precios son más bajos?",
                content = "Los precios son más bajos porque los productos están próximos a vencer. Esto ayuda a los Sellers a reducir desperdicios y a los Users a aprovechar descuentos significativos en alimentos y productos de calidad."
            )

        }
    }
}

@Composable
fun ExpandableItem(
    title: String,
    content: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        //elevation = 0.dp,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                if (expanded) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropUp,
                        contentDescription = "Colapsar",
                        tint = Color.Black
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Expandir",
                        tint = Color.Black
                    )
                }
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(content, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}