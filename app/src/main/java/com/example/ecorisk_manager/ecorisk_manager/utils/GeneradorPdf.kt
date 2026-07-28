package com.example.ecorisk_manager.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.model.IncidenteDetalle
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utilidad encargada de generar los reportes PDF del sistema.
 *
 * Centraliza la creación de documentos PDF para los distintos
 * módulos de reportes, incluyendo el historial de incidentes y
 * el listado de materiales clasificados por riesgo.
 */
object GeneradorPdf {

    // Dimensiones de una página A4 en puntos PDF y margen utilizado
    // para mantener una presentación uniforme en todos los reportes.
    private const val ANCHO_PAGINA = 595
    private const val ALTO_PAGINA = 842
    private const val MARGEN = 40

    /**
     * Genera un reporte PDF con los materiales peligrosos que cumplen
     * el filtro de clasificación de riesgo indicado.
     *
     * Cada material se presenta junto con su información principal
     * para facilitar su consulta o impresión.
     */
    fun generarReporteMaterialesPorRiesgo(
        contexto: Context,
        filtroAplicado: String,
        materiales: List<MaterialPeligrosoEntity>
        // Crear el documento PDF y el escritor encargado de administrar
        // automáticamente las páginas y el contenido.
    ): File {
        val documento = PdfDocument()
        val escritor = EscritorPdf(documento)

        val titulo = "Reporte de materiales por riesgo"

        // Escribir el encabezado con la información general del reporte.
        escritor.dibujarEncabezado(
            titulo = titulo,
            filtroAplicado = filtroAplicado,
            total = materiales.size
        )

        // Agregar cada material al documento respetando el formato definido.
        materiales.forEachIndexed { indice, material ->
            escritor.separadorSuave()

            escritor.linea(
                texto = "${indice + 1}. ${material.codigoMaterial} - ${material.nombreComercial}",
                paint = pinturas.subtitulo,
                espacioDespues = 20
            )

            escritor.linea("Riesgo: ${material.clasificacionRiesgo}", pinturas.texto)
            escritor.linea("Estado: ${material.estado}", pinturas.texto)
            escritor.linea("Unidad de medida: ${material.unidadMedida}", pinturas.texto)

            escritor.parrafo(
                texto = "Descripción: ${material.descripcion}",
                paint = pinturas.texto,
                espacioDespues = 10
            )
        }

        // Finalizar el documento y almacenarlo dentro de la carpeta de reportes.
        return guardarDocumento(
            contexto = contexto,
            documento = documento,
            escritor = escritor,
            nombreBase = "reporte_materiales_riesgo"
        )
    }

    /**
     * Genera un reporte PDF con el historial de incidentes registrados.
     *
     * El reporte puede representar el historial completo o un conjunto
     * filtrado por estado o nivel de severidad.
     */
    fun generarReporteHistorialIncidentes(
        contexto: Context,
        filtroAplicado: String,
        incidentes: List<IncidenteDetalle>
    ): File {
        val documento = PdfDocument()
        val escritor = EscritorPdf(documento)

        val titulo = "Reporte de historial de incidentes"

        escritor.dibujarEncabezado(
            titulo = titulo,
            filtroAplicado = filtroAplicado,
            total = incidentes.size
        )

        // Recorrer todos los incidentes e incorporarlos al reporte.
        incidentes.forEachIndexed { indice, incidente ->
            escritor.separadorSuave()

            escritor.linea(
                texto = "${indice + 1}. ${incidente.tipoIncidente}",
                paint = pinturas.subtitulo,
                espacioDespues = 20
            )

            escritor.linea("Material: ${incidente.codigoMaterial} - ${incidente.nombreMaterial}", pinturas.texto)
            escritor.linea("Fecha: ${incidente.fechaIncidente}", pinturas.texto)
            escritor.linea("Severidad: ${incidente.nivelSeveridad}", pinturas.texto)
            escritor.linea("Estado: ${incidente.estado}", pinturas.texto)

            escritor.parrafo(
                texto = "Descripción: ${incidente.descripcion}",
                paint = pinturas.texto,
                espacioDespues = 4
            )

            // Mostrar un mensaje alternativo cuando el incidente todavía
            // no posee acciones correctivas registradas.
            val acciones = if (incidente.accionesCorrectivas.isBlank()) {
                "Acciones correctivas: pendiente de registrar"
            } else {
                "Acciones correctivas: ${incidente.accionesCorrectivas}"
            }

            escritor.parrafo(
                texto = acciones,
                paint = pinturas.texto,
                espacioDespues = 10
            )
        }

        /**
         * Finaliza el documento PDF, lo guarda en almacenamiento interno
         * de la aplicación y devuelve el archivo generado.
         */
        return guardarDocumento(
            contexto = contexto,
            documento = documento,
            escritor = escritor,
            nombreBase = "reporte_historial_incidentes"
        )
    }

