package ucne.edu.registrotecnicos.presentation.articulo

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.example.registrotecnicos.data.repository.ArticuloRepository
import com.example.registrotecnicos.presentation.screens.tickets.toEntity
import com.example.registrotecnicos.utils.Resource
import com.example.registrotecnicos.remote.dto.ArticuloDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class ArticuloViewModel @Inject constructor(
    private val articuloRepository: ArticuloRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        getArticulos()
    }

    fun validarCampos(): Boolean {
        return  !_uiState.value.descripcion.isNullOrBlank() &&
                !_uiState.value.costo.isNullOrBlank() &&
                !_uiState.value.precio.isNullOrBlank() &&
                !_uiState.value.ganancia.isNullOrBlank()
    }

    fun save() {
        viewModelScope.launch {
            if (!validarCampos()) {
                _uiState.update {
                    it.copy(error = "Por favor, completa todos los campos correctamente.")
                }
                return@launch
            }

            val articulo = _uiState.value.toEntity()
            articuloRepository.save(articulo)
            _uiState.update { it.copy(error = null) }
        }
    }

    fun delete() {
        viewModelScope.launch {
            if (_uiState.value.articuloId != null) {
                articuloRepository.delete(_uiState.value.articuloId)
                _uiState.update { it.copy(articuloId = 0) }
            }
        }
    }

    fun update() {
        viewModelScope.launch {
            if (!validarCampos() || _uiState.value.articuloId == null) {
                _uiState.update {
                    it.copy(error = "Por favor, completa todos los campos correctamente.")
                }
                return@launch
            }

            val articulo = _uiState.value.toEntity()
            articuloRepository.update(articulo.articuloId, articulo)
            _uiState.update { it.copy(error = null) }
        }
    }

    fun new() {
        _uiState.value = UiState()
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(
                descripcion = descripcion,
                error = if (descripcion.isBlank()) "Debes rellenar el campo Descripción"
                else null
            )
        }
    }

    fun onCostoChange(costo: String) {
        _uiState.update {
            val costoDouble = costo.toDoubleOrNull()
            val gananciaDouble = it.ganancia.toDoubleOrNull()
            val precio = if (costoDouble != null && gananciaDouble != null)
                (costoDouble * gananciaDouble / 100) + costoDouble
            else null

            it.copy(
                costo = costo,
                precio = precio?.toString() ?: "",
                error = when {
                    costoDouble == null -> "Valor no numérico"
                    costoDouble <= 0 -> "Debe ingresar un valor mayor a 0"
                    else -> null
                }
            )
        }
    }

    fun onGananciaChange(ganancia: String) {
        _uiState.update {
            val gananciaDouble = ganancia.toDoubleOrNull()
            val costoDouble = it.costo.toDoubleOrNull()
            val precio = if (costoDouble != null && gananciaDouble != null)
                (costoDouble * gananciaDouble / 100) + costoDouble
            else null

            it.copy(
                ganancia = ganancia,
                precio = precio?.toString() ?: "",
                error = when {
                    gananciaDouble == null -> "Valor no numérico"
                    gananciaDouble < 0 -> "Debe ser 0 o mayor"
                    else -> null
                }
            )
        }
    }

    fun onPrecioChange(precio: String) {
        _uiState.update {
            val precioDouble = precio.toDoubleOrNull()
            val costoDouble = it.costo.toDoubleOrNull()
            val gananciaDouble = it.ganancia.toDoubleOrNull()

            val calculatedPrecio = if (precioDouble != null) precioDouble
            else if (costoDouble != null && gananciaDouble != null)
                (costoDouble * gananciaDouble / 100) + costoDouble
            else null

            it.copy(
                precio = calculatedPrecio?.toString() ?: "",
                error = when {
                    precioDouble == null && calculatedPrecio == null -> "Valor no numérico"
                    precioDouble != null && precioDouble <= 0 -> "Debe ingresar un valor mayor a 0"
                    else -> null
                }
            )
        }
    }



    fun getById(articuloId: Int) {
        viewModelScope.launch {
            if (articuloId > 0) {
                val articuloDto = articuloRepository.find(articuloId)
                if (articuloDto.articuloId != 0) {
                    _uiState.update {
                        it.copy(
                            articuloId = articuloDto.articuloId,
                            descripcion = articuloDto.descripcion,
                            costo = articuloDto.costo.toString(),
                            ganancia = articuloDto.ganancia.toString(),
                            precio = articuloDto.precio.toString()
                        )
                    }
                }
            }
        }
    }

    fun getArticulos() {
        viewModelScope.launch {
            articuloRepository.getArticulos().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                articulos = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                error = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}

fun UiState.toEntity() = ArticuloDto(
    articuloId = articuloId,
    descripcion = descripcion,
    costo = costo.toDouble(),
    ganancia = ganancia.toDouble(),
    precio = precio.toDouble()
)

data class UiState(
    val articuloId: Int = 0,
    val descripcion: String = "",
    val costo: String = "",
    val ganancia: String = "",
    val precio: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val articulos: List<ArticuloDto> = emptyList()
)