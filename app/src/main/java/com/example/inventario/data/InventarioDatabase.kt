package com.example.inventario.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Producto::class], version = 1)
abstract class InventarioDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
}