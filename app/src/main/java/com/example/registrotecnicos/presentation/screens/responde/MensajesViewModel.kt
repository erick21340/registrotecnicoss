package com.example.registrotecnicos.presentation.screens.responde

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrotecnicos.data.local.entities.MensajeEntity
import com.example.registrotecnicos.data.local.entities.TecnicoEntity
import com.example.registrotecnicos.data.repository.MensajeRepository
import com.example.registrotecnicos.data.repository.TecnicoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch


import java.util.Date

import javax.inject.Inject

@HiltViewModel
class MensajesViewModel @Inject constructor(
    private val mensajeRepository: MensajeRepository,
    private val tecnicoRepository: TecnicoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadMensajes()

        loadTecnicos()


    }

    private fun loadMensajes() {
        viewModelScope.launch {
            mensajeRepository.getAll().collect { mensajes ->
                _uiState.value = _uiState.value.copy(mensajes = mensajes)
            }
        }
    }

    private fun loadTecnicos() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                _uiState.value = _uiState.value.copy(tecnicos = tecnicos)
            }
        }
    }

    fun onDescripcionChange(newDescripcion: String) {
        _uiState.value = _uiState.value.copy(descripcion = newDescripcion)
    }

    fun onTecnicoIdChange(newTecnicoId: String) {
        _uiState.value = _uiState.value.copy(tecnicoId = newTecnicoId)
    }

    fun saveMensaje() {
        val currentState = _uiState.value
        if (currentState.tecnicoId != null && currentState.descripcion.isNotEmpty()) {
            val tecnicoI = currentState.tecnicoId.toIntOrNull()

            if (tecnicoI != null) {
                val fecha = currentState.fecha

                val nuevoMensaje = MensajeEntity(
                    tecnicoId = tecnicoI,
                    descripcion = currentState.descripcion,
                    fecha = fecha // Ahora guardamos la fecha como String
                )

                viewModelScope.launch {
                    mensajeRepository.saveMensaje(nuevoMensaje)
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Mensaje guardado con éxito",
                        descripcion = "",
                        tecnicoId = null,
                        fecha = System.currentTimeMillis().toString() // Convertimos a String
                    )
                }
            } else {
                _uiState.value = _uiState.value.copy(error = "ID de técnico no válido.")
            }
        } else {
            _uiState.value = _uiState.value.copy(error = "Debe llenar todos los campos.")
        }
    }

    fun nuevoMensaje() {
        _uiState.value = _uiState.value.copy(descripcion = "", tecnicoId = null, fecha = System.currentTimeMillis().toString())
    }
}

data class UiState(
    val mensajes: List<MensajeEntity> = emptyList(),
    val tecnicos: List<TecnicoEntity> = emptyList(),
    val descripcion: String = "",
    val tecnicoId: String? = null,
    val successMessage: String? = null,
    val error: String? = null,
    val fecha: String = System.currentTimeMillis().toString() // Cambio a String
)

fun UiState.toEntity(): MensajeEntity {
    return MensajeEntity(
        tecnicoId = tecnicoId?.toInt() ?: 0,
        descripcion = descripcion,
        fecha = fecha // Ahora se pasa como String
    )
}
