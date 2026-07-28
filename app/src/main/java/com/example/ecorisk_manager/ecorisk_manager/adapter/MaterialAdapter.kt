package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.data.entity.MaterialPeligrosoEntity
import com.example.ecorisk_manager.databinding.ItemMaterialBinding

/**
 * Adaptador encargado de mostrar la lista de materiales peligrosos
 * dentro del RecyclerView y gestionar las acciones del usuario.
 */
class MaterialAdapter(
    private val alVerDetalleMaterial: (MaterialPeligrosoEntity) -> Unit,
    private val alEditarMaterial: (MaterialPeligrosoEntity) -> Unit
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    // Lista que contiene los materiales mostrados en pantalla.
    private val listaMateriales = mutableListOf<MaterialPeligrosoEntity>()

    /**
     * Reemplaza la lista actual por una nueva y actualiza la vista.
     */
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

    /**
     * Asocia los datos del material con el elemento correspondiente.
     */
    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        holder.mostrarMaterial(listaMateriales[position])
    }

    override fun getItemCount(): Int {
        return listaMateriales.size
    }

    /**
     * ViewHolder encargado de mostrar la información de cada material
     * y responder a las acciones del usuario.
     */
    inner class MaterialViewHolder(
        private val binding: ItemMaterialBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Muestra la información del material en la tarjeta
         * y configura los eventos de los botones.
         */
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

            // Permite abrir el detalle tocando cualquier parte de la tarjeta.
            binding.root.setOnClickListener {
                alVerDetalleMaterial(material)
            }
        }
    }
}