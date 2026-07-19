package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.databinding.ItemProveedorBinding

class ProveedorAdapter(
    private val alVerDetalleProveedor: (ProveedorEntity) -> Unit,
    private val alEditarProveedor: (ProveedorEntity) -> Unit
) : RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder>() {

    private val listaProveedores = mutableListOf<ProveedorEntity>()

    fun actualizarLista(nuevaLista: List<ProveedorEntity>) {
        listaProveedores.clear()
        listaProveedores.addAll(nuevaLista)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProveedorViewHolder {
        val binding = ItemProveedorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProveedorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProveedorViewHolder, position: Int) {
        holder.mostrarProveedor(listaProveedores[position])
    }

    override fun getItemCount(): Int {
        return listaProveedores.size
    }

    inner class ProveedorViewHolder(
        private val binding: ItemProveedorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun mostrarProveedor(proveedor: ProveedorEntity) {
            binding.textoNombreProveedor.text = proveedor.nombre
            binding.textoCorreoProveedor.text = "Correo: ${proveedor.correo}"
            binding.textoTelefonoProveedor.text = "Teléfono: ${proveedor.telefono}"
            binding.textoContactoProveedor.text = "Contacto: ${proveedor.contactoPrincipal}"

            binding.botonVerDetalleProveedor.setOnClickListener {
                alVerDetalleProveedor(proveedor)
            }

            binding.botonEditarProveedor.setOnClickListener {
                alEditarProveedor(proveedor)
            }

            // Tocar la tarjeta también abre el detalle, más cómodo para demo.
            binding.root.setOnClickListener {
                alVerDetalleProveedor(proveedor)
            }
        }
    }
}