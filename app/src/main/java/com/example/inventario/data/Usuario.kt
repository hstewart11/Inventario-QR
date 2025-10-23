package com.example.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "contrasena")
    val contrasena: String
)