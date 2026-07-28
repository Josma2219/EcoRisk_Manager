package com.example.ecorisk_manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecorisk_manager.data.entity.ProveedorEntity
import com.example.ecorisk_manager.databinding.ItemProveedorBinding

/**
 * Adaptador encargado de mostrar la lista de proveedores
 * dentro del RecyclerView y gestionar las acciones del usuario.
 */
class ProveedorAdapter(
    private val alVerDetalleProveedor: (ProveedorEntity) -> Unit,
    private val alEditarProveedor: (ProveedorEntity) -> Unit
) : RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder>() {

    // Lista que contiene los proveedores mostrados en pantalla.
    private val listaProveedores = mutableListOf<ProveedorEntity>()

    /**
     * Reemplaza la lista actual por una nueva y actualiza la vista.
     */
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

    /**
     * Asocia los datos del proveedor con el elemento correspondiente.
     */
    override fun onBindViewHolder(holder: ProveedorViewHolder, position: Int) {
        holder.mostrarProveedor(listaProveedores[position])
    }

    override fun getItemCount(): Int {
        return listaProveedores.size
    }

    /**
     * ViewHolder encargado de mostrar la información de cada proveedor
     * y responder a las acciones del usuario.
     */
    inner class ProveedorViewHolder(
        private val binding: ItemProveedorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Muestra la información del proveedor en la tarjeta
         * y configura los eventos de los botones.
         */
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

            // Permite abrir el detalle tocando cualquier parte de la tarjeta.
            binding.root.setOnClickListener {
                alVerDetalleProveedor(proveedor)
            }
        }
    }
}