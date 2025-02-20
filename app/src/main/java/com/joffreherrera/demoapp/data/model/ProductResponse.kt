package com.joffreherrera.demoapp.data.model

data class ProductResponse(
    val items: List<Product>,
    val totalItems: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int
)
