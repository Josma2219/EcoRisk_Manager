package com.example.ecorisk_manager.model

import androidx.room.ColumnInfo

/**
 * Representa la información detallada de la relación entre
 * un material peligroso y un proveedor.
 * Se utiliza para mostrar los datos obtenidos al combinar
 * ambas entidades en una misma consulta.
 */
data class MaterialProveedorDetalle(

    @ColumnInfo(name = "id_material_proveedor")
    val idMaterialProveedor: Int,

    @ColumnInfo(name = "id_material")
    val idMaterial: Int,

    @ColumnInfo(name = "id_proveedor")
    val idProveedor: Int,

    @ColumnInfo(name = "nombre_material")
    val nombreMaterial: String,

    @ColumnInfo(name = "codigo_material")
    val codigoMaterial: String,

    @ColumnInfo(name = "clasificacion_riesgo")
    val clasificacionRiesgo: String,

    @ColumnInfo(name = "nombre_proveedor")
    val nombreProveedor: String,

    @ColumnInfo(name = "correo_proveedor")
    val correoProveedor: String,

    @ColumnInfo(name = "precio_referencia")
    val precioReferencia: Double
)