package com.example.ecorisk_manager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa un material peligroso registrado en el sistema.
 * Almacena la información básica utilizada en los diferentes módulos
 * de la aplicación.
 */
@Entity(
    tableName = "materiales_peligrosos",
    indices = [
        // Evita que existan materiales con el mismo código de identificación.
        Index(value = ["codigo_material"], unique = true)
    ]
)
data class MaterialPeligrosoEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_material")
    val idMaterial: Int = 0,

    @ColumnInfo(name = "codigo_material")
    val codigoMaterial: String,

    @ColumnInfo(name = "nombre_comercial")
    val nombreComercial: String,

    @ColumnInfo(name = "descripcion")
    val descripcion: String,

    @ColumnInfo(name = "clasificacion_riesgo")
    val clasificacionRiesgo: String,

    @ColumnInfo(name = "unidad_medida")
    val unidadMedida: String,

    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: String,

    @ColumnInfo(name = "estado")
    val estado: String
)