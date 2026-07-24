package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.databinding.ItemMaterialProveedorBinding
import com.example.ecorisk_manager.model.MaterialProveedorDetalle

class MaterialProveedorAdapter(
    private val alEliminarRelacion: (MaterialProveedorDetalle) -> Unit
) : RecyclerView.Adapter<MaterialProveedorAdapter.MaterialProveedorViewHolder>() {

    private val listaRelaciones = mutableListOf<MaterialProveedorDetalle>()

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

    override fun onBindViewHolder(holder: MaterialProveedorViewHolder, position: Int) {
        holder.mostrarRelacion(listaRelaciones[position])
    }

    override fun getItemCount(): Int {
        return listaRelaciones.size
    }

    inner class MaterialProveedorViewHolder(
        private val binding: ItemMaterialProveedorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

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