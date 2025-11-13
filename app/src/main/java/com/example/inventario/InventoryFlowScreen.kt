package com.example.inventario

import androidx.compose.runtime.*

// Definimos los estados de la pantalla de Inventario
enum class InventoryScreenState {
    PRODUCT_LIST,
    ADD_PRODUCT
}

@Composable
fun InventoryFlowScreen(onBack: () -> Unit) {
    // Usamos un estado para saber si estamos viendo la lista o añadiendo un producto
    var currentState by remember { mutableStateOf(InventoryScreenState.PRODUCT_LIST) }

    when (currentState) {
        InventoryScreenState.PRODUCT_LIST -> {
            // Llama a la pantalla principal de la lista
            ProductListScreen(
                onBack = onBack,
                onAddProduct = { currentState = InventoryScreenState.ADD_PRODUCT }
            )
        }
        InventoryScreenState.ADD_PRODUCT -> {
            // Llama a la pantalla para agregar un producto
            AddProductScreen(
                onBack = { currentState = InventoryScreenState.PRODUCT_LIST },
                onProductSaved = { currentState = InventoryScreenState.PRODUCT_LIST } // Volver después de guardar
            )
        }
    }
}