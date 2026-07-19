package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.databinding.ItemReporteMaterialBinding

class ReporteMaterialAdapter : RecyclerView.Adapter<ReporteMaterialAdapter.ReporteMaterialViewHolder>() {

    private val listaMateriales = mutableListOf<MaterialPeligrosoEntity>()

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

    override fun onBindViewHolder(holder: ReporteMaterialViewHolder, position: Int) {
        holder.mostrarMaterial(listaMateriales[position])
    }

    override fun getItemCount(): Int {
        return listaMateriales.size
    }

    inner class ReporteMaterialViewHolder(
        private val binding: ItemReporteMaterialBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mostrarMaterial(material: MaterialPeligrosoEntity) {
            binding.textoNombreMaterial.text = material.nombreComercial
            binding.textoCodigoMaterial.text = "Código: ${material.codigoMaterial}"
            binding.textoRiesgoMaterial.text = "Riesgo: ${material.clasificacionRiesgo}"
            binding.textoEstadoMaterial.text = "Estado: ${material.estado}"
            binding.textoUnidadMaterial.text = "Unidad: ${material.unidadMedida}"
        }
    }
}