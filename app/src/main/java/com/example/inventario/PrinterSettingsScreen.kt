package com.example.inventario

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.* import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* import androidx.compose.material3.* import androidx.compose.runtime.* import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.content.Context
import android.widget.Toast

// --- IMPORTACIONES BLUETOOTH Y UTILIDADES ---
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.Manifest
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Extensión para obtener la actividad desde el contexto
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> this.baseContext.findActivity()
    else -> null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrinterSettingsScreen(
    context: Context,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val activity = context.findActivity()

    // --- ESTADOS DE CONFIGURACIÓN Y BLUETOOTH ---
    var nombre by remember { mutableStateOf("Imp. de Inventario") }
    var bluetoothAddress by remember { mutableStateOf("86:67:7A:56:93:3E") }
    var isSearching by remember { mutableStateOf(false) }
    var showDeviceDialog by remember { mutableStateOf(false) }

    var foundDevices by remember { mutableStateOf(emptyList<BluetoothDevice>()) }

    val bluetoothAdapter: BluetoothAdapter? = remember { BluetoothAdapter.getDefaultAdapter() }

    // --- FUNCIÓN DE BÚSQUEDA DECLARADA COMO 'lateinit' REFERENCE HOLDER ---
    // Esto evita el ciclo de referencia. La inicializaremos después de los launchers.
    lateinit var startBluetoothSearch: () -> Unit

    // --- LANZADORES DE ACTIVIDADES (VAN PRIMERO para que su código vea la función) ---

    // 1. Lanzador para solicitar permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val granted = permissions.values.all { it }
        // La referencia existe aunque la función aún no esté inicializada
        if (granted) startBluetoothSearch.invoke()
        else {
            Toast.makeText(context, "Permisos de Bluetooth denegados.", Toast.LENGTH_SHORT).show()
            isSearching = false
        }
    }

    // 2. Lanzador para solicitar que se active el Bluetooth
    val enableBluetoothLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // La referencia existe aunque la función aún no esté inicializada
        if (result.resultCode == Activity.RESULT_OK) startBluetoothSearch.invoke()
        else {
            Toast.makeText(context, "Bluetooth no activado.", Toast.LENGTH_SHORT).show()
            isSearching = false
        }
    }

    // --- INICIALIZACIÓN DE LA FUNCIÓN DE BÚSQUEDA (VA DESPUÉS de los launchers) ---
    startBluetoothSearch = remember {
        { // Inicio del Lambda
            // 1. Si ya está buscando, salimos inmediatamente.
            if (isSearching) {
                // No hacer nada
            } else {
                isSearching = true

                // 2. Verificar Adaptador
                if (bluetoothAdapter == null) {
                    Toast.makeText(context, "Bluetooth no soportado en este dispositivo.", Toast.LENGTH_LONG).show()
                    isSearching = false
                } else {
                    // 3. Verificar y solicitar permisos
                    val requiredPermissions = arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN
                    )
                    val missingPermissions = requiredPermissions.filter {
                        ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
                    }

                    if (missingPermissions.isNotEmpty()) {
                        // Los launchers ya están definidos y se pueden llamar
                        permissionLauncher.launch(missingPermissions.toTypedArray())
                    } else {
                        // 4. Verificar si Bluetooth está encendido
                        if (!bluetoothAdapter.isEnabled) {
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            // Los launchers ya están definidos y se pueden llamar
                            enableBluetoothLauncher.launch(enableBtIntent)
                        } else {
                            // 5. Ejecutar la búsqueda de dispositivos EMPAREJADOS (Lógica final)
                            Toast.makeText(context, "Buscando dispositivos Bluetooth emparejados...", Toast.LENGTH_SHORT).show()

                            CoroutineScope(Dispatchers.IO).launch {
                                // Aseguramos que la lista se inicialice solo con dispositivos emparejados
                                val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter.bondedDevices

                                launch(Dispatchers.Main) {
                                    foundDevices = pairedDevices?.toList() ?: emptyList()
                                    isSearching = false // Terminó la búsqueda
                                    showDeviceDialog = true
                                }
                            }
                        }
                    }
                }
            }
        } // Fin del Lambda
    }

    // --- CUERPO PRINCIPAL (UI) ---
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar impresora") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") }
                },
                actions = {
                    TextButton(onClick = onSave, enabled = !isSearching) {
                        Text("GUARDAR", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre de la Impresora") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
            Spacer(modifier = Modifier.height(16.dp))

            // ... (Campos de configuración simulados) ...
            Text("Modelo de la impresora", style = MaterialTheme.typography.labelLarge)
            Row(modifier = Modifier.fillMaxWidth().clickable { /* Abre Dropdown */ }, verticalAlignment = Alignment.CenterVertically) { Text("Otro modelo", modifier = Modifier.weight(1f)); Icon(Icons.Filled.ArrowDropDown, contentDescription = "Seleccionar modelo") }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Interfaz", style = MaterialTheme.typography.labelLarge)
            Row(modifier = Modifier.fillMaxWidth().clickable { /* Abre Dropdown */ }, verticalAlignment = Alignment.CenterVertically) { Text("Bluetooth", modifier = Modifier.weight(1f)); Icon(Icons.Filled.ArrowDropDown, contentDescription = "Seleccionar interfaz") }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // 4. Impresora Bluetooth y Botón Buscar
            Text("Dirección Bluetooth", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = bluetoothAddress,
                    onValueChange = { bluetoothAddress = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    label = { Text("MAC Address / Nombre") }
                )
                Spacer(modifier = Modifier.width(8.dp))

                // Botón BUSCAR
                Button(
                    onClick = { startBluetoothSearch.invoke() }, // Llamada al lambda inicializado
                    enabled = !isSearching,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSearching) Color.Gray else MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isSearching) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Text("BUSCAR")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Ancho de papel", style = MaterialTheme.typography.labelLarge)
            Row(modifier = Modifier.fillMaxWidth().clickable { /* Abre Dropdown */ }, verticalAlignment = Alignment.CenterVertically) { Text("58 mm", modifier = Modifier.weight(1f)); Icon(Icons.Filled.ArrowDropDown, contentDescription = "Seleccionar ancho") }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            // --- Configuración Avanzada ---
            Text("Configuración Avanzada", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            ListItem(headlineContent = { Text("Imprimir recibos") }, trailingContent = { Switch(checked = true, onCheckedChange = { /* state update */ }) }, modifier = Modifier.fillMaxWidth())
            ListItem(headlineContent = { Text("Imprimir recibos automáticamente") }, trailingContent = { Switch(checked = false, onCheckedChange = { /* state update */ }) }, modifier = Modifier.fillMaxWidth())
            HorizontalDivider()
            Spacer(modifier = Modifier.height(32.dp))

            // 8. Impresión de Prueba
            Button(onClick = { Toast.makeText(context, "Simulando impresión de prueba a $bluetoothAddress", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.primary)) {
                Icon(Icons.Filled.Print, contentDescription = "Impresión de prueba"); Spacer(modifier = Modifier.width(8.dp)); Text("IMPRESIÓN DE PRUEBA")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // 9. Eliminar Impresora
            TextButton(onClick = { Toast.makeText(context, "Impresora $nombre eliminada.", Toast.LENGTH_SHORT).show(); onBack() }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar impresora", tint = MaterialTheme.colorScheme.error); Spacer(modifier = Modifier.width(8.dp)); Text("ELIMINAR IMPRESORA", color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // --- DIÁLOGO DE SELECCIÓN DE DISPOSITIVO (Resultados Reales) ---
    if (showDeviceDialog) {
        AlertDialog(
            onDismissRequest = { showDeviceDialog = false },
            title = { Text("Dispositivos Emparejados") },
            text = {
                Column {
                    if (foundDevices.isEmpty()) {
                        Text("No se encontraron dispositivos Bluetooth emparejados.")
                    } else {
                        foundDevices.forEach { device ->
                            ListItem(
                                headlineContent = { Text(device.name ?: "Nombre Desconocido") },
                                supportingContent = { Text(device.address) },
                                modifier = Modifier.clickable {
                                    bluetoothAddress = device.address // Actualizar la dirección real
                                    Toast.makeText(context, "Seleccionada: ${device.address}", Toast.LENGTH_SHORT).show()
                                    showDeviceDialog = false
                                }
                            )
                            HorizontalDivider()
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showDeviceDialog = false }) { Text("Cerrar") }
            }
        )
    }
}