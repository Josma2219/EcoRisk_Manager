package com.example.ecorisk_manager.utils

/**
 * Contiene las constantes utilizadas en toda la aplicación.
 *
 * Centralizar estos valores evita duplicación de cadenas de texto,
 * facilita el mantenimiento del código y reduce errores al utilizar
 * claves repetidas entre actividades, sesiones y módulos.
 */
object Constantes {

    /**
     * Constantes utilizadas para almacenar y recuperar
     * información de la sesión mediante SharedPreferences.
     */
    object Sesion {

        /** Nombre del archivo de preferencias. */
        const val NOMBRE_PREFERENCIAS = "preferencias_ecorisk"

        /** Indica si existe una sesión iniciada. */
        const val CLAVE_SESION_ACTIVA = "sesion_activa"

        /** Nombre del usuario autenticado. */
        const val CLAVE_NOMBRE_USUARIO = "nombre_usuario"

        /** Rol del usuario autenticado. */
        const val CLAVE_ROL_USUARIO = "rol_usuario"
    }

    /**
     * Claves utilizadas para enviar información
     * entre Activities mediante Intent.
     */
    object Extras {

        /** Identificador de un material peligroso. */
        const val EXTRA_ID_MATERIAL = "extra_id_material"

        /** Identificador de un proveedor. */
        const val EXTRA_ID_PROVEEDOR = "extra_id_proveedor"

        /** Identificador de un incidente. */
        const val EXTRA_ID_INCIDENTE = "extra_id_incidente"

        /** Identificador de una hoja de seguridad. */
        const val EXTRA_ID_HOJA = "extra_id_hoja"
    }

    /**
     * Datos del usuario administrador que se crea
     * automáticamente durante la primera ejecución
     * de la aplicación.
     */
    object UsuarioTemporal {

        /** Nombre de usuario predeterminado. */
        const val USUARIO = "admin"

        /** Contraseña inicial del administrador. */
        const val CONTRASENA = "admin123"

        /** Nombre mostrado del administrador. */
        const val NOMBRE = "Administrador"

        /** Rol asignado al administrador. */
        const val ROL = "Administrador"
    }

    /**
     * Roles disponibles para los usuarios
     * del sistema.
     */
    object Roles {

        /** Rol con acceso completo al sistema. */
        const val ADMINISTRADOR = "Administrador"

        /** Rol con permisos de supervisión. */
        const val SUPERVISOR = "Supervisor"

        /** Rol únicamente para consultas. */
        const val CONSULTA = "Consulta"
    }

    /**
     * Estados utilizados por distintos
     * módulos de la aplicación.
     */
    object Estados {

        /** Estado activo. */
        const val ACTIVO = "Activo"

        /** Estado inactivo. */
        const val INACTIVO = "Inactivo"

        /** Estado de incidente abierto. */
        const val ABIERTO = "Abierto"

        /** Estado de incidente en proceso. */
        const val EN_PROCESO = "En proceso"

        /** Estado de incidente cerrado. */
        const val CERRADO = "Cerrado"
    }

    /**
     * Mensajes reutilizados en diferentes
     * pantallas de la aplicación.
     */
    object Mensajes {

        /** Mensaje utilizado para funcionalidades aún no implementadas. */
        const val MODULO_EN_PROCESO = "Este módulo lo conectamos en una próxima etapa"
    }
}