package com.example.avesretrofitjpc.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.avesretrofitjpc.model.Comentario
import com.example.avesretrofitjpc.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecursoDetalleScreen(
    viewModel: MainViewModel,
    onBackClick: () -> Unit
) {
    val recurso = viewModel.recursoSeleccionado
    val usuario = viewModel.usuario

    var showComentarioDialog by remember { mutableStateOf(false) }
    var nuevoComentario by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recurso?.titulo ?: "Detalle Recurso") },
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
            if (usuario != null) {
                FloatingActionButton(
                    onClick = { showComentarioDialog = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Comentario")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Imagen del recurso
            item {
                recurso?.let {
                    AsyncImage(
                        model = it.url,
                        contentDescription = it.titulo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = it.titulo,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Comentarios",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Lista de comentarios
            items(viewModel.comentarios) { comentario ->
                ComentarioItem(comentario = comentario)
            }

            // Mensaje si no hay comentarios
            if (viewModel.comentarios.isEmpty()) {
                item {
                    Text(
                        text = "No hay comentarios todavía",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (showComentarioDialog) {
        AlertDialog(
            onDismissRequest = { showComentarioDialog = false },
            title = { Text("Nuevo Comentario") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nuevoComentario,
                        onValueChange = { nuevoComentario = it },
                        label = { Text("Tu comentario") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nuevoComentario.isNotBlank() && usuario != null && recurso != null) {
                            val fecha = Date()
                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val fechaTXT = sdf.format(fecha)

                            android.util.Log.d("AVES", "Enviando: UserID=${usuario.id}, RecursoID=${recurso.id}, Fecha=$fechaTXT")

                            viewModel.insertComentario(
                                idusuario = usuario.id,
                                idrecurso = recurso.id,
                                fecha = fechaTXT,
                                comentario = nuevoComentario
                            ) { success ->
                                if (success) {
                                    showComentarioDialog = false
                                    nuevoComentario = ""
                                    errorMessage = ""
                                } else {
                                    errorMessage = "Error al enviar"
                                }
                            }
                        } else {
                            errorMessage = "Escribe un comentario"
                        }
                    }
                ) {
                    Text("Enviar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showComentarioDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ComentarioItem(comentario: Comentario) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comentario.nick,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = comentario.fecha,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comentario.comentario,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
