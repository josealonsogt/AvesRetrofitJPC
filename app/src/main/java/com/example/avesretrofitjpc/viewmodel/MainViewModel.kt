package com.example.avesretrofitjpc.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.avesretrofitjpc.api.MainRepository
import com.example.avesretrofitjpc.model.Comentario
import com.example.avesretrofitjpc.model.Recurso
import com.example.avesretrofitjpc.model.Usuario
import com.example.avesretrofitjpc.model.Zona
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    // --- ESTADOS DE LA UI ---
    var zonas by mutableStateOf<List<Zona>>(emptyList())
        private set

    var recursos by mutableStateOf<List<Recurso>>(emptyList())
        private set

    var comentarios by mutableStateOf<List<Comentario>>(emptyList())
        private set

    var usuario by mutableStateOf<Usuario?>(null)
        private set

    // Selecciones actuales para navegación
    var zonaSeleccionada by mutableStateOf<Zona?>(null)
        private set

    var recursoSeleccionado by mutableStateOf<Recurso?>(null)
        private set

    // --- FUNCIONES DE CARGA DE DATOS ---

    fun getZonas() {
        viewModelScope.launch {
            zonas = repository.getZonas()
        }
    }

    fun getRecursos(idzona: String) {
        viewModelScope.launch {
            recursos = repository.getRecursos(idzona)
        }
    }

    fun getComentarios(idrecurso: String) {
        viewModelScope.launch {
            comentarios = repository.getComentarios(idrecurso)
        }
    }

    // --- GESTIÓN DE USUARIO ---

    fun getUsuario(nick: String, pass: String, onResult: (Usuario?) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUsuario(nick, pass)
            usuario = user
            onResult(user)
        }
    }

    // Función auxiliar para recuperar usuario desde SharedPreferences al abrir la app
    fun actualizarUsuario(user: Usuario?) {
        usuario = user
    }

    // --- ACCIONES ---

    fun insertComentario(
        idusuario: String,
        idrecurso: String,
        fecha: String,
        comentario: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val exito = repository.insertComentario(idusuario, idrecurso, fecha, comentario)
            if (exito) {
                // Si se insertó correctamente, recargamos la lista para ver el nuevo comentario
                getComentarios(idrecurso)
            }
            onResult(exito)
        }
    }

    fun seleccionarZona(zona: Zona) {
        zonaSeleccionada = zona
    }

    fun seleccionarRecurso(recurso: Recurso) {
        recursoSeleccionado = recurso
    }
}