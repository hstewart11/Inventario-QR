package com.example.inventario

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Define tus rutas de navegación (NavRoutes debe existir en tu proyecto)
object NavRoutes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val INVENTORY = "inventory_flow" // Apunta al flujo principal
    const val PRINTER_SETTINGS = "printer_settings"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    context: Context
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.LOGIN
    ) {
        // --- RUTA 0: LOGIN ---
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                context = context,
                onLoginExitoso = { navController.navigate(NavRoutes.HOME) {
                    popUpTo(NavRoutes.LOGIN) { inclusive = true }
                } },
                onIrARegistro = { navController.navigate(NavRoutes.REGISTER) }
            )
        }

        // --- RUTA 1: REGISTRO ---
        composable(NavRoutes.REGISTER) {
            RegistroScreen(
                context = context,
                onRegistroExitoso = { navController.navigate(NavRoutes.HOME) {
                    popUpTo(NavRoutes.LOGIN) { inclusive = true }
                } }
            )
        }

        // --- RUTA 2: HOME ---
        composable(NavRoutes.HOME) {
            HomeScreen(
                onNavigateToSettings = { navController.navigate(NavRoutes.PRINTER_SETTINGS) },
                onNavigateToInventory = { navController.navigate(NavRoutes.INVENTORY) }
            )
        }

        // --- RUTA 3: INVENTARIO (Apunta al gestor de estados) ---
        composable(NavRoutes.INVENTORY) {
            // InventoryFlowScreen gestiona la vista de lista y la de agregar producto
            InventoryFlowScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // --- RUTA 4: AJUSTES DE IMPRESORA ---
        composable(NavRoutes.PRINTER_SETTINGS) {
            PrinterSettingsScreen(
                context = context,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
    }
}