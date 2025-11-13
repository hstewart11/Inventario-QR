package com.example.inventario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBack: () -> Unit,
    onProductSaved: () -> Unit // Para volver a la lista después de guardar
) {
    // Estados para los campos de entrada
    var productName by remember { mutableStateOf("") }
    var productCode by remember { mutableStateOf("") }
    var productStock by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }

    // Estado simulado para el QR
    var qrCodeData by remember { mutableStateOf("Generar QR para el producto") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Nuevo Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(
                        onClick = onProductSaved, // Simulación de guardar
                        enabled = productName.isNotBlank() && productCode.isNotBlank()
                    ) {
                        Icon(Icons.Filled.Save, contentDescription = "Guardar", modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("GUARDAR")
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Detalles Básicos", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Campo 1: Nombre del Producto
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Nombre del Producto *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Campo 2: Código (Inventario o SKU)
                OutlinedTextField(
                    value = productCode,
                    onValueChange = { productCode = it },
                    label = { Text("Código/SKU *") },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                    singleLine = true
                )

                // Campo 3: Stock Inicial
                OutlinedTextField(
                    value = productStock,
                    onValueChange = { productStock = it.filter { it.isDigit() } },
                    label = { Text("Stock Inicial") },
                    // ESTO RESUELVE EL ERROR DE KeyboardOptions si el Gradle es correcto
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f).padding(start = 8.dp),
                    singleLine = true
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Campo 4: Descripción
            OutlinedTextField(
                value = productDescription,
                onValueChange = { productDescription = it },
                label = { Text("Descripción (Opcional)") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp),
            )
            Spacer(modifier = Modifier.height(32.dp))

            // --- SECCIÓN DE GENERACIÓN DE CÓDIGO QR ---
            Text("Generación de Código QR", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder para el código QR
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.QrCode, contentDescription = "QR Placeholder", modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(qrCodeData, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para generar el QR
            Button(
                onClick = {
                    qrCodeData = "QR generado para: $productCode"
                },
                enabled = productCode.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GENERAR CÓDIGO QR")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}