package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.databinding.ItemReporteIncidenteBinding
import com.example.ecorisk_manager.model.IncidenteDetalle

/**
 * Adaptador encargado de mostrar la información de los incidentes
 * dentro del reporte.
 */
class ReporteIncidenteAdapter : RecyclerView.Adapter<ReporteIncidenteAdapter.ReporteIncidenteViewHolder>() {

    // Lista que contiene los incidentes incluidos en el reporte.
    private val listaIncidentes = mutableListOf<IncidenteDetalle>()

    /**
     * Reemplaza la lista actual por una nueva y actualiza la vista.
     */
    fun actualizarLista(nuevaLista: List<IncidenteDetalle>) {
        listaIncidentes.clear()
        listaIncidentes.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteIncidenteViewHolder {
        val binding = ItemReporteIncidenteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReporteIncidenteViewHolder(binding)
    }

    /**
     * Asocia los datos del incidente con el elemento correspondiente.
     */
    override fun onBindViewHolder(holder: ReporteIncidenteViewHolder, position: Int) {
        holder.mostrarIncidente(listaIncidentes[position])
    }

    override fun getItemCount(): Int {
        return listaIncidentes.size
    }

    /**
     * ViewHolder encargado de mostrar la información de cada incidente
     * dentro del reporte.
     */
    inner class ReporteIncidenteViewHolder(
        private val binding: ItemReporteIncidenteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Muestra la información del incidente en el reporte.
         */
        fun mostrarIncidente(incidente: IncidenteDetalle) {
            binding.textoTipoIncidente.text = incidente.tipoIncidente
            binding.textoMaterialIncidente.text = "Material: ${incidente.nombreMaterial}"
            binding.textoFechaIncidente.text = "Fecha: ${incidente.fechaIncidente}"
            binding.textoSeveridadIncidente.text = "Severidad: ${incidente.nivelSeveridad}"
            binding.textoEstadoIncidente.text = "Estado: ${incidente.estado}"

            // Muestra un mensaje cuando el incidente no tiene una descripción registrada.
            binding.textoDescripcionIncidente.text = if (incidente.descripcion.isBlank()) {
                "Descripción: sin descripción registrada"
            } else {
                "Descripción: ${incidente.descripcion}"
            }
        }
    }
}