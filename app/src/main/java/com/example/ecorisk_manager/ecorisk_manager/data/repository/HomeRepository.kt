package com.example.ecorisk_manager.data.repository

import com.example.ecorisk_manager.data.dao.IncidenteDao
import com.example.ecorisk_manager.data.dao.MaterialPeligrosoDao
import com.example.ecorisk_manager.data.dao.ProveedorDao
import com.example.ecorisk_manager.model.ResumenDashboard

/**
 * Repositorio encargado de obtener la información
 * mostrada en el panel principal de la aplicación.
 */
class HomeRepository(
    private val materialPeligrosoDao: MaterialPeligrosoDao,
    private val proveedorDao: ProveedorDao,
    private val incidenteDao: IncidenteDao
) {

    /**
     * Obtiene un resumen con los datos principales
     * utilizados en el panel de inicio.
     */
    suspend fun obtenerResumenDashboard(): ResumenDashboard {
        return ResumenDashboard(
            totalMateriales = materialPeligrosoDao.contarMateriales(),
            totalProveedores = proveedorDao.contarProveedores(),
            incidentesAbiertos = incidenteDao.contarIncidentesAbiertos()
        )
    }
}