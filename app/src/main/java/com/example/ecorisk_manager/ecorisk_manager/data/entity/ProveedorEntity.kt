package com.example.ecorisk_manager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "proveedores")
data class ProveedorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_proveedor")
    val idProveedor: Int = 0,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "telefono")
    val telefono: String,

    @ColumnInfo(name = "correo")
    val correo: String,

    @ColumnInfo(name = "direccion")
    val direccion: String,

    @ColumnInfo(name = "contacto_principal")
    val contactoPrincipal: String
)