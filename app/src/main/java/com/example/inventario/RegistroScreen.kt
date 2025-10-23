package com.example.inventario

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.inventario.data.AppDatabase
import com.example.inventario.data.Usuario
import kotlinx.coroutines.*

@Composable
fun RegistroScreen(
    context: Context,
    onRegistroExitoso: () -> Unit
) {
    val db = AppDatabase.obtenerInstancia(context)
    val usuarioDao = db.usuarioDao()

    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de Usuario", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nombre.isNotBlank() && contrasena.isNotBlank()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val existente = usuarioDao.buscarPorNombre(nombre)
                        if (existente == null) {
                            usuarioDao.insertar(Usuario(nombre, contrasena))
                            withContext(Dispatchers.Main) {
                                mensaje = "Usuario registrado correctamente"
                                onRegistroExitoso()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                mensaje = "Ese usuario ya existe"
                            }
                        }
                    }
                } else {
                    mensaje = "Completa todos los campos"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Registrar", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(mensaje, color = Color.Gray)
    }
}