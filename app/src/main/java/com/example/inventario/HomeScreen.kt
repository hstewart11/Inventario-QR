// Archivo: HomeScreen.kt

package com.example.inventario

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

// Importamos la anotación para el TopAppBar y ModalNavigationDrawer
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    // ¡NUEVO! Función para la navegación a la pantalla de Ajustes
    onNavigateToSettings: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onCloseDrawer = {
                    scope.launch { drawerState.close() }
                },
                onNavigateToSettings = onNavigateToSettings // Pasamos la función al Drawer
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Inventario QR") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                // Alternar el estado del menú
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Abrir Menú")
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxSize()
            ) {
                // SALUDO
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))

                // SECCIÓN DE STOCK BAJO (Contenido temporal)
                Text(
                    text = "Productos con Stock Bajo:",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Actualmente no hay productos con stock bajo.",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

// Contenido del menú plegable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    onCloseDrawer: () -> Unit,
    onNavigateToSettings: () -> Unit // ¡NUEVO! Función para la navegación
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // LOGO QR
            Image(
                painter = painterResource(id = R.drawable.disenoqr), // Asegúrate de que R.drawable.disenoqr exista
                contentDescription = "QR Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            // OPCIONES DE NAVEGACIÓN
            DrawerItem(text = "Ver Inventario", icon = null, onClick = {
                onCloseDrawer()
                // Aquí iría la navegación real (e.g., navController.navigate(NavRoutes.INVENTORY))
            })
            DrawerItem(text = "Analizar QR", icon = null, onClick = {
                onCloseDrawer()
                // Aquí iría la navegación real (e.g., navController.navigate(NavRoutes.SCANNER))
            })
            DrawerItem(text = "Agregar Inventario", icon = null, onClick = {
                onCloseDrawer()
                // Aquí iría la navegación real (e.g., navController.navigate(NavRoutes.ADD_ITEM))
            })
            HorizontalDivider()

            // OPCIÓN DE AJUSTES (llama a la función de navegación)
            DrawerItem(text = "Ajustes", icon = null, onClick = {
                onCloseDrawer()
                onNavigateToSettings() // <-- Navega a la pantalla de Ajustes
            })
        }
    }
}

// Elemento reutilizable para las opciones del menú
@Composable
fun DrawerItem(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector?, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(text) },
        selected = false,
        onClick = onClick,
        icon = {
            if (icon != null) {
                Icon(icon, contentDescription = text)
            }
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}