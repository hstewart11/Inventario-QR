package com.example.inventario

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Asumiendo que LoginScreen, RegistroScreen, HomeScreen, NavRoutes y PrinterSettingsScreen existen en este paquete.

@Composable
fun AppNavGraph(context: Context) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        // La aplicación inicia en la pantalla de LOGIN
        startDestination = NavRoutes.LOGIN
    ) {
        // --- RUTA 0: LOGIN (Parámetros confirmados por LoginScreen.kt) ---
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                context = context, // LoginScreen también pide el contexto
                onLoginExitoso = { navController.navigate(NavRoutes.HOME) {
                    popUpTo(NavRoutes.LOGIN) { inclusive = true }
                } },
                onIrARegistro = { navController.navigate(NavRoutes.REGISTER) }
            )
        }

        // --- RUTA 1: REGISTRO (Parámetros confirmados por RegistroScreen.kt) ---
        composable(NavRoutes.REGISTER) {
            // CORRECCIÓN CRÍTICA: La función se llama RegistroScreen, no RegisterScreen.
            // La función solo espera 'context' y 'onRegistroExitoso'.
            RegistroScreen(
                context = context,
                onRegistroExitoso = { navController.navigate(NavRoutes.HOME) {
                    // Opcional: Navegar a HOME al registrarse
                    popUpTo(NavRoutes.LOGIN) { inclusive = true }
                } }
                // Nota: Tu RegistroScreen.kt no tiene el callback onNavigateBack, así que lo eliminamos.
            )
        }

        // --- RUTA 2: HOME (Parámetros confirmados por HomeScreen.kt) ---
        composable(NavRoutes.HOME) {
            // HomeScreen no necesita el contexto.
            HomeScreen(
                onNavigateToSettings = { navController.navigate(NavRoutes.PRINTER_SETTINGS) }
            )
        }

        // --- RUTA 3: AJUSTES DE IMPRESORA ---
        composable(NavRoutes.PRINTER_SETTINGS) {
            PrinterSettingsScreen(
                context = context,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
    }
}