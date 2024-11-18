package com.example.merco


import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.merco.domain.model.Category
import com.example.merco.screens.AddCategoryScreen
import com.example.merco.screens.AddProductScreen
import com.example.merco.screens.LoginScreen
import com.example.merco.screens.MenuSellerScreen
import com.example.merco.screens.MenuUserScreen
import com.example.merco.screens.ProductsScreen
import com.example.merco.screens.ProfileUserScreen
import com.example.merco.screens.ProfileSellerScreen
import com.example.merco.screens.RegisterSellerScreen
import com.example.merco.screens.RegisterUserScreen
import com.example.merco.screens.SelectTypeUserScreen

import com.example.merco.ui.theme.MercoTheme
import com.example.merco.viewmodel.ProductViewModel
//import com.example.merco.ui.theme.White
import com.example.merco.viewmodel.SignupViewModel
import kotlinx.serialization.json.Json


class MainActivity : ComponentActivity() {

    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Observar el authState y mostrar mensajes de error o manejar navegación
        signupViewModel.authState.observe(this) { authState ->
            when (authState) {
                1 -> {
                    // Mostrar progreso en MainActivity o delegar en el Composable
                }
                2 -> {
                    // Mostrar un mensaje de error o manejarlo de alguna forma
                    Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show()
                }
                3 -> {
                    // Navegar a otra pantalla (por ejemplo, Profile) si la autenticación fue exitosa
                }
            }
        }

        signupViewModel.errorMessage.observe(this) { errorMessage ->
            // Mostrar el mensaje de error si existe
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        enableEdgeToEdge()
        setContent {
            MercoTheme{
                App()
            }
        }
    }
}


@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("profileUser") { ProfileUserScreen(navController) }
        composable("profileSeller") { ProfileSellerScreen(navController) }
        composable("registerUser") { RegisterUserScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("registerSeller") { RegisterSellerScreen(navController) }
        composable ("selectTypeUser"){ SelectTypeUserScreen(navController) }
        composable("menuUser") { MenuUserScreen(navController) }
        composable("menuSeller") { MenuSellerScreen(navController) }
        composable("addCategory") { AddCategoryScreen(navController) }


        composable(
            "productsScreen/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryJson = backStackEntry.arguments?.getString("category")
            if (categoryJson != null) {
                val category = Json.decodeFromString<Category>(categoryJson)
                val viewModel: ProductViewModel = viewModel() // Inicializa el ViewModel
                ProductsScreen(
                    category = category,
                    navController = navController,
                    viewModel = viewModel
                )
            } else {
                // Manejar el caso en que `categoryJson` sea nulo
                Log.e("Navigation", "Category JSON is null")
            }
        }

        composable("addProducts/{categoryId}") { backStackEntry ->
            // Recupera el parámetro de la ruta
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            AddProductScreen(navController, categoryId, productViewModel = viewModel(), sellerId = "")
        }







    }
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun SelectTypeUserPreview(){
    SelectTypeUserScreen(navController = rememberNavController())
}


@Preview(showBackground = true)
@Composable
fun AddCategoryPreview(){
    AddCategoryScreen(navController = rememberNavController())
}

/*
@Preview(showBackground = true)
@Composable
fun AddProductPreview(){
    AddProductScreen(navController = rememberNavController())
}*/


@Preview(showBackground = true)
@Composable
fun ProfileUserPreview() {
    ProfileUserScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun ProfileSellerPreview() {
    ProfileSellerScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun RegisterUserPreview() {
    RegisterUserScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun RegisterSellerPreview() {
    RegisterSellerScreen(navController = rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun MenuUserPreview()  {
    MenuUserScreen(navController = rememberNavController())
}


@Preview(showBackground = true)
@Composable
fun MenuSellerPreview()  {
    MenuSellerScreen(navController = rememberNavController())
}