    private fun guardarDocumento(
        contexto: Context,
        documento: PdfDocument,
        escritor: EscritorPdf,
        nombreBase: String
    ): File {
        // Cerrar correctamente la última página antes de escribir el archivo.
        escritor.cerrar()

        val carpetaReportes = obtenerCarpetaReportes(contexto)
        val nombreArchivo = "${nombreBase}_${fechaParaArchivo()}.pdf"
        val archivo = File(carpetaReportes, nombreArchivo)

        // Escribir físicamente el contenido del documento en el archivo PDF.
        FileOutputStream(archivo).use { salida ->
            documento.writeTo(salida)
        }

        documento.close()

        return archivo
    }

    /**
     * Obtiene la carpeta donde se almacenarán los reportes PDF.
     *
     * Si la carpeta aún no existe, se crea automáticamente.
     */
    private fun obtenerCarpetaReportes(contexto: Context): File {
        val carpetaBase = contexto.getExternalFilesDir(null) ?: contexto.filesDir
        val carpetaReportes = File(carpetaBase, "reportes")

        if (!carpetaReportes.exists()) {
            carpetaReportes.mkdirs()
        }

        return carpetaReportes
    }

    private fun fechaVisible(): String {
        return SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss",
            Locale.getDefault()
        ).format(Date())
    }

    private fun fechaParaArchivo(): String {
        return SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())
    }

    /**
     * Conjunto de estilos gráficos utilizados durante la creación
     * de los reportes PDF.
     */
    private object pinturas {
        val marca = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(0, 36, 80)
            textSize = 22f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val titulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(13, 82, 137)
            textSize = 18f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val subtitulo = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(30, 41, 59)
            textSize = 13f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val texto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(30, 41, 59)
            textSize = 11f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val textoSecundario = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(100, 116, 139)
            textSize = 10f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val linea = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(210, 220, 230)
            strokeWidth = 1f
        }

        val piePagina = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.rgb(100, 116, 139)
            textSize = 9f
        }
    }

    /**
     * Clase auxiliar encargada de escribir el contenido dentro del PDF.
     *
     * Gestiona automáticamente la creación de páginas, el control
     * del espacio disponible y el formato utilizado en el documento.
     */
    private class EscritorPdf(
        private val documento: PdfDocument
    ) {
        private var numeroPagina = 0
        private var paginaActual: PdfDocument.Page? = null
        private var canvasActual: Canvas? = null
        private var yActual = MARGEN

        // Crear la primera página del documento al inicializar el escritor.
        init {
            nuevaPagina()
        }

        /**
         * Dibuja el encabezado principal del reporte con el título,
         * fecha de generación, filtro aplicado y cantidad de registros.
         */
        fun dibujarEncabezado(
            titulo: String,
            filtroAplicado: String,
            total: Int
        ) {
            linea("EcoRisk Manager", pinturas.marca, 28)
            linea(titulo, pinturas.titulo, 24)
            linea("Fecha de generación: ${fechaVisible()}", pinturas.textoSecundario, 16)
            linea("Filtro aplicado: $filtroAplicado", pinturas.textoSecundario, 16)
            linea("Total de registros: $total", pinturas.textoSecundario, 18)
            separadorFuerte()
        }

        // Escribe una única línea de texto respetando el espacio disponible.
        fun linea(
            texto: String,
            paint: Paint,
            espacioDespues: Int = 16
        ) {
            asegurarEspacio(espacioDespues + 8)
            canvasActual?.drawText(texto, MARGEN.toFloat(), yActual.toFloat(), paint)
            yActual += espacioDespues
        }

        // Divide automáticamente el texto en varias líneas cuando supera
        // el ancho disponible de la página.
        fun parrafo(
            texto: String,
            paint: Paint,
            espacioDespues: Int = 12
        ) {
            val anchoDisponible = ANCHO_PAGINA - (MARGEN * 2)
            val lineas = dividirTextoEnLineas(texto, paint, anchoDisponible)

            lineas.forEach { linea ->
                asegurarEspacio(16)
                canvasActual?.drawText(linea, MARGEN.toFloat(), yActual.toFloat(), paint)
                yActual += 15
            }

            yActual += espacioDespues
        }

        fun separadorFuerte() {
            asegurarEspacio(16)
            canvasActual?.drawLine(
                MARGEN.toFloat(),
                yActual.toFloat(),
                (ANCHO_PAGINA - MARGEN).toFloat(),
                yActual.toFloat(),
                pinturas.linea
            )
            yActual += 18
        }

        fun separadorSuave() {
            asegurarEspacio(18)
            canvasActual?.drawLine(
                MARGEN.toFloat(),
                yActual.toFloat(),
                (ANCHO_PAGINA - MARGEN).toFloat(),
                yActual.toFloat(),
                pinturas.linea
            )
            yActual += 16
        }

        fun cerrar() {
            cerrarPaginaActual()
        }

        // Finaliza la página actual e inicia una nueva página del documento.
        private fun nuevaPagina() {
            cerrarPaginaActual()

            numeroPagina++

            val infoPagina = PdfDocument.PageInfo.Builder(
                ANCHO_PAGINA,
                ALTO_PAGINA,
                numeroPagina
            ).create()

            paginaActual = documento.startPage(infoPagina)
            canvasActual = paginaActual?.canvas
            yActual = MARGEN
        }

        // Agrega el número de página y cierra correctamente la página actual.
        private fun cerrarPaginaActual() {
            val pagina = paginaActual ?: return

            canvasActual?.drawText(
                "Página $numeroPagina",
                (ANCHO_PAGINA - MARGEN - 60).toFloat(),
                (ALTO_PAGINA - 22).toFloat(),
                pinturas.piePagina
            )

            documento.finishPage(pagina)

            paginaActual = null
            canvasActual = null
        }

        // Verifica si existe espacio suficiente antes de escribir.
        // Si no lo hay, crea automáticamente una nueva página.
        private fun asegurarEspacio(espacioNecesario: Int) {
            if (yActual + espacioNecesario > ALTO_PAGINA - MARGEN) {
                nuevaPagina()
            }
        }

        /**
         * Divide un texto largo en varias líneas utilizando el ancho
         * máximo disponible para evitar que el contenido salga de la página.
         */
        private fun dividirTextoEnLineas(
            texto: String,
            paint: Paint,
            anchoMaximo: Int
        ): List<String> {
            if (texto.isBlank()) {
                return listOf("")
            }

            val palabras = texto.split(" ")
            val lineas = mutableListOf<String>()
            var lineaActual = ""

            palabras.forEach { palabra ->
                val prueba = if (lineaActual.isBlank()) {
                    palabra
                } else {
                    "$lineaActual $palabra"
                }

                if (paint.measureText(prueba) <= anchoMaximo) {
                    lineaActual = prueba
                } else {
                    if (lineaActual.isNotBlank()) {
                        lineas.add(lineaActual)
                    }

                    lineaActual = palabra
                }
            }

            if (lineaActual.isNotBlank()) {
                lineas.add(lineaActual)
            }

            return lineas
        }
    }
}