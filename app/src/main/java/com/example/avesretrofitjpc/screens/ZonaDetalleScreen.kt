package com.example.avesretrofitjpc.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.avesretrofitjpc.model.Zona
import com.example.avesretrofitjpc.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZonaDetalleScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onRecursosClick: () -> Unit
) {
    val zona = viewModel.zonaSeleccionada

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(zona?.nombre ?: "Detalle Zona") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    zona?.let {
                        viewModel.getRecursos(it.id)
                        onRecursosClick()
                    }
                }
            ) {
                Icon(Icons.Default.List, contentDescription = "Ver Recursos")
            }
        }
    ) { paddingValues ->
        zona?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Nombre
                Text(
                    text = it.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Localización
                DetailSection(titulo = "Localización", contenido = it.localizacion)

                Spacer(modifier = Modifier.height(12.dp))

                // Formaciones principales
                DetailSection(titulo = "Formaciones Principales", contenido = it.formaciones_principales)

                Spacer(modifier = Modifier.height(12.dp))

                // Presentación
                DetailSection(titulo = "Presentación", contenido = it.presentacion)

                Spacer(modifier = Modifier.height(12.dp))

                // Coordenadas
                DetailSection(
                    titulo = "Coordenadas",
                    contenido = "Lat: ${it.geom_lat}, Lon: ${it.geom_lon}"
                )
            }
        }
    }
}

@Composable
fun DetailSection(titulo: String, contenido: String) {
    Column {
        Text(
            text = titulo,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = contenido,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

