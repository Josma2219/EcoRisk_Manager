package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.databinding.ItemIncidenteBinding
import com.example.ecorisk_manager.model.IncidenteDetalle

class IncidenteAdapter(
    private val alVerDetalleIncidente: (IncidenteDetalle) -> Unit,
    private val alEditarIncidente: (IncidenteDetalle) -> Unit
) : RecyclerView.Adapter<IncidenteAdapter.IncidenteViewHolder>() {

    private val listaIncidentes = mutableListOf<IncidenteDetalle>()

    fun actualizarLista(nuevaLista: List<IncidenteDetalle>) {
        listaIncidentes.clear()
        listaIncidentes.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidenteViewHolder {
        val binding = ItemIncidenteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return IncidenteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IncidenteViewHolder, position: Int) {
        holder.mostrarIncidente(listaIncidentes[position])
    }

    override fun getItemCount(): Int {
        return listaIncidentes.size
    }

    inner class IncidenteViewHolder(
        private val binding: ItemIncidenteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mostrarIncidente(incidente: IncidenteDetalle) {
            binding.textoTipoIncidente.text = incidente.tipoIncidente
            binding.textoMaterialIncidente.text = "Material: ${incidente.nombreMaterial}"
            binding.textoFechaIncidente.text = "Fecha: ${incidente.fechaIncidente}"
            binding.textoSeveridadIncidente.text = "Severidad: ${incidente.nivelSeveridad}"
            binding.textoEstadoIncidente.text = "Estado: ${incidente.estado}"

            binding.botonDetalleIncidente.setOnClickListener {
                alVerDetalleIncidente(incidente)
            }

            binding.botonEditarIncidente.setOnClickListener {
                alEditarIncidente(incidente)
            }

            binding.root.setOnClickListener {
                alVerDetalleIncidente(incidente)
            }
        }
    }
}