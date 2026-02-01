package com.example.avesretrofitjpc.api

import com.example.avesretrofitjpc.model.Respuesta
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AvesService {

    // --- ZONAS ---
    @GET("zonas")
    suspend fun getZonas(): Response<Respuesta>

    @GET("zona/{idzona}/recursos")
    suspend fun getRecursos(@Path("idzona") idzona: String): Response<Respuesta>

    // --- USUARIOS ---

    // Login: Se usa GET con @Query porque los datos van en la URL
    @GET("usuario")
    suspend fun loginUsuario(
        @Query("nick") nick: String,
        @Query("pass") pass: String
    ): Response<Respuesta>

    // Registro: Se usa POST con @Field porque los datos van en el cuerpo
    @FormUrlEncoded
    @POST("usuario")
    suspend fun registrarUsuario(
        @Field("nick") nick: String,
        @Field("pass") pass: String
    ): Response<Respuesta>

    // --- COMENTARIOS ---
    @GET("recurso/{idrecurso}/comentarios")
    suspend fun getComentarios(@Path("idrecurso") idrecurso: String): Response<Respuesta>


    @FormUrlEncoded
    @POST("recurso/{idrecurso}/comentario")
    suspend fun insertComentario(
        @Path("idrecurso") idrecurso: String,
        @Field("usuario") idusuario: String, // Clave corregida
        @Field("fecha") fecha: String,
        @Field("comentario") comentario: String
    ): Response<Respuesta>
}