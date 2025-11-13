package com.example.inventario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Estructura de datos del producto
data class Product(
    val id: String,
    val name: String,
    val code: String,
    val stock: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onBack: () -> Unit,
    onAddProduct: () -> Unit // Navegar al formulario de agregar
) {
    // Lista de productos simulada
    val products = remember {
        mutableStateListOf(
            Product("P001", "Laptop HP ProBook", "ABC-123", 15),
            Product("P002", "Monitor Dell 24\"", "DEF-456", 5),
            Product("P003", "Teclado Mecánico", "GHI-789", 22),
            Product("P004", "Mouse Óptico", "JKL-012", 30)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario de Productos") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver a Inicio")
                    }
                }
            )
        },
        floatingActionButton = {
            // Botón flotante para agregar producto
            FloatingActionButton(onClick = onAddProduct) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Producto")
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        if (products.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No hay productos en el inventario. Presiona '+' para agregar uno.", style = MaterialTheme.typography.titleMedium)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(products) { product ->
                    ProductListItem(product = product)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun ProductListItem(product: Product) {
    ListItem(
        modifier = Modifier.padding(vertical = 4.dp),
        headlineContent = { Text(product.name, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text("Código: ${product.code}") },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Text("Stock", style = MaterialTheme.typography.labelSmall)
                val stockColor = if (product.stock < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                Text(
                    text = product.stock.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(color = stockColor)
                )
            }
        }
    )
}