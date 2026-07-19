package com.example.ecorisk_manager.utils

import com.example.ecorisk_manager.data.database.AppDatabase
import com.example.ecorisk_manager.data.entity.HojaSeguridadEntity
import com.example.ecorisk_manager.data.entity.IncidenteEntity
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.data.entity.MaterialProveedorEntity
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.data.entity.UsuarioEntity

object PobladorDatosPrueba {

    suspend fun reiniciarYPoblarBaseDatos(baseDatos: AppDatabase) {
        limpiarBaseDatos(baseDatos)

        insertarUsuarios(baseDatos)

        val materiales = insertarMateriales(baseDatos)
        val proveedores = insertarProveedores(baseDatos)

        insertarHojasSeguridad(baseDatos, materiales)
        insertarMaterialesProveedores(baseDatos, materiales, proveedores)
        insertarIncidentes(baseDatos, materiales)
    }

    private suspend fun limpiarBaseDatos(baseDatos: AppDatabase) {
        // Primero tablas dependientes para no chocar con llaves foráneas.
        baseDatos.incidenteDao().eliminarTodosIncidentes()
        baseDatos.hojaSeguridadDao().eliminarTodasHojas()
        baseDatos.materialProveedorDao().eliminarTodasRelaciones()

        // Luego tablas principales.
        baseDatos.materialPeligrosoDao().eliminarTodosMateriales()
        baseDatos.proveedorDao().eliminarTodosProveedores()
        baseDatos.usuarioDao().eliminarTodosUsuarios()
    }

    private suspend fun insertarUsuarios(baseDatos: AppDatabase) {
        val usuarios = listOf(
            UsuarioEntity(
                nombre = "Administrador General",
                usuario = "admin",
                contrasena = "admin123",
                rol = "Administrador",
                estado = "Activo"
            ),
            UsuarioEntity(
                nombre = "Supervisor de Seguridad",
                usuario = "supervisor",
                contrasena = "super123",
                rol = "Supervisor",
                estado = "Activo"
            ),
            UsuarioEntity(
                nombre = "Usuario de Consulta",
                usuario = "consulta",
                contrasena = "consulta123",
                rol = "Consulta",
                estado = "Activo"
            )
        )

        usuarios.forEach { usuario ->
            baseDatos.usuarioDao().insertarUsuario(usuario)
        }
    }

    private suspend fun insertarMateriales(
        baseDatos: AppDatabase
    ): Map<String, Int> {
        val materiales = listOf(
            MaterialPeligrosoEntity(
                codigoMaterial = "MAT-001",
                nombreComercial = "Ácido Sulfúrico",
                descripcion = "Sustancia corrosiva utilizada en procesos industriales y de limpieza especializada.",
                clasificacionRiesgo = "Corrosivo",
                unidadMedida = "Litros",
                fechaRegistro = "2026-07-01",
                estado = "Activo"
            ),
            MaterialPeligrosoEntity(
                codigoMaterial = "MAT-002",
                nombreComercial = "Acetona Industrial",
                descripcion = "Solvente inflamable utilizado para limpieza de piezas y procesos de laboratorio.",
                clasificacionRiesgo = "Inflamable",
                unidadMedida = "Litros",
                fechaRegistro = "2026-07-02",
                estado = "Activo"
            ),
            MaterialPeligrosoEntity(
                codigoMaterial = "MAT-003",
                nombreComercial = "Cloro Líquido",
                descripcion = "Producto químico tóxico utilizado para procesos de desinfección y tratamiento.",
                clasificacionRiesgo = "Tóxico",
                unidadMedida = "Galones",
                fechaRegistro = "2026-07-03",
                estado = "Activo"
            ),
            MaterialPeligrosoEntity(
                codigoMaterial = "MAT-004",
                nombreComercial = "Peróxido de Hidrógeno",
                descripcion = "Sustancia comburente utilizada en procesos de limpieza y tratamiento químico.",
                clasificacionRiesgo = "Comburente",
                unidadMedida = "Litros",
                fechaRegistro = "2026-07-04",
                estado = "Activo"
            ),
            MaterialPeligrosoEntity(
                codigoMaterial = "MAT-005",
                nombreComercial = "Soda Cáustica",
                descripcion = "Material corrosivo usado en limpieza industrial y procesos de neutralización.",
                clasificacionRiesgo = "Corrosivo",
                unidadMedida = "Kilogramos",
                fechaRegistro = "2026-07-05",
                estado = "Activo"
            ),
            MaterialPeligrosoEntity(
                codigoMaterial = "MAT-006",
                nombreComercial = "Amoníaco Industrial",
                descripcion = "Sustancia tóxica e irritante utilizada en procesos de refrigeración y limpieza.",
                clasificacionRiesgo = "Irritante",
                unidadMedida = "Litros",
                fechaRegistro = "2026-07-06",
                estado = "Activo"
            ),
            MaterialPeligrosoEntity(
                codigoMaterial = "MAT-007",
                nombreComercial = "Gasolina de Prueba",
                descripcion = "Combustible altamente inflamable almacenado para pruebas controladas internas.",
                clasificacionRiesgo = "Inflamable",
                unidadMedida = "Galones",
                fechaRegistro = "2026-07-07",
                estado = "Activo"
            ),
            MaterialPeligrosoEntity(
                codigoMaterial = "MAT-008",
                nombreComercial = "Resina Reactiva",
                descripcion = "Compuesto reactivo utilizado en procesos de fabricación y recubrimiento.",
                clasificacionRiesgo = "Reactivo",
                unidadMedida = "Kilogramos",
                fechaRegistro = "2026-07-08",
                estado = "Activo"
            )
        )

        val ids = mutableMapOf<String, Int>()

        materiales.forEach { material ->
            val idGenerado = baseDatos.materialPeligrosoDao()
                .insertarMaterial(material)
                .toInt()

            ids[material.codigoMaterial] = idGenerado
        }

        return ids
    }

