package com.example.inventario.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios WHERE nombre = :nombre AND contrasena = :contrasena LIMIT 1")
    fun validar(nombre: String, contrasena: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE nombre = :nombre LIMIT 1")
    fun buscarPorNombre(nombre: String): Usuario?

    @Insert
    fun insertar(usuario: Usuario)
}