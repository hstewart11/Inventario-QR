package com.example.inventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventario.ui.theme.InventarioTheme
// Asegúrate de que todos los imports necesarios estén aquí, incluyendo RegistroScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventarioTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "login") {

            // RUTA DE LOGIN
            composable("login") {
                LoginScreen(
                    context = navController.context,
                    onIrARegistro = { navController.navigate("registro") },
                    // CONEXIÓN CLAVE: Navegar a 'home'
                    onLoginExitoso = {
                        navController.navigate("home") {
                            // Esto evita que el usuario regrese a Login con el botón "Atrás"
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            // RUTA DE REGISTRO
            composable("registro") {
                // Asumo que tienes una función Composable RegistroScreen()
                RegistroScreen(
                    context = navController.context,
                    onRegistroExitoso = { navController.popBackStack() }
                )
            }

            // NUEVA RUTA: HOME (Pantalla Principal)
            composable("home") {
                HomeScreen() // Llama a la nueva pantalla
            }
        }
    }
}