    private suspend fun insertarProveedores(
        baseDatos: AppDatabase
    ): Map<String, Int> {
        val proveedores = listOf(
            ProveedorEntity(
                nombre = "Químicos del Norte",
                telefono = "2222-1111",
                correo = "ventas@quimicosnorte.com",
                direccion = "San José, Costa Rica",
                contactoPrincipal = "Carlos Vargas Mora"
            ),
            ProveedorEntity(
                nombre = "Suministros Industriales CR",
                telefono = "8888-7777",
                correo = "contacto@sicr.com",
                direccion = "Alajuela, Costa Rica",
                contactoPrincipal = "María López"
            ),
            ProveedorEntity(
                nombre = "EcoQuímica Global",
                telefono = "2444-9090",
                correo = "info@ecoquimicaglobal.com",
                direccion = "Heredia, Costa Rica",
                contactoPrincipal = "Andrés Solano"
            ),
            ProveedorEntity(
                nombre = "TecnoSafety Supplies",
                telefono = "2290-3030",
                correo = "ventas@tecnosafety.com",
                direccion = "Cartago, Costa Rica",
                contactoPrincipal = "Daniela Rojas"
            ),
            ProveedorEntity(
                nombre = "Distribuidora Industrial Pacífico",
                telefono = "2661-5555",
                correo = "pedidos@dipsa.com",
                direccion = "Puntarenas, Costa Rica",
                contactoPrincipal = "Luis Fernández"
            )
        )

        val ids = mutableMapOf<String, Int>()

        proveedores.forEach { proveedor ->
            val idGenerado = baseDatos.proveedorDao()
                .insertarProveedor(proveedor)
                .toInt()

            ids[proveedor.nombre] = idGenerado
        }

        return ids
    }

    private suspend fun insertarHojasSeguridad(
        baseDatos: AppDatabase,
        materiales: Map<String, Int>
    ) {
        val hojas = listOf(
            HojaSeguridadEntity(
                version = "1.0",
                fechaEmision = "2026-07-01",
                archivoPdf = "hoja_acido_sulfurico_v1.pdf",
                observaciones = "Hoja inicial para control del material corrosivo.",
                idMaterial = materiales["MAT-001"] ?: 0
            ),
            HojaSeguridadEntity(
                version = "1.1",
                fechaEmision = "2026-07-10",
                archivoPdf = "hoja_acido_sulfurico_v1_1.pdf",
                observaciones = "Actualización de medidas de almacenamiento.",
                idMaterial = materiales["MAT-001"] ?: 0
            ),
            HojaSeguridadEntity(
                version = "1.0",
                fechaEmision = "2026-07-02",
                archivoPdf = "hoja_acetona_industrial_v1.pdf",
                observaciones = "Documento base para producto inflamable.",
                idMaterial = materiales["MAT-002"] ?: 0
            ),
            HojaSeguridadEntity(
                version = "1.0",
                fechaEmision = "2026-07-03",
                archivoPdf = "hoja_cloro_liquido_v1.pdf",
                observaciones = "Incluye controles básicos de manipulación segura.",
                idMaterial = materiales["MAT-003"] ?: 0
            ),
            HojaSeguridadEntity(
                version = "2.0",
                fechaEmision = "2026-07-12",
                archivoPdf = "hoja_peroxido_hidrogeno_v2.pdf",
                observaciones = "Versión actualizada por cambio de proveedor.",
                idMaterial = materiales["MAT-004"] ?: 0
            ),
            HojaSeguridadEntity(
                version = "1.0",
                fechaEmision = "2026-07-05",
                archivoPdf = "hoja_soda_caustica_v1.pdf",
                observaciones = "Hoja agregada para material corrosivo sólido.",
                idMaterial = materiales["MAT-005"] ?: 0
            )
        )

        hojas.forEach { hoja ->
            baseDatos.hojaSeguridadDao().insertarHoja(hoja)
        }
    }

