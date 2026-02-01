package com.example.avesretrofitjpc.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.avesretrofitjpc.model.Recurso
import com.example.avesretrofitjpc.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecursosScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit,
    onRecursoClick: (Recurso) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recursos - ${viewModel.zonaSeleccionada?.nombre ?: ""}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(viewModel.recursos) { recurso ->
                RecursoItem(
                    recurso = recurso,
                    onClick = {
                        viewModel.seleccionarRecurso(recurso)
                        viewModel.getComentarios(recurso.id)
                        onRecursoClick(recurso)
                    }
                )
            }
        }
    }
}

@Composable
fun RecursoItem(
    recurso: Recurso,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = recurso.titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

