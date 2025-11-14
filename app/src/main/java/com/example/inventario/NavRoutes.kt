package com.example.inventario

// Definición única de las rutas de navegación
object NavRoutes {
    const val LOGIN = "login"                  // Punto de inicio del flujo de autenticación
    const val REGISTER = "register"
    const val HOME = "home"
    const val PRINTER_SETTINGS = "printer_settings"
    const val SCANNER = "scanner"
    const val QR_RESULT = "qr_result/{qrContent}"  // Ruta para mostrar el resultado del escaneo
    const val INVENTARIO = "inventario"
    const val AGREGAR_PRODUCTO = "agregar_producto"
}