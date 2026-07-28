package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.databinding.ItemIncidenteBinding
import com.example.ecorisk_manager.model.IncidenteDetalle

/**
 * Adaptador encargado de mostrar la lista de incidentes
 * dentro del RecyclerView y gestionar las acciones del usuario.
 */
class IncidenteAdapter(
    private val alVerDetalleIncidente: (IncidenteDetalle) -> Unit,
    private val alEditarIncidente: (IncidenteDetalle) -> Unit
) : RecyclerView.Adapter<IncidenteAdapter.IncidenteViewHolder>() {

    // Lista que contiene los incidentes mostrados en pantalla.
    private val listaIncidentes = mutableListOf<IncidenteDetalle>()

    /**
     * Reemplaza la lista actual por una nueva y actualiza la vista.
     */
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

    /**
     * Asocia los datos del incidente con el elemento correspondiente.
     */
    override fun onBindViewHolder(holder: IncidenteViewHolder, position: Int) {
        holder.mostrarIncidente(listaIncidentes[position])
    }

    override fun getItemCount(): Int {
        return listaIncidentes.size
    }

    /**
     * ViewHolder encargado de mostrar la información de cada incidente
     * y responder a las acciones del usuario.
     */
    inner class IncidenteViewHolder(
        private val binding: ItemIncidenteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Muestra la información del incidente en la tarjeta
         * y configura los eventos de los botones.
         */
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

            // Permite abrir el detalle tocando cualquier parte de la tarjeta.
            binding.root.setOnClickListener {
                alVerDetalleIncidente(incidente)
            }
        }
    }
}