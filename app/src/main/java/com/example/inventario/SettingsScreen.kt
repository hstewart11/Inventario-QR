package com.example.inventario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    // ¡NUEVO! Función para navegar a la configuración de la impresora
    onNavigateToPrinterSettings: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {
            // Opción para CONFIGURAR IMPRESORA
            ListItem(
                modifier = Modifier.clickable { onNavigateToPrinterSettings() },
                headlineContent = { Text("Configuración de Impresora") },
                supportingContent = { Text("Añade o edita tu impresora de recibos.") },
                leadingContent = {
                    Icon(
                        Icons.Filled.Print,
                        contentDescription = "Impresora"
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Filled.ArrowForward,
                        contentDescription = "Ir a configuración"
                    )
                }
            )
            Divider()

            // Opción de versión
            ListItem(
                headlineContent = { Text("Versión de la Aplicación") },
                supportingContent = { Text("1.0.0") }
            )
            Divider()

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de Cerrar Sesión
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar Sesión",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}