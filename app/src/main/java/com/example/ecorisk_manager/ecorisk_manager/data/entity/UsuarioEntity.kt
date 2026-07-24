package com.example.ecorisk_manager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios",
    indices = [
        Index(value = ["usuario"], unique = true)
    ]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int = 0,

    @ColumnInfo(name = "nombre")
    val nombre: String,

    @ColumnInfo(name = "usuario")
    val usuario: String,

    @ColumnInfo(name = "contrasena")
    val contrasena: String,

    @ColumnInfo(name = "rol")
    val rol: String,

    @ColumnInfo(name = "estado")
    val estado: String
)