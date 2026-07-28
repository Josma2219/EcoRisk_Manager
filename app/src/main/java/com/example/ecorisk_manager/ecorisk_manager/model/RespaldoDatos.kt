package com.example.ecorisk_manager.model

import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.entity.MaterialProveedorEntity
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.data.entity.UsuarioEntity

/**
 * Representa la estructura de un archivo de respaldo.
 * Agrupa toda la información necesaria para restaurar
 * la base de datos de la aplicación.
 */
data class RespaldoDatos(

    val fechaGeneracion: String,

    // Permite identificar la versión del formato del respaldo.
    val versionRespaldo: Int = 1,

    val usuarios: List<UsuarioEntity>,
    val materiales: List<MaterialPeligrosoEntity>,
    val proveedores: List<ProveedorEntity>,
    val hojasSeguridad: List<HojaSeguridadEntity>,
    val materialesProveedores: List<MaterialProveedorEntity>,
    val incidentes: List<IncidenteEntity>
)