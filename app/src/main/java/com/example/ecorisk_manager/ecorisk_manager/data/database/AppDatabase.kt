package com.example.ecorisk_manager.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ecorisk_manager.data.dao.HojaSeguridadDao
import com.example.ecorisk_manager.data.dao.IncidenteDao
import com.example.ecorisk_manager.data.dao.MaterialPeligrosoDao
import com.example.ecorisk_manager.data.dao.MaterialProveedorDao
import com.example.ecorisk_manager.data.dao.ProveedorDao
import com.example.ecorisk_manager.data.dao.ReporteDao
import com.example.ecorisk_manager.data.dao.UsuarioDao
import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.entity.MaterialProveedorEntity
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.data.entity.UsuarioEntity

@Database(
    entities = [
        MaterialPeligrosoEntity::class,
        HojaSeguridadEntity::class,
        ProveedorEntity::class,
        MaterialProveedorEntity::class,
        IncidenteEntity::class,
        UsuarioEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun materialPeligrosoDao(): MaterialPeligrosoDao
    abstract fun hojaSeguridadDao(): HojaSeguridadDao
    abstract fun proveedorDao(): ProveedorDao
    abstract fun materialProveedorDao(): MaterialProveedorDao
    abstract fun incidenteDao(): IncidenteDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun reporteDao(): ReporteDao

    companion object {
        @Volatile
        private var INSTANCIA: AppDatabase? = null

        fun obtenerBaseDatos(contexto: Context): AppDatabase {
            return INSTANCIA ?: synchronized(this) {
                val nuevaInstancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    AppDatabase::class.java,
                    "base_datos_ecorisk"
                )
                    // Mientras estamos desarrollando, esto evita bloqueos si cambiamos tablas.
                    // Cuando la app esté lista, si cambiamos versión hacemos migraciones reales.
                    .fallbackToDestructiveMigration(false)
                    .build()

                INSTANCIA = nuevaInstancia
                nuevaInstancia
            }
        }
    }
}