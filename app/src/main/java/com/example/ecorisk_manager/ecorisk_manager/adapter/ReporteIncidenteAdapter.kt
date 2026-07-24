package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.databinding.ItemReporteIncidenteBinding
import com.example.ecorisk_manager.model.IncidenteDetalle

class ReporteIncidenteAdapter : RecyclerView.Adapter<ReporteIncidenteAdapter.ReporteIncidenteViewHolder>() {

    private val listaIncidentes = mutableListOf<IncidenteDetalle>()

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

    override fun onBindViewHolder(holder: ReporteIncidenteViewHolder, position: Int) {
        holder.mostrarIncidente(listaIncidentes[position])
    }

    override fun getItemCount(): Int {
        return listaIncidentes.size
    }

    inner class ReporteIncidenteViewHolder(
        private val binding: ItemReporteIncidenteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mostrarIncidente(incidente: IncidenteDetalle) {
            binding.textoTipoIncidente.text = incidente.tipoIncidente
            binding.textoMaterialIncidente.text = "Material: ${incidente.nombreMaterial}"
            binding.textoFechaIncidente.text = "Fecha: ${incidente.fechaIncidente}"
            binding.textoSeveridadIncidente.text = "Severidad: ${incidente.nivelSeveridad}"
            binding.textoEstadoIncidente.text = "Estado: ${incidente.estado}"

            binding.textoDescripcionIncidente.text = if (incidente.descripcion.isBlank()) {
                "Descripción: sin descripción registrada"
            } else {
                "Descripción: ${incidente.descripcion}"
            }
        }
    }
}