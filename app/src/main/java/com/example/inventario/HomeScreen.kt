package com.example.inventario

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToInventory: () -> Unit
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
                onNavigateToSettings = onNavigateToSettings,
                onNavigateToInventory = onNavigateToInventory
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

// Contenido del menú plegable (Drawer)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    onCloseDrawer: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToInventory: () -> Unit
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
                painter = painterResource(id = R.drawable.disenoqr),
                contentDescription = "QR Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            // 1. Ver Inventario
            DrawerItem(
                text = "Ver Inventario",
                icon = Icons.Filled.Inventory,
                onClick = {
                    onCloseDrawer()
                    onNavigateToInventory()
                }
            )

            // 2. Analizar QR
            DrawerItem(
                text = "Analizar QR",
                icon = Icons.Filled.QrCodeScanner,
                onClick = {
                    onCloseDrawer()
                }
            )



            HorizontalDivider()

            // 4. OPCIÓN DE AJUSTES
            DrawerItem(
                text = "Ajustes de Impresora",
                icon = Icons.Filled.Settings,
                onClick = {
                    onCloseDrawer()
                    onNavigateToSettings()
                }
            )
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