package com.example.kamerasistemleri

data class Camera(
    val id: Int = 0,
    val name: String,
    val ip: String,
    val port: Int,
    val username: String,
    val password: String,
    val streamUrl: String
)
