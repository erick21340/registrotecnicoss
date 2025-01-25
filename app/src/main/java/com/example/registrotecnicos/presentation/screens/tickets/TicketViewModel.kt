 package com.example.registrotecnicos.presentation.screens.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrotecnicos.data.local.entities.TecnicoEntity
import com.example.registrotecnicos.data.local.entities.TicketEntity
import com.example.registrotecnicos.data.repository.TecnicoRepository
import com.example.registrotecnicos.data.repository.TicketRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val tecnicoRepository: TecnicoRepository,
    private val ticketRepository: TicketRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTickets()
        getTecnicos()
    }

    fun validarCampos(): Boolean {
        return !_uiState.value.asunto.isNullOrBlank() &&
                !_uiState.value.descripcion.isNullOrBlank() &&
                !_uiState.value.cliente.isNullOrBlank() &&
                (_uiState.value.tecnicoId != 0)
    }

    fun save() {
        viewModelScope.launch {
            if (!validarCampos()) {
                _uiState.update {
                    it.copy(error = "Por favor, completa todos los campos correctamente.", guardado = false)
                }
                return@launch
            }

            val ticket = _uiState.value.toEntity()
            ticketRepository.save(ticket)
            _uiState.update { it.copy(error = null, guardado = true) }
        }
    }

    fun update() {
        viewModelScope.launch {
            if (!validarCampos() || _uiState.value.ticketId == null) {
                _uiState.update {
                    it.copy(error = "Por favor, completa todos los campos correctamente.", guardado = false)
                }
                return@launch
            }

            val ticket = _uiState.value.toEntity()
            ticketRepository.update(ticket)
            _uiState.update { it.copy(error = null, guardado = true) }
        }
    }

    fun delete() {
        viewModelScope.launch {
            if (_uiState.value.ticketId != null) {
                ticketRepository.delete(_uiState.value.toEntity())
                _uiState.update { it.copy(ticketId = null) }
            }
        }
    }

    fun getById(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticket = ticketRepository.get(ticketId)
                _uiState.update { currentState ->
                    currentState.copy(
                        ticketId = ticket?.ticketId,
                        prioridadId = ticket?.prioridadId,
                        tecnicoId = ticket?.tecnicoId,
                        fecha = ticket?.fecha ?: "",
                        cliente = ticket?.cliente ?: "",
                        asunto = ticket?.asunto ?: "",
                        descripcion = ticket?.descripcion ?: "",
                        error = null
                    )
                }
            }
        }
    }

    private fun getTickets() {
        viewModelScope.launch {
            ticketRepository.getAll().collect { tickets ->
                _uiState.update {
                    it.copy(tickets = tickets)
                }
            }
        }
    }

    private fun getTecnicos() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                _uiState.update {
                    it.copy(tecnicos = tecnicos)
                }
            }
        }
    }

    fun onClienteChange(cliente: String) {
        _uiState.update {
            it.copy(cliente = cliente)
        }
    }

    fun onAsuntoChange(asunto: String) {
        _uiState.update {
            it.copy(asunto = asunto)
        }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(descripcion = descripcion)
        }
    }

    fun onFechaChange(fecha: String) {
        _uiState.update {
            it.copy(fecha = fecha)
        }
    }

    fun onTecnicoIdChange(tecnicoId: Int) {
        _uiState.update {
            it.copy(tecnicoId = tecnicoId)
        }
    }
}

data class UiState(
    val ticketId: Int? = null,
    val prioridadId: Int? = null,
    val tecnicoId: Int? = null,
    val fecha: String = "",
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val error: String? = null,
    var guardado: Boolean? = false,
    val tecnicos:List<TecnicoEntity> = emptyList(),
    val tickets:List<TicketEntity> = emptyList(),
)

fun UiState.toEntity() = TicketEntity(
    ticketId = ticketId,
    prioridadId = prioridadId ?: 0,
    tecnicoId = tecnicoId ?: 0,
    fecha = fecha ?: "",
    cliente = cliente ?: "",
    asunto = asunto ?: "",
    descripcion = descripcion ?: ""
)