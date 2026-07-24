package com.example.ecorisk_manager.model

import androidx.room.ColumnInfo

data class HojaSeguridadDetalle(
    @ColumnInfo(name = "id_hoja")
    val idHoja: Int,

    @ColumnInfo(name = "version")
    val version: String,

    @ColumnInfo(name = "fecha_emision")
    val fechaEmision: String,

    @ColumnInfo(name = "archivo_pdf")
    val archivoPdf: String,

    @ColumnInfo(name = "observaciones")
    val observaciones: String,

    @ColumnInfo(name = "id_material")
    val idMaterial: Int,

    @ColumnInfo(name = "codigo_material")
    val codigoMaterial: String,

    @ColumnInfo(name = "nombre_material")
    val nombreMaterial: String,

    @ColumnInfo(name = "clasificacion_riesgo")
    val clasificacionRiesgo: String
)