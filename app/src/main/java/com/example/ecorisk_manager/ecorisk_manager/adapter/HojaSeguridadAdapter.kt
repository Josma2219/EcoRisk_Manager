package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.databinding.ItemHojaSeguridadBinding
import com.example.ecorisk_manager.model.HojaSeguridadDetalle

/**
 * Adaptador encargado de mostrar la lista de hojas de seguridad
 * dentro del RecyclerView y gestionar las acciones del usuario.
 */
class HojaSeguridadAdapter(
    private val alVerDetalleHoja: (HojaSeguridadDetalle) -> Unit,
    private val alEditarHoja: (HojaSeguridadDetalle) -> Unit
) : RecyclerView.Adapter<HojaSeguridadAdapter.HojaSeguridadViewHolder>() {

    // Lista que contiene las hojas de seguridad mostradas en pantalla.
    private val listaHojas = mutableListOf<HojaSeguridadDetalle>()

    /**
     * Reemplaza la lista actual por una nueva y actualiza la vista.
     */
    fun actualizarLista(nuevaLista: List<HojaSeguridadDetalle>) {
        listaHojas.clear()
        listaHojas.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HojaSeguridadViewHolder {
        val binding = ItemHojaSeguridadBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return HojaSeguridadViewHolder(binding)
    }

    /**
     * Asocia los datos de la hoja de seguridad con el elemento correspondiente.
     */
    override fun onBindViewHolder(holder: HojaSeguridadViewHolder, position: Int) {
        holder.mostrarHoja(listaHojas[position])
    }

    override fun getItemCount(): Int {
        return listaHojas.size
    }

    /**
     * ViewHolder encargado de mostrar la información de cada hoja de seguridad
     * y responder a las acciones del usuario.
     */
    inner class HojaSeguridadViewHolder(
        private val binding: ItemHojaSeguridadBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Muestra la información de una hoja de seguridad en la tarjeta
         * y configura los eventos de los botones.
         */
        fun mostrarHoja(hoja: HojaSeguridadDetalle) {
            binding.textoMaterialHoja.text = hoja.nombreMaterial
            binding.textoCodigoMaterial.text = "Código: ${hoja.codigoMaterial}"
            binding.textoVersionHoja.text = "Versión: ${hoja.version}"
            binding.textoFechaEmision.text = "Fecha emisión: ${hoja.fechaEmision}"
            binding.textoArchivoPdf.text = "Archivo: ${hoja.archivoPdf}"

            binding.botonDetalleHoja.setOnClickListener {
                alVerDetalleHoja(hoja)
            }

            binding.botonEditarHoja.setOnClickListener {
                alEditarHoja(hoja)
            }

            // Permite abrir el detalle tocando cualquier parte de la tarjeta.
            binding.root.setOnClickListener {
                alVerDetalleHoja(hoja)
            }
        }
    }
}