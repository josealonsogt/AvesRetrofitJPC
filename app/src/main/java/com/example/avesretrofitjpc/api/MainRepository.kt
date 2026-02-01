package com.example.avesretrofitjpc.api

import com.example.avesretrofitjpc.model.Comentario
import com.example.avesretrofitjpc.model.Recurso
import com.example.avesretrofitjpc.model.Usuario
import com.example.avesretrofitjpc.model.Zona

class MainRepository {

    private val apiService = WebAccess.avesService

    // Obtener lista de zonas. Si falla, devuelve lista vacía.
    suspend fun getZonas(): List<Zona> {
        return try {
            val response = apiService.getZonas()
            if (response.isSuccessful) {
                response.body()?.zonas ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Obtener recursos de una zona específica.
    suspend fun getRecursos(idzona: String): List<Recurso> {
        return try {
            val response = apiService.getRecursos(idzona)
            if (response.isSuccessful) {
                response.body()?.recursos ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    /*
     * Lógica Especial de Login/Registro:
     * 1. Intenta loguearse.
     * 2. Si el usuario no existe (devuelve null), intenta registrarlo.
     * 3. Si se registra con éxito, se loguea automáticamente para obtener el ID real.
     */
    suspend fun getUsuario(nick: String, pass: String): Usuario? {
        var usuario: Usuario? = null
        try {
            // Paso 1: Intentar Login
            val responseLogin = apiService.loginUsuario(nick, pass)
            if (responseLogin.isSuccessful) {
                usuario = responseLogin.body()?.usuario
            }

            // Paso 2: Si no existe, Intentar Registro
            if (usuario == null) {
                val responseRegistro = apiService.registrarUsuario(nick, pass)

                // Paso 3: Si registro OK, reloguear para obtener ID
                if (responseRegistro.isSuccessful) {
                    val login2 = apiService.loginUsuario(nick, pass)
                    usuario = login2.body()?.usuario
                }
            }
        } catch (e: Exception) {
            // Si hay error de red, devolvemos null
            e.printStackTrace()
        }
        return usuario
    }

    // Obtener comentarios de un recurso.
    suspend fun getComentarios(idrecurso: String): List<Comentario> {
        return try {
            val response = apiService.getComentarios(idrecurso)
            if (response.isSuccessful) {
                response.body()?.comentarios ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Insertar comentario. Devuelve true si fue exitoso.
    suspend fun insertComentario(
        idusuario: String,
        idrecurso: String,
        fecha: String,
        comentario: String
    ): Boolean {
        return try {
            val response = apiService.insertComentario(idrecurso, idusuario, fecha, comentario)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}