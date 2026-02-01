package com.example.avesretrofitjpc.model





data class Zona(
    var id: String = "",
    var nombre: String = "",
    var localizacion: String = "",
    var formaciones_principales: String = "",
    var presentacion: String = "",
    var geom_lat: String = "",
    var geom_lon: String = ""
)

data class Recurso(
    var id: String = "",
    var zona: String = "",
    var titulo: String = "",
    var url: String = ""
)

data class Usuario(
    var id: String = "",
    var nick: String = "",
    var pass: String = ""
)

data class Comentario(
    var id: String = "",
    var fecha: String = "",
    var comentario: String = "",
    var nick: String = ""
)

data class Respuesta(
    val zonas: List<Zona>? = null,
    val recursos: List<Recurso>? = null,
    val usuarios: List<Usuario>? = null,
    val usuario: Usuario? = null,
    val comentarios: List<Comentario>? = null,
    val msg: String? = null
)