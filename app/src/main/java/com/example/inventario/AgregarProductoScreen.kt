package com.example.inventario.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.inventario.data.InventarioDatabase
import com.example.inventario.data.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AgregarProductoScreen(
    db: InventarioDatabase,
    onProductoAgregado: () -> Unit
) {
    var codigoQR by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Agregar producto", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = codigoQR,
            onValueChange = { codigoQR = it },
            label = { Text("Código QR") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val producto = Producto(
                    codigoQR = codigoQR,
                    nombre = nombre,
                    cantidad = cantidad.toIntOrNull() ?: 0,
                    tipoOperacion = "manual",
                    fecha = System.currentTimeMillis()
                )
                scope.launch(Dispatchers.IO) {
                    db.productoDao().insertarProducto(producto)
                    onProductoAgregado()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}