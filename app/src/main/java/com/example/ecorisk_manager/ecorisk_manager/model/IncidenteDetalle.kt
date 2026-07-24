package com.example.ecorisk_manager.model

import androidx.room.ColumnInfo

data class IncidenteDetalle(
    @ColumnInfo(name = "id_incidente")
    val idIncidente: Int,

    @ColumnInfo(name = "fecha_incidente")
    val fechaIncidente: String,

    @ColumnInfo(name = "tipo_incidente")
    val tipoIncidente: String,

    @ColumnInfo(name = "descripcion")
    val descripcion: String,

    @ColumnInfo(name = "nivel_severidad")
    val nivelSeveridad: String,

    @ColumnInfo(name = "acciones_correctivas")
    val accionesCorrectivas: String,

    @ColumnInfo(name = "estado")
    val estado: String,

    @ColumnInfo(name = "id_material")
    val idMaterial: Int,

    @ColumnInfo(name = "codigo_material")
    val codigoMaterial: String,

    @ColumnInfo(name = "nombre_material")
    val nombreMaterial: String,

    @ColumnInfo(name = "clasificacion_riesgo")
    val clasificacionRiesgo: String
)