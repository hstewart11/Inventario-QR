// Archivo: BluetoothFinder.kt (ejemplo en com.example.inventario.util)

package com.example.inventario.util

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.widget.Toast

// Clase simple para manejar la búsqueda de impresoras
class BluetoothFinder(private val context: Context) {

    // El adaptador Bluetooth es el punto de entrada
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    // Usamos @SuppressLint("MissingPermission") temporalmente porque el permiso
    // debe ser solicitado en la Activity/Composable antes de llamar a esta función.
    @SuppressLint("MissingPermission")
    fun obtenerDispositivosEmparejados(): List<BluetoothDevice> {
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "Bluetooth no soportado en este dispositivo", Toast.LENGTH_SHORT).show()
            return emptyList()
        }

        if (bluetoothAdapter?.isEnabled == false) {
            Toast.makeText(context, "Por favor, activa Bluetooth", Toast.LENGTH_SHORT).show()
            // Aquí deberías pedir al usuario que lo active (con un Intent)
            return emptyList()
        }

        // Retorna la lista de dispositivos ya emparejados
        return bluetoothAdapter?.bondedDevices?.toList() ?: emptyList()
    }
}