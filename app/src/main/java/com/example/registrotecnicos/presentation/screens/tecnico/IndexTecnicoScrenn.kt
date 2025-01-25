 package com.example.registrotecnicos.presentation.screens.tecnico


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.registrotecnicos.data.local.entities.TecnicoEntity

@Composable
fun IndexTecnicoScreen(
    viewModel: TecnicoViewModel = hiltViewModel(),
    onDrawerToggle: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BodyTecnico(
        uiState = uiState,
        onDrawerToggle = onDrawerToggle,
        onTecnicoChange = viewModel::onTecnicoChange,
        onSueldoChange = viewModel::onSueldoChange,
        saveTecnico = viewModel::save
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyTecnico(
    uiState: UiState,
    onDrawerToggle: () -> Unit,
    onTecnicoChange: (String) -> Unit,
    onSueldoChange: (Double) -> Unit,
    saveTecnico: () -> Unit,
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Técnico") },
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
                .padding(8.dp)
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text(text = "Técnico") },
                        value = uiState.tecnico,
                        onValueChange = onTecnicoChange,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text(text = "Sueldo") },
                        value = if (uiState.sueldo > 0.0) uiState.sueldo.toString() else "",
                        onValueChange = { sueldo -> onSueldoChange(sueldo.toDoubleOrNull() ?: 0.0) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(2.dp))

                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = {
                            onTecnicoChange("")
                            onSueldoChange(0.0)
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Nuevo")
                            Text(text = "Nuevo")
                        }

                        OutlinedButton(
                            onClick = {
                                if (uiState.tecnico.isBlank()) {
                                    errorMessage = "Nombre de técnico vacío"
                                } else if (uiState.sueldo <= 0.0) {
                                    errorMessage = "Sueldo no válido"
                                } else {
                                    errorMessage = null
                                    saveTecnico()
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Guardar")
                            Text(text = "Guardar")
                        }
                    }
                }
            }

            TecnicoListScreen(tecnicoList = uiState.tecnicos)
        }
    }
}


@Composable
fun TecnicoListScreen(tecnicoList: List<TecnicoEntity>) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Lista de Técnicos", style = MaterialTheme.typography.headlineSmall)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(tecnicoList) { tecnico ->
                TecnicoRow(tecnico)
            }
        }
    }
}

@Composable
private fun TecnicoRow(tecnico: TecnicoEntity) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
        Text(
            modifier = Modifier.weight(1f),
            text = tecnico.tecnicoId?.toString() ?: "",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier.weight(2f),
            text = tecnico.tecnico,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier.weight(2f),
            text = "$${tecnico.sueldo}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
    Divider()
}
