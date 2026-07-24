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

object GeneradorPdf {

    private const val ANCHO_PAGINA = 595
    private const val ALTO_PAGINA = 842
    private const val MARGEN = 40

    fun generarReporteMaterialesPorRiesgo(
        contexto: Context,
        filtroAplicado: String,
        materiales: List<MaterialPeligrosoEntity>
    ): File {
        val documento = PdfDocument()
        val escritor = EscritorPdf(documento)

        val titulo = "Reporte de materiales por riesgo"

        escritor.dibujarEncabezado(
            titulo = titulo,
            filtroAplicado = filtroAplicado,
            total = materiales.size
        )

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

        return guardarDocumento(
            contexto = contexto,
            documento = documento,
            escritor = escritor,
            nombreBase = "reporte_materiales_riesgo"
        )
    }

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
        escritor.cerrar()

        val carpetaReportes = obtenerCarpetaReportes(contexto)
        val nombreArchivo = "${nombreBase}_${fechaParaArchivo()}.pdf"
        val archivo = File(carpetaReportes, nombreArchivo)

        FileOutputStream(archivo).use { salida ->
            documento.writeTo(salida)
        }

        documento.close()

        return archivo
    }

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

    private class EscritorPdf(
        private val documento: PdfDocument
    ) {
        private var numeroPagina = 0
        private var paginaActual: PdfDocument.Page? = null
        private var canvasActual: Canvas? = null
        private var yActual = MARGEN

        init {
            nuevaPagina()
        }

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

        fun linea(
            texto: String,
            paint: Paint,
            espacioDespues: Int = 16
        ) {
            asegurarEspacio(espacioDespues + 8)
            canvasActual?.drawText(texto, MARGEN.toFloat(), yActual.toFloat(), paint)
            yActual += espacioDespues
        }

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

        private fun asegurarEspacio(espacioNecesario: Int) {
            if (yActual + espacioNecesario > ALTO_PAGINA - MARGEN) {
                nuevaPagina()
            }
        }

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