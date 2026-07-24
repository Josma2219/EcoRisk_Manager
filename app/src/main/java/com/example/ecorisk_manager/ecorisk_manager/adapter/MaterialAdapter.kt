package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.databinding.ItemMaterialBinding

class MaterialAdapter(
    private val alVerDetalleMaterial: (MaterialPeligrosoEntity) -> Unit,
    private val alEditarMaterial: (MaterialPeligrosoEntity) -> Unit
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    private val listaMateriales = mutableListOf<MaterialPeligrosoEntity>()

    fun actualizarLista(nuevaLista: List<MaterialPeligrosoEntity>) {
        listaMateriales.clear()
        listaMateriales.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding = ItemMaterialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.mostrarMaterial(listaMateriales[position])
    }

    override fun getItemCount(): Int {
        return listaMateriales.size
    }

    inner class MaterialViewHolder(
        private val binding: ItemMaterialBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mostrarMaterial(material: MaterialPeligrosoEntity) {
            binding.textoNombreMaterial.text = material.nombreComercial
            binding.textoCodigoMaterial.text = "Código: ${material.codigoMaterial}"
            binding.textoRiesgoMaterial.text = "Riesgo: ${material.clasificacionRiesgo}"
            binding.textoEstadoMaterial.text = "Estado: ${material.estado}"

            binding.botonVerDetalleMaterial.setOnClickListener {
                alVerDetalleMaterial(material)
            }

            binding.botonEditarMaterial.setOnClickListener {
                alEditarMaterial(material)
            }

            // También dejamos que tocar la tarjeta abra el detalle.
            // Es un extra pequeño, pero hace la app más cómoda.
            binding.root.setOnClickListener {
                alVerDetalleMaterial(material)
            }
        }
    }
}