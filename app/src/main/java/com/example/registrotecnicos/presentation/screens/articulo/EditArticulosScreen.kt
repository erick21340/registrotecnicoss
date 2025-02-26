 package com.example.registrotecnicos.presentation.screens.articulo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ucne.edu.registrotecnicos.presentation.articulo.ArticuloViewModel
import ucne.edu.registrotecnicos.presentation.articulo.UiState


 @Composable
fun EditArticulosScreen(
    viewModel: ArticuloViewModel = hiltViewModel(),
    articuloId: Int?,
    onDrawerToggle: () -> Unit,
    goToArticulo: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = articuloId) {
        if (articuloId != null) {
            viewModel.getById(articuloId)
        }
    }

    BodyEditArticulo(
        uiState = uiState,
        onDrawerToggle = onDrawerToggle,
        onDescripcionChange = viewModel::onDescripcionChange,
        onCostoChange = viewModel::onCostoChange,
        onGananciaChange = viewModel::onGananciaChange,
        onPrecioChange = viewModel::onPrecioChange,
        goToArticulo = goToArticulo,
        update = viewModel::update
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyEditArticulo(
    uiState: UiState,
    onDrawerToggle: () -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCostoChange: (String) -> Unit,
    onGananciaChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    goToArticulo: () -> Unit,
    update: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Ticket") },
                navigationIcon = {
                    IconButton(onClick = {
                        onDrawerToggle()
                    }) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
        ) {

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = onDescripcionChange,
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.costo,
                onValueChange = onCostoChange,
                label = { Text("Costo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.precio,
                onValueChange = onPrecioChange,
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.ganancia,
                onValueChange = onGananciaChange,
                label = { Text("Ganancia") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    update()
                    goToArticulo()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled =   uiState.descripcion.isNotEmpty() &&
                        uiState.precio.isNotEmpty() &&
                        uiState.costo.isNotEmpty() &&
                        uiState.ganancia.isNotEmpty()
            ) {
                Text("Actualizar")
            }

            uiState.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
