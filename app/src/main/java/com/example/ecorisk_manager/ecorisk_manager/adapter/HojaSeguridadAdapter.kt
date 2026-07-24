package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.databinding.ItemHojaSeguridadBinding
import com.example.ecorisk_manager.model.HojaSeguridadDetalle

class HojaSeguridadAdapter(
    private val alVerDetalleHoja: (HojaSeguridadDetalle) -> Unit,
    private val alEditarHoja: (HojaSeguridadDetalle) -> Unit
) : RecyclerView.Adapter<HojaSeguridadAdapter.HojaSeguridadViewHolder>() {

    private val listaHojas = mutableListOf<HojaSeguridadDetalle>()

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

    override fun onBindViewHolder(holder: HojaSeguridadViewHolder, position: Int) {
        holder.mostrarHoja(listaHojas[position])
    }

    override fun getItemCount(): Int {
        return listaHojas.size
    }

    inner class HojaSeguridadViewHolder(
        private val binding: ItemHojaSeguridadBinding
    ) : RecyclerView.ViewHolder(binding.root) {

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

            // Tocar toda la tarjeta abre detalle, más cómodo en demo.
            binding.root.setOnClickListener {
                alVerDetalleHoja(hoja)
            }
        }
    }
}