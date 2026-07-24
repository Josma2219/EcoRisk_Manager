package com.example.ecorisk_manager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hojas_seguridad",
    foreignKeys = [
        ForeignKey(
            entity = MaterialPeligrosoEntity::class,
            parentColumns = ["id_material"],
            childColumns = ["id_material"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_material"]),
        Index(value = ["id_material", "version"], unique = true)
    ]
)
data class HojaSeguridadEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_hoja")
    val idHoja: Int = 0,

    @ColumnInfo(name = "version")
    val version: String,

    @ColumnInfo(name = "fecha_emision")
    val fechaEmision: String,

    @ColumnInfo(name = "archivo_pdf")
    val archivoPdf: String,

    @ColumnInfo(name = "observaciones")
    val observaciones: String,

    @ColumnInfo(name = "id_material")
    val idMaterial: Int
)