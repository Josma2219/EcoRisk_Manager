package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.MaterialProveedorDao
import com.example.ecorisk_manager.data.entity.MaterialProveedorEntity
import com.example.ecorisk_manager.model.MaterialProveedorDetalle
import kotlinx.coroutines.flow.Flow

class MaterialProveedorRepository(
    private val materialProveedorDao: MaterialProveedorDao
) {

    fun obtenerRelacionesDetalle(): Flow<List<MaterialProveedorDetalle>> {
        return materialProveedorDao.obtenerRelacionesDetalle()
    }

    fun obtenerRelacionesDetallePorMaterial(idMaterial: Int): Flow<List<MaterialProveedorDetalle>> {
        return materialProveedorDao.obtenerRelacionesDetallePorMaterial(idMaterial)
    }

    fun obtenerRelacionesDetallePorProveedor(idProveedor: Int): Flow<List<MaterialProveedorDetalle>> {
        return materialProveedorDao.obtenerRelacionesDetallePorProveedor(idProveedor)
    }

    suspend fun insertarRelacion(relacion: MaterialProveedorEntity): Long {
        return materialProveedorDao.insertarRelacion(relacion)
    }

    suspend fun existeRelacion(idMaterial: Int, idProveedor: Int): Boolean {
        return materialProveedorDao.contarRelacionExistente(
            idMaterial = idMaterial,
            idProveedor = idProveedor
        ) > 0
    }

    suspend fun eliminarRelacionPorId(idRelacion: Int) {
        materialProveedorDao.eliminarRelacionPorId(idRelacion)
    }
}