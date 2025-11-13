package com.example.inventario

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inventario.data.InventarioDatabase

@Composable
fun AppNavGraph(
    context: Context,
    db: InventarioDatabase
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.LOGIN
    ) {
        // --- RUTA 0: LOGIN ---
        composable(NavRoutes.LOGIN) {
            LoginScreen(
                context = context,
                onLoginExitoso = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                },
                onIrARegistro = { navController.navigate(NavRoutes.REGISTER) }
            )
        }

        // --- RUTA 1: REGISTRO ---
        composable(NavRoutes.REGISTER) {
            RegistroScreen(
                context = context,
                onRegistroExitoso = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // --- RUTA 2: HOME ---
        composable(NavRoutes.HOME) {
            HomeScreen(
                navController = navController,
                db = db,
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

        // --- RUTA 4: ESCÁNER QR ---
        composable(NavRoutes.SCANNER) {
            ScannerScreen(
                navController = navController,
                db = db
            )
        }

        // --- RUTA 5: RESULTADO QR ---
        composable("qr_result/{qrContent}") { backStackEntry ->
            val qrContent = backStackEntry.arguments?.getString("qrContent") ?: ""
            QRResultScreen(
                qrContent = qrContent,
                onBack = { navController.popBackStack() }
            )
        }
    }
}