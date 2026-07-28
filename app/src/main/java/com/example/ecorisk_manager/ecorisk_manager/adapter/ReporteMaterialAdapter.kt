package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.databinding.ItemReporteMaterialBinding

/**
 * Adaptador encargado de mostrar la información de los materiales
 * dentro del reporte.
 */
class ReporteMaterialAdapter : RecyclerView.Adapter<ReporteMaterialAdapter.ReporteMaterialViewHolder>() {

    // Lista que contiene los materiales incluidos en el reporte.
    private val listaMateriales = mutableListOf<MaterialPeligrosoEntity>()

    /**
     * Reemplaza la lista actual por una nueva y actualiza la vista.
     */
    fun actualizarLista(nuevaLista: List<MaterialPeligrosoEntity>) {
        listaMateriales.clear()
        listaMateriales.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReporteMaterialViewHolder {
        val binding = ItemReporteMaterialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ReporteMaterialViewHolder(binding)
    }

    /**
     * Asocia los datos del material con el elemento correspondiente.
     */
    override fun onBindViewHolder(holder: ReporteMaterialViewHolder, position: Int) {
        holder.mostrarMaterial(listaMateriales[position])
    }

    override fun getItemCount(): Int {
        return listaMateriales.size
    }

    /**
     * ViewHolder encargado de mostrar la información de cada material
     * dentro del reporte.
     */
    inner class ReporteMaterialViewHolder(
        private val binding: ItemReporteMaterialBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Muestra la información del material en el reporte.
         */
        fun mostrarMaterial(material: MaterialPeligrosoEntity) {
            binding.textoNombreMaterial.text = material.nombreComercial
            binding.textoCodigoMaterial.text = "Código: ${material.codigoMaterial}"
            binding.textoRiesgoMaterial.text = "Riesgo: ${material.clasificacionRiesgo}"
            binding.textoEstadoMaterial.text = "Estado: ${material.estado}"
            binding.textoUnidadMaterial.text = "Unidad: ${material.unidadMedida}"
        }
    }
}