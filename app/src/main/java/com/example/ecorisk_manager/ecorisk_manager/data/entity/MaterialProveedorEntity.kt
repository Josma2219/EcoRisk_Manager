package com.example.ecorisk_manager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "materiales_proveedores",
    foreignKeys = [
        ForeignKey(
            entity = MaterialPeligrosoEntity::class,
            parentColumns = ["id_material"],
            childColumns = ["id_material"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProveedorEntity::class,
            parentColumns = ["id_proveedor"],
            childColumns = ["id_proveedor"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_material"]),
        Index(value = ["id_proveedor"]),
        Index(value = ["id_material", "id_proveedor"], unique = true)
    ]
)
data class MaterialProveedorEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_material_proveedor")
    val idMaterialProveedor: Int = 0,

    @ColumnInfo(name = "id_material")
    val idMaterial: Int,

    @ColumnInfo(name = "id_proveedor")
    val idProveedor: Int,

    @ColumnInfo(name = "precio_referencia")
    val precioReferencia: Double
)