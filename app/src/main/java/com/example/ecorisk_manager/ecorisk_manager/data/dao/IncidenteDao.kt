package com.example.ecorisk_manager.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncidenteDao {

    @Insert
    suspend fun insertarIncidente(incidente: IncidenteEntity): Long

    @Update
    suspend fun actualizarIncidente(incidente: IncidenteEntity)

    @Delete
    suspend fun eliminarIncidente(incidente: IncidenteEntity)

    @Query("SELECT * FROM incidentes ORDER BY fecha_incidente DESC")
    fun obtenerIncidentes(): Flow<List<IncidenteEntity>>

    @Query("SELECT * FROM incidentes WHERE id_incidente = :idIncidente LIMIT 1")
    suspend fun obtenerIncidentePorId(idIncidente: Int): IncidenteEntity?

    @Query("""
        SELECT * FROM incidentes
        WHERE id_material = :idMaterial
        ORDER BY fecha_incidente DESC
    """)
    fun obtenerIncidentesPorMaterial(idMaterial: Int): Flow<List<IncidenteEntity>>

    @Query("""
        SELECT * FROM incidentes
        WHERE estado = :estado
        ORDER BY fecha_incidente DESC
    """)
    fun obtenerIncidentesPorEstado(estado: String): Flow<List<IncidenteEntity>>

    @Query("""
        SELECT * FROM incidentes
        WHERE nivel_severidad = :nivelSeveridad
        ORDER BY fecha_incidente DESC
    """)
    fun obtenerIncidentesPorSeveridad(nivelSeveridad: String): Flow<List<IncidenteEntity>>

    @Query("SELECT COUNT(*) FROM incidentes WHERE estado = 'Abierto'")
    suspend fun contarIncidentesAbiertos(): Int

    @Query("SELECT COUNT(*) FROM incidentes WHERE estado = 'Cerrado'")
    suspend fun contarIncidentesCerrados(): Int
}