package com.example.avesretrofitjpc.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.avesretrofitjpc.model.Zona
import com.example.avesretrofitjpc.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZonasScreen(
    viewModel: MainViewModel,
    onZonaClick: (Zona) -> Unit
) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences("AvesPrefs", Context.MODE_PRIVATE)

    // Estados para el diálogo de login
    var showLoginDialog by remember { mutableStateOf(false) }
    var nick by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Cargar usuario de SharedPreferences al inicio
    LaunchedEffect(Unit) {
        viewModel.getZonas()
        val savedNick = sharedPrefs.getString("nick", null)
        val savedId = sharedPrefs.getString("userId", null)
        if (savedNick != null && savedId != null) {
            viewModel.actualizarUsuario(
                com.example.avesretrofitjpc.model.Usuario(
                    id = savedId,
                    nick = savedNick,
                    pass = ""
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (viewModel.usuario != null)
                            "Aves - ${viewModel.usuario?.nick}"
                        else
                            "Aves de España"
                    )
                },
                actions = {
                    IconButton(onClick = { showLoginDialog = true }) {
                        Icon(Icons.Default.Person, contentDescription = "Login")
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
            items(viewModel.zonas) { zona ->
                ZonaItem(
                    zona = zona,
                    onClick = {
                        viewModel.seleccionarZona(zona)
                        onZonaClick(zona)
                    }
                )
            }
        }
    }

    // Diálogo de Login/Registro
    if (showLoginDialog) {
        AlertDialog(
            onDismissRequest = { showLoginDialog = false },
            title = { Text("Login / Registro") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nick,
                        onValueChange = { nick = it },
                        label = { Text("Nick") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pass,
                        onValueChange = { pass = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
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
                        if (nick.isNotBlank() && pass.isNotBlank()) {
                            viewModel.getUsuario(nick, pass) { usuario ->
                                if (usuario != null) {
                                    // Guardar en SharedPreferences
                                    sharedPrefs.edit()
                                        .putString("nick", usuario.nick)
                                        .putString("userId", usuario.id)
                                        .apply()
                                    showLoginDialog = false
                                    errorMessage = ""
                                } else {
                                    errorMessage = "Error en login/registro"
                                }
                            }
                        } else {
                            errorMessage = "Completa todos los campos"
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLoginDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ZonaItem(
    zona: Zona,
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
                text = zona.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = zona.localizacion,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

