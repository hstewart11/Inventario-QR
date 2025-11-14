package com.example.inventario.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inventario.data.InventarioDatabase
import com.example.inventario.data.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun InventarioScreen(db: InventarioDatabase) {
    val scope = rememberCoroutineScope()
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            productos = db.productoDao().obtenerTodo()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Inventario completo", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(productos) { producto ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("QR: ${producto.codigoQR}", style = MaterialTheme.typography.titleMedium)
                        Text("Nombre: ${producto.nombre}")
                        Text("Cantidad: ${producto.cantidad}")
                        Text("Operación: ${producto.tipoOperacion}")
                        Text("Fecha: ${producto.fecha}")
                    }
                }
            }
        }
    }
}