package com.example.ecorisk_manager.model

/**
 * Representa el resumen de información mostrado
 * en el panel principal de la aplicación.
 */
data class ResumenDashboard(
    val totalMateriales: Int = 0,
    val totalProveedores: Int = 0,
    val incidentesAbiertos: Int = 0
)