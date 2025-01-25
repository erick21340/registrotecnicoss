 package com.example.registrotecnicos.presentation.screens.tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.registrotecnicos.data.local.entities.TecnicoEntity
import com.example.registrotecnicos.data.repository.TecnicoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TecnicoViewModel @Inject constructor(
    private val tecnicoRepository: TecnicoRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTecnicos()
    }

    fun validarCampos(): Boolean {
        return !_uiState.value.tecnico.isNullOrBlank() &&
                (_uiState.value.sueldo != 0.0)
    }

    fun save() {
        viewModelScope.launch {
            if (!validarCampos()) {
                _uiState.update {
                    it.copy(error = "Por favor, completa todos los campos correctamente.", guardado = false)
                }
                return@launch
            }

            val tecnico = _uiState.value.toEntity()
            tecnicoRepository.save(tecnico)
            _uiState.update { it.copy(error = null, guardado = true) }
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

    fun onTecnicoChange(tecnico: String) {
        _uiState.update {
            it.copy(tecnico = tecnico)
        }
    }

    fun onSueldoChange(sueldo: Double) {
        _uiState.update {
            it.copy(sueldo = sueldo)
        }
    }
}

data class UiState(
    val tecnicoId: Int? = null,
    val tecnico: String = "",
    val sueldo: Double = 0.0,
    val error: String? = null,
    var guardado: Boolean? = false,
    val tecnicos:List<TecnicoEntity> = emptyList(),
)

fun UiState.toEntity() = TecnicoEntity(
    tecnicoId = tecnicoId,
    tecnico = tecnico,
    sueldo = sueldo
)