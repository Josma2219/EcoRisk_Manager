package com.example.ecorisk_manager.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.repository.RespaldoRepository
import com.example.ecorisk_manager.model.ResultadoOperacion
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de gestionar las operaciones
 * de respaldo y restauración de la base de datos.
 */
class RespaldoViewModel(
    private val respaldoRepository: RespaldoRepository
) : ViewModel() {

    private val _resultadoOperacion = MutableLiveData<ResultadoOperacion?>()
    val resultadoOperacion: LiveData<ResultadoOperacion?> = _resultadoOperacion

    /**
     * Genera un respaldo de la información almacenada
     * en la base de datos.
     */
    fun generarRespaldo(contexto: Context) {
        viewModelScope.launch {
            val resultado = respaldoRepository.generarRespaldo(contexto.applicationContext)
            _resultadoOperacion.value = resultado
        }
    }

    /**
     * Restaura el último respaldo disponible.
     */
    fun restaurarUltimoRespaldo(contexto: Context) {
        viewModelScope.launch {
            val resultado = respaldoRepository.restaurarUltimoRespaldo(contexto.applicationContext)
            _resultadoOperacion.value = resultado
        }
    }

    /**
     * Restablece el resultado de la última operación
     * para evitar que vuelva a procesarse.
     */
    fun limpiarResultadoOperacion() {
        _resultadoOperacion.value = null
    }
}