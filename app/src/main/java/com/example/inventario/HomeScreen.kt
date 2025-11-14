package com.example.inventario

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.inventario.data.InventarioDatabase
import com.example.inventario.data.Producto
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    db: InventarioDatabase,
    onNavigateToSettings: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val productos by produceState<List<Producto>>(initialValue = emptyList()) {
        value = db.productoDao().obtenerHistorial()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                onCloseDrawer = { scope.launch { drawerState.close() } },
                onNavigateToSettings = onNavigateToSettings
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
                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Historial de productos escaneados:",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (productos.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = "No hay productos registrados aún.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn {
                        items(productos) { producto ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("QR: ${producto.codigoQR}", fontWeight = FontWeight.Bold)
                                    Text("Nombre: ${producto.nombre}")
                                    Text("Cantidad: ${producto.cantidad}")
                                    Text("Operación: ${producto.tipoOperacion}")
                                    Text("Fecha: ${formatearFecha(producto.fecha)}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatearFecha(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContent(
    navController: NavHostController,
    onCloseDrawer: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.disenoqr),
                contentDescription = "QR Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()

            DrawerItem(text = "Ver Inventario", icon = Icons.Default.Inventory, onClick = {
                onCloseDrawer()
                navController.navigate(NavRoutes.INVENTARIO)
            })
            DrawerItem(text = "Agregar Inventario", icon = Icons.Default.Add, onClick = {
                onCloseDrawer()
                navController.navigate(NavRoutes.AGREGAR_PRODUCTO)
            })
            DrawerItem(text = "Analizar QR", icon = Icons.Default.QrCodeScanner, onClick = {
                onCloseDrawer()
                navController.navigate(NavRoutes.SCANNER)
            })
            HorizontalDivider()
            DrawerItem(text = "Ajustes", icon = Icons.Default.Settings, onClick = {
                onCloseDrawer()
                onNavigateToSettings()
            })
        }
    }
}

@Composable
fun DrawerItem(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    onClick: () -> Unit
) {
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