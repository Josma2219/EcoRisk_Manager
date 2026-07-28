package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.MaterialProveedorDao
import com.example.ecorisk_manager.data.entity.MaterialProveedorEntity
import com.example.ecorisk_manager.model.MaterialProveedorDetalle
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio encargado de gestionar las relaciones
 * entre materiales peligrosos y proveedores.
 */
class MaterialProveedorRepository(
    private val materialProveedorDao: MaterialProveedorDao
) {

    /**
     * Obtiene el detalle de todas las relaciones registradas.
     */
    fun obtenerRelacionesDetalle(): Flow<List<MaterialProveedorDetalle>> {
        return materialProveedorDao.obtenerRelacionesDetalle()
    }

    /**
     * Obtiene las relaciones asociadas a un material.
     */
    fun obtenerRelacionesDetallePorMaterial(idMaterial: Int): Flow<List<MaterialProveedorDetalle>> {
        return materialProveedorDao.obtenerRelacionesDetallePorMaterial(idMaterial)
    }

    /**
     * Obtiene las relaciones asociadas a un proveedor.
     */
    fun obtenerRelacionesDetallePorProveedor(idProveedor: Int): Flow<List<MaterialProveedorDetalle>> {
        return materialProveedorDao.obtenerRelacionesDetallePorProveedor(idProveedor)
    }

    /**
     * Registra una nueva relación entre un material y un proveedor.
     */
    suspend fun insertarRelacion(relacion: MaterialProveedorEntity): Long {
        return materialProveedorDao.insertarRelacion(relacion)
    }

    /**
     * Verifica si ya existe una relación entre un material y un proveedor.
     */
    suspend fun existeRelacion(idMaterial: Int, idProveedor: Int): Boolean {
        return materialProveedorDao.contarRelacionExistente(
            idMaterial = idMaterial,
            idProveedor = idProveedor
        ) > 0
    }

    /**
     * Elimina una relación utilizando su identificador.
     */
    suspend fun eliminarRelacionPorId(idRelacion: Int) {
        materialProveedorDao.eliminarRelacionPorId(idRelacion)
    }
}