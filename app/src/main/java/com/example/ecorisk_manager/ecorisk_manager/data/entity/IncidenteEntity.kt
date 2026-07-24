package com.example.ecorisk_manager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "incidentes",
    foreignKeys = [
        ForeignKey(
            entity = MaterialPeligrosoEntity::class,
            parentColumns = ["id_material"],
            childColumns = ["id_material"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_material"])
    ]
)
data class IncidenteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_incidente")
    val idIncidente: Int = 0,

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
    val idMaterial: Int
)