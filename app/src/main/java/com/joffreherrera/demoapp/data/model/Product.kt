package com.joffreherrera.demoapp.data.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val isDesired: Boolean
)