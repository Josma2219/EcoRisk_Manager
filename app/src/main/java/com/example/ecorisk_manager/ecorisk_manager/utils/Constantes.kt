package com.example.ecorisk_manager.utils

object Constantes {

    object Sesion {
        const val NOMBRE_PREFERENCIAS = "preferencias_ecorisk"
        const val CLAVE_SESION_ACTIVA = "sesion_activa"
        const val CLAVE_NOMBRE_USUARIO = "nombre_usuario"
        const val CLAVE_ROL_USUARIO = "rol_usuario"
    }

    object Extras {
        const val EXTRA_ID_MATERIAL = "extra_id_material"
        const val EXTRA_ID_PROVEEDOR = "extra_id_proveedor"
        const val EXTRA_ID_INCIDENTE = "extra_id_incidente"
        const val EXTRA_ID_HOJA = "extra_id_hoja"
    }

    object UsuarioTemporal {
        const val USUARIO = "admin"
        const val CONTRASENA = "admin123"
        const val NOMBRE = "Administrador"
        const val ROL = "Administrador"
    }

    object Roles {
        const val ADMINISTRADOR = "Administrador"
        const val SUPERVISOR = "Supervisor"
        const val CONSULTA = "Consulta"
    }

    object Estados {
        const val ACTIVO = "Activo"
        const val INACTIVO = "Inactivo"
        const val ABIERTO = "Abierto"
        const val EN_PROCESO = "En proceso"
        const val CERRADO = "Cerrado"
    }

    object Mensajes {
        const val MODULO_EN_PROCESO = "Este módulo lo conectamos en una próxima etapa"
    }
}