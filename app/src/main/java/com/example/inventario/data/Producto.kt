package com.example.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val codigoQR: String,
    val nombre: String,
    val cantidad: Int,
    val tipoOperacion: String, // "ingreso" o "salida"
    val fecha: Long // timestamp
)