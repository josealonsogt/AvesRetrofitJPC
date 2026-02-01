package com.example.avesretrofitjpc.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.avesretrofitjpc.api.MainRepository
import com.example.avesretrofitjpc.model.Comentario
import com.example.avesretrofitjpc.model.Recurso
import com.example.avesretrofitjpc.model.Usuario
import com.example.avesretrofitjpc.model.Zona
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DataViewModel {

    private var repository: MainRepository = MainRepository()

    // Obtener lista de zonas
    fun getZonas(): MutableLiveData<List<Zona>> {
        val zonas = MutableLiveData<List<Zona>>()
        GlobalScope.launch(Main) {
            zonas.value = repository.getZonas()
        }
        return zonas
    }

    // Obtener recursos de una zona
    fun getRecursos(idzona: String): MutableLiveData<List<Recurso>> {
        val recursos = MutableLiveData<List<Recurso>>()
        GlobalScope.launch(Main) {
            recursos.value = repository.getRecursos(idzona)
        }
        return recursos
    }

    // Login de usuario
    fun getUsuario(nick: String, pass: String): MutableLiveData<Usuario?> {
        val usuario = MutableLiveData<Usuario?>()
        GlobalScope.launch(Main) {
            usuario.value = repository.getUsuario(nick, pass)
        }
        return usuario
    }

    // Obtener comentarios de un recurso
    fun getComentarios(idrecurso: String): MutableLiveData<List<Comentario>> {
        val comentarios = MutableLiveData<List<Comentario>>()
        GlobalScope.launch(Main) {
            comentarios.value = repository.getComentarios(idrecurso)
        }
        return comentarios
    }

    // Insertar nuevo comentario
    fun insertComentario(
        idusuario: String,
        idrecurso: String,
        fecha: String,
        comentario: String
    ): MutableLiveData<Boolean> {
        val resultado = MutableLiveData<Boolean>()
        GlobalScope.launch(Main) {
            resultado.value = repository.insertComentario(idusuario, idrecurso, fecha, comentario)
        }
        return resultado
    }
}
