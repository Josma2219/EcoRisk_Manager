package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.ProveedorDao
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar las operaciones
 * relacionadas con los proveedores.
 */
class ProveedorRepository(
    private val proveedorDao: ProveedorDao
) {

    /**
     * Obtiene todos los proveedores registrados.
     */
    fun obtenerProveedores(): Flow<List<ProveedorEntity>> {
        return proveedorDao.obtenerProveedores()
    }

    /**
     * Busca proveedores por nombre, correo o contacto principal.
     */
    fun buscarProveedores(texto: String): Flow<List<ProveedorEntity>> {
        return proveedorDao.buscarProveedores(texto)
    }

    /**
     * Busca un proveedor utilizando su identificador.
     */
    suspend fun obtenerProveedorPorId(idProveedor: Int): ProveedorEntity? {
        return proveedorDao.obtenerProveedorPorId(idProveedor)
    }

    /**
     * Registra un nuevo proveedor.
     */
    suspend fun insertarProveedor(proveedor: ProveedorEntity): Long {
        return proveedorDao.insertarProveedor(proveedor)
    }

    /**
     * Actualiza la información de un proveedor existente.
     */
    suspend fun actualizarProveedor(proveedor: ProveedorEntity) {
        proveedorDao.actualizarProveedor(proveedor)
    }

    /**
     * Verifica si ya existe un proveedor con el mismo correo.
     */
    suspend fun existeCorreoProveedor(correo: String): Boolean {
        return proveedorDao.contarProveedorPorCorreo(correo) > 0
    }

    /**
     * Verifica si otro proveedor ya utiliza el mismo correo.
     */
    suspend fun existeCorreoEnOtroRegistro(correo: String, idProveedor: Int): Boolean {
        return proveedorDao.contarProveedorPorCorreoEnOtroRegistro(
            correo = correo,
            idProveedor = idProveedor
        ) > 0
    }
}