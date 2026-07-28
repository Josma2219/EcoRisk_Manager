package com.example.ecorisk_manager.data.repository

import android.content.Context
import androidx.room.withTransaction
import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.model.RespaldoDatos
import com.example.ecorisk_manager.model.ResultadoOperacion
import com.google.gson.GsonBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Repositorio encargado de generar y restaurar
 * los respaldos de la base de datos de la aplicación.
 */
class RespaldoRepository(
    private val baseDatos: AppDatabase
) {

    // Instancia utilizada para convertir la información hacia y desde formato JSON.
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    /**
     * Genera un respaldo con toda la información almacenada
     * y lo guarda como un archivo JSON.
     */
    suspend fun generarRespaldo(contexto: Context): ResultadoOperacion {
        return try {
            val fechaVisible = SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
            ).format(Date())

            val fechaArchivo = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
            ).format(Date())

            // Reúne toda la información que será incluida en el respaldo.
            val datos = RespaldoDatos(
                fechaGeneracion = fechaVisible,
                usuarios = baseDatos.usuarioDao().obtenerUsuariosParaRespaldo(),
                materiales = baseDatos.materialPeligrosoDao().obtenerMaterialesParaRespaldo(),
                proveedores = baseDatos.proveedorDao().obtenerProveedoresParaRespaldo(),
                hojasSeguridad = baseDatos.hojaSeguridadDao().obtenerHojasParaRespaldo(),
                materialesProveedores = baseDatos.materialProveedorDao().obtenerRelacionesParaRespaldo(),
                incidentes = baseDatos.incidenteDao().obtenerIncidentesParaRespaldo()
            )

            val carpetaRespaldos = obtenerCarpetaRespaldos(contexto)
            val archivo = File(carpetaRespaldos, "respaldo_ecorisk_$fechaArchivo.json")

            val json = gson.toJson(datos)
            archivo.writeText(json)

            ResultadoOperacion(
                exitoso = true,
                mensaje = "Respaldo generado correctamente:\n${archivo.absolutePath}"
            )
        } catch (error: Exception) {
            ResultadoOperacion(
                exitoso = false,
                mensaje = "No se pudo generar el respaldo"
            )
        }
    }

    /**
     * Restaura la información utilizando el respaldo más reciente disponible.
     */
    suspend fun restaurarUltimoRespaldo(contexto: Context): ResultadoOperacion {
        return try {
            val carpetaRespaldos = obtenerCarpetaRespaldos(contexto)

            // Busca el archivo de respaldo más reciente.
            val ultimoRespaldo = carpetaRespaldos
                .listFiles { archivo ->
                    archivo.isFile && archivo.name.endsWith(".json")
                }
                ?.maxByOrNull { archivo ->
                    archivo.lastModified()
                }

            if (ultimoRespaldo == null) {
                return ResultadoOperacion(
                    exitoso = false,
                    mensaje = "No hay respaldos disponibles para restaurar"
                )
            }

            val json = ultimoRespaldo.readText()
            val datos = gson.fromJson(json, RespaldoDatos::class.java)

            // La restauración se realiza dentro de una transacción para mantener la integridad de los datos.
            baseDatos.withTransaction {

                // Primero se eliminan las tablas dependientes para evitar conflictos de llaves foráneas.
                baseDatos.incidenteDao().eliminarTodosIncidentes()
                baseDatos.hojaSeguridadDao().eliminarTodasHojas()
                baseDatos.materialProveedorDao().eliminarTodasRelaciones()

                // Luego se eliminan las tablas principales.
                baseDatos.materialPeligrosoDao().eliminarTodosMateriales()
                baseDatos.proveedorDao().eliminarTodosProveedores()
                baseDatos.usuarioDao().eliminarTodosUsuarios()

                // Se restauran primero las tablas principales.
                baseDatos.usuarioDao().insertarUsuarios(datos.usuarios)
                baseDatos.materialPeligrosoDao().insertarMateriales(datos.materiales)
                baseDatos.proveedorDao().insertarProveedores(datos.proveedores)

                // Finalmente, se restauran las tablas que dependen de las anteriores.
                baseDatos.hojaSeguridadDao().insertarHojas(datos.hojasSeguridad)
                baseDatos.materialProveedorDao().insertarRelaciones(datos.materialesProveedores)
                baseDatos.incidenteDao().insertarIncidentes(datos.incidentes)
            }

            ResultadoOperacion(
                exitoso = true,
                mensaje = "Respaldo restaurado correctamente:\n${ultimoRespaldo.name}"
            )
        } catch (error: Exception) {
            ResultadoOperacion(
                exitoso = false,
                mensaje = "No se pudo restaurar el respaldo"
            )
        }
    }

    /**
     * Obtiene la carpeta donde se almacenan los archivos de respaldo.
     * Si no existe, la crea automáticamente.
     */
    private fun obtenerCarpetaRespaldos(contexto: Context): File {
        val carpeta = File(contexto.getExternalFilesDir(null), "respaldos")

        if (!carpeta.exists()) {
            carpeta.mkdirs()
        }

        return carpeta
    }
}