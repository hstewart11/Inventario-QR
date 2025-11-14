package com.example.inventario.data

import androidx.room.*

@Dao
interface ProductoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarProducto(producto: Producto)

    @Query("SELECT * FROM productos ORDER BY fecha DESC")
    suspend fun obtenerHistorial(): List<Producto>

    @Query("SELECT * FROM productos")
    suspend fun obtenerTodo(): List<Producto>
}