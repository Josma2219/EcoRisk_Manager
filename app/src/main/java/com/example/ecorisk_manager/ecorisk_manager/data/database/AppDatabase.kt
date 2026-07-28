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

/**
 * Base de datos principal de la aplicación.
 * Contiene las entidades registradas y proporciona acceso a los distintos DAO.
 */
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

    /**
     * Proporciona acceso al DAO de materiales peligrosos.
     */
    abstract fun materialPeligrosoDao(): MaterialPeligrosoDao

    /**
     * Proporciona acceso al DAO de hojas de seguridad.
     */
    abstract fun hojaSeguridadDao(): HojaSeguridadDao

    /**
     * Proporciona acceso al DAO de proveedores.
     */
    abstract fun proveedorDao(): ProveedorDao

    /**
     * Proporciona acceso al DAO de relaciones entre materiales y proveedores.
     */
    abstract fun materialProveedorDao(): MaterialProveedorDao

    /**
     * Proporciona acceso al DAO de incidentes.
     */
    abstract fun incidenteDao(): IncidenteDao

    /**
     * Proporciona acceso al DAO de usuarios.
     */
    abstract fun usuarioDao(): UsuarioDao

    /**
     * Proporciona acceso al DAO utilizado para generar reportes.
     */
    abstract fun reporteDao(): ReporteDao

    companion object {

        @Volatile
        private var INSTANCIA: AppDatabase? = null

        /**
         * Obtiene la instancia de la base de datos.
         * Si aún no existe, la crea y la reutiliza durante la ejecución de la aplicación.
         */
        fun obtenerBaseDatos(contexto: Context): AppDatabase {
            return INSTANCIA ?: synchronized(this) {
                val nuevaInstancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    AppDatabase::class.java,
                    "base_datos_ecorisk"
                )
                    // Recrea la base de datos si cambia la versión y no existen migraciones definidas.
                    .fallbackToDestructiveMigration(false)
                    .build()

                INSTANCIA = nuevaInstancia
                nuevaInstancia
            }
        }
    }
}