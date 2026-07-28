package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.databinding.ItemMaterialProveedorBinding
import com.example.ecorisk_manager.model.MaterialProveedorDetalle

/**
 * Adaptador encargado de mostrar las relaciones entre materiales
 * y proveedores dentro del RecyclerView.
 */
class MaterialProveedorAdapter(
    private val alEliminarRelacion: (MaterialProveedorDetalle) -> Unit
) : RecyclerView.Adapter<MaterialProveedorAdapter.MaterialProveedorViewHolder>() {

    // Lista que contiene las relaciones mostradas en pantalla.
    private val listaRelaciones = mutableListOf<MaterialProveedorDetalle>()

    /**
     * Reemplaza la lista actual por una nueva y actualiza la vista.
     */
    fun actualizarLista(nuevaLista: List<MaterialProveedorDetalle>) {
        listaRelaciones.clear()
        listaRelaciones.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialProveedorViewHolder {
        val binding = ItemMaterialProveedorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MaterialProveedorViewHolder(binding)
    }

    /**
     * Asocia los datos de la relación con el elemento correspondiente.
     */
    override fun onBindViewHolder(holder: MaterialProveedorViewHolder, position: Int) {
        holder.mostrarRelacion(listaRelaciones[position])
    }

    override fun getItemCount(): Int {
        return listaRelaciones.size
    }

    /**
     * ViewHolder encargado de mostrar la información de cada relación
     * entre un material y su proveedor.
     */
    inner class MaterialProveedorViewHolder(
        private val binding: ItemMaterialProveedorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Muestra la información de la relación en la tarjeta
         * y configura la acción para eliminarla.
         */
        fun mostrarRelacion(relacion: MaterialProveedorDetalle) {
            binding.textoNombreMaterial.text = relacion.nombreMaterial
            binding.textoCodigoMaterial.text = "Código: ${relacion.codigoMaterial}"
            binding.textoRiesgoMaterial.text = "Riesgo: ${relacion.clasificacionRiesgo}"
            binding.textoProveedor.text = "Proveedor: ${relacion.nombreProveedor}"
            binding.textoCorreoProveedor.text = "Correo: ${relacion.correoProveedor}"
            binding.textoPrecioReferencia.text = "Precio referencia: ₡${relacion.precioReferencia}"

            binding.botonEliminarRelacion.setOnClickListener {
                alEliminarRelacion(relacion)
            }
        }
    }
}