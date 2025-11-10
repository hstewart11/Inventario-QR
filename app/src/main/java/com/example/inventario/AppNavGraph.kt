package com.example.inventario

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Se asume que HomeScreen y NavRoutes están definidos y son accesibles.

@Composable
fun AppNavGraph(context: Context) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        // --- RUTA 1: HOME ---
        composable(NavRoutes.HOME) {
            HomeScreen(
                onNavigateToSettings = { navController.navigate(NavRoutes.PRINTER_SETTINGS) }
            )
        }

        // --- RUTA 2: AJUSTES DE IMPRESORA ---
        composable(NavRoutes.PRINTER_SETTINGS) {
            PrinterSettingsScreen(
                context = context,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
    }
}