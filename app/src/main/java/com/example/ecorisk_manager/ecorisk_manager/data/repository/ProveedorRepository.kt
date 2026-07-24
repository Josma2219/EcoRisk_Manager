package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.ProveedorDao
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import kotlinx.coroutines.flow.Flow

class ProveedorRepository(
    private val proveedorDao: ProveedorDao
) {

    fun obtenerProveedores(): Flow<List<ProveedorEntity>> {
        return proveedorDao.obtenerProveedores()
    }

    fun buscarProveedores(texto: String): Flow<List<ProveedorEntity>> {
        return proveedorDao.buscarProveedores(texto)
    }

    suspend fun obtenerProveedorPorId(idProveedor: Int): ProveedorEntity? {
        return proveedorDao.obtenerProveedorPorId(idProveedor)
    }

    suspend fun insertarProveedor(proveedor: ProveedorEntity): Long {
        return proveedorDao.insertarProveedor(proveedor)
    }

    suspend fun actualizarProveedor(proveedor: ProveedorEntity) {
        proveedorDao.actualizarProveedor(proveedor)
    }

    suspend fun existeCorreoProveedor(correo: String): Boolean {
        return proveedorDao.contarProveedorPorCorreo(correo) > 0
    }

    suspend fun existeCorreoEnOtroRegistro(correo: String, idProveedor: Int): Boolean {
        return proveedorDao.contarProveedorPorCorreoEnOtroRegistro(
            correo = correo,
            idProveedor = idProveedor
        ) > 0
    }
}