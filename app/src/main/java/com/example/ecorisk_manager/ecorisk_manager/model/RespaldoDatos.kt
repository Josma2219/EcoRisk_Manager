package com.example.ecorisk_manager.model

import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.entity.MaterialProveedorEntity
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.data.entity.UsuarioEntity

data class RespaldoDatos(
    val fechaGeneracion: String,
    val versionRespaldo: Int = 1,
    val usuarios: List<UsuarioEntity>,
    val materiales: List<MaterialPeligrosoEntity>,
    val proveedores: List<ProveedorEntity>,
    val hojasSeguridad: List<HojaSeguridadEntity>,
    val materialesProveedores: List<MaterialProveedorEntity>,
    val incidentes: List<IncidenteEntity>
)