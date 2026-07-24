package com.example.ecorisk_manager.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecorisk_manager.data.repository.RespaldoRepository
import com.example.ecorisk_manager.model.ResultadoOperacion
import kotlinx.coroutines.launch

class RespaldoViewModel(
    private val respaldoRepository: RespaldoRepository
) : ViewModel() {

    private val _resultadoOperacion = MutableLiveData<ResultadoOperacion?>()
    val resultadoOperacion: LiveData<ResultadoOperacion?> = _resultadoOperacion

    fun generarRespaldo(contexto: Context) {
        viewModelScope.launch {
            val resultado = respaldoRepository.generarRespaldo(contexto.applicationContext)
            _resultadoOperacion.value = resultado
        }
    }

    fun restaurarUltimoRespaldo(contexto: Context) {
        viewModelScope.launch {
            val resultado = respaldoRepository.restaurarUltimoRespaldo(contexto.applicationContext)
            _resultadoOperacion.value = resultado
        }
    }

    fun limpiarResultadoOperacion() {
        _resultadoOperacion.value = null
    }
}