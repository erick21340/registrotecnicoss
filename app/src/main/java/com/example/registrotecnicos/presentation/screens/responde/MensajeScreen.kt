package com.example.registrotecnicos.presentation.screens.responde

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.registrotecnicos.data.local.entities.MensajeEntity
import com.example.registrotecnicos.data.local.entities.TecnicoEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeScreen(
    viewModel: MensajesViewModel = hiltViewModel(),
    onDrawerToggle: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var selectedTecnico by remember { mutableStateOf<TecnicoEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Mensajes") },
                navigationIcon = {
                    IconButton(onClick = onDrawerToggle) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            if (uiState.mensajes.isEmpty()) {
                Text("No hay mensajes disponibles.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.mensajes) { mensaje ->
                        MensajeCard(mensaje, uiState.tecnicos)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DropdownMenu para seleccionar el técnico
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium)
                    .clickable { expanded = true } // Permitir expandir el menú al hacer clic
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedTecnico?.tecnico ?: "Seleccionar Técnico",
                    color = if (selectedTecnico != null) Color.Black else Color.Gray
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    uiState.tecnicos.forEach { tecnico ->
                        DropdownMenuItem(
                            onClick = {
                                selectedTecnico = tecnico
                                expanded = false
                                viewModel.onTecnicoIdChange(tecnico.tecnicoId.toString())
                            },
                            text = { Text(tecnico.tecnico) }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.onDescripcionChange(it) },
                label = { Text("Descripción del Mensaje") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Fecha: ${uiState.fecha}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveMensaje() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Mensaje")
            }

            Spacer(modifier = Modifier.height(16.dp))

            uiState.successMessage?.let {
                Text(text = it, color = Color.Green)
            }
            uiState.error?.let {
                Text(text = it, color = Color.Red)
            }
        }
    }
}

@Composable
fun MensajeCard(mensaje: MensajeEntity, tecnicos: List<TecnicoEntity>) {
    val tecnico = tecnicos.find { it.tecnicoId == mensaje.tecnicoId }

    Column(modifier = Modifier.padding(horizontal = 4.dp)) {
        Text(
            text = "Mensaje enviado por Técnico ${tecnico?.tecnico ?: "Desconocido"} en ${mensaje.fecha}",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Descripción: ${mensaje.descripcion}",
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Fecha: ${mensaje.fecha}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.Blue)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Técnico",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