    private suspend fun insertarMaterialesProveedores(
        baseDatos: AppDatabase,
        materiales: Map<String, Int>,
        proveedores: Map<String, Int>
    ) {
        val relaciones = listOf(
            MaterialProveedorEntity(
                idMaterial = materiales["MAT-001"] ?: 0,
                idProveedor = proveedores["Químicos del Norte"] ?: 0,
                precioReferencia = 18500.0
            ),
            MaterialProveedorEntity(
                idMaterial = materiales["MAT-001"] ?: 0,
                idProveedor = proveedores["EcoQuímica Global"] ?: 0,
                precioReferencia = 17950.0
            ),
            MaterialProveedorEntity(
                idMaterial = materiales["MAT-002"] ?: 0,
                idProveedor = proveedores["Suministros Industriales CR"] ?: 0,
                precioReferencia = 12200.0
            ),
            MaterialProveedorEntity(
                idMaterial = materiales["MAT-003"] ?: 0,
                idProveedor = proveedores["TecnoSafety Supplies"] ?: 0,
                precioReferencia = 21400.0
            ),
            MaterialProveedorEntity(
                idMaterial = materiales["MAT-004"] ?: 0,
                idProveedor = proveedores["EcoQuímica Global"] ?: 0,
                precioReferencia = 16800.0
            ),
            MaterialProveedorEntity(
                idMaterial = materiales["MAT-005"] ?: 0,
                idProveedor = proveedores["Distribuidora Industrial Pacífico"] ?: 0,
                precioReferencia = 9900.0
            ),
            MaterialProveedorEntity(
                idMaterial = materiales["MAT-007"] ?: 0,
                idProveedor = proveedores["Suministros Industriales CR"] ?: 0,
                precioReferencia = 15300.0
            )
        )

        relaciones.forEach { relacion ->
            if (relacion.idMaterial != 0 && relacion.idProveedor != 0) {
                baseDatos.materialProveedorDao().insertarRelacion(relacion)
            }
        }
    }

    private suspend fun insertarIncidentes(
        baseDatos: AppDatabase,
        materiales: Map<String, Int>
    ) {
        val incidentes = listOf(
            IncidenteEntity(
                fechaIncidente = "2026-07-09",
                tipoIncidente = "Derrame",
                descripcion = "Derrame menor detectado durante traslado interno del material.",
                nivelSeveridad = "Medio",
                accionesCorrectivas = "Se aisló el área, se limpió el derrame y se registró seguimiento.",
                estado = "Cerrado",
                idMaterial = materiales["MAT-001"] ?: 0
            ),
            IncidenteEntity(
                fechaIncidente = "2026-07-11",
                tipoIncidente = "Almacenamiento incorrecto",
                descripcion = "Se detectó un contenedor ubicado fuera de la zona asignada.",
                nivelSeveridad = "Bajo",
                accionesCorrectivas = "Se reubicó el material y se reforzó la rotulación.",
                estado = "Cerrado",
                idMaterial = materiales["MAT-002"] ?: 0
            ),
            IncidenteEntity(
                fechaIncidente = "2026-07-13",
                tipoIncidente = "Fuga",
                descripcion = "Fuga leve reportada en área de almacenamiento temporal.",
                nivelSeveridad = "Alto",
                accionesCorrectivas = "Se notificó al supervisor y se mantiene el área restringida.",
                estado = "En proceso",
                idMaterial = materiales["MAT-003"] ?: 0
            ),
            IncidenteEntity(
                fechaIncidente = "2026-07-15",
                tipoIncidente = "Exposición",
                descripcion = "Trabajador reportó irritación menor durante manipulación del material.",
                nivelSeveridad = "Medio",
                accionesCorrectivas = "Se revisó el equipo de protección y se actualizó la bitácora.",
                estado = "Abierto",
                idMaterial = materiales["MAT-006"] ?: 0
            ),
            IncidenteEntity(
                fechaIncidente = "2026-07-16",
                tipoIncidente = "Reacción química",
                descripcion = "Se reportó reacción inesperada durante revisión de compatibilidad.",
                nivelSeveridad = "Crítico",
                accionesCorrectivas = "Se suspendió el uso del lote y se abrió investigación interna.",
                estado = "Abierto",
                idMaterial = materiales["MAT-008"] ?: 0
            ),
            IncidenteEntity(
                fechaIncidente = "2026-07-17",
                tipoIncidente = "Incendio",
                descripcion = "Conato controlado en zona de pruebas relacionado con material inflamable.",
                nivelSeveridad = "Alto",
                accionesCorrectivas = "Se revisaron protocolos y se documentó el incidente.",
                estado = "En proceso",
                idMaterial = materiales["MAT-007"] ?: 0
            )
        )

        incidentes.forEach { incidente ->
            if (incidente.idMaterial != 0) {
                baseDatos.incidenteDao().insertarIncidente(incidente)
            }
        }
    }
}