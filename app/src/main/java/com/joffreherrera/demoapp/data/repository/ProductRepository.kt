package com.joffreherrera.demoapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.joffreherrera.demoapp.data.model.Product
import com.joffreherrera.demoapp.data.network.ApiClient
import com.joffreherrera.demoapp.data.paging.ProductPagingSource
import kotlinx.coroutines.flow.Flow

class ProductRepository {
    private val apiService = ApiClient.apiService

    fun getPagedProducts(query: String? = null): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = 50,
                initialLoadSize = 50,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ProductPagingSource(apiService, query) }
        ).flow
    }

    suspend fun addToWishlist(productId: Int) {
        val response = apiService.addToWishlist(productId)
        if (!response.isSuccessful) {
            throw Exception("Error al agregar el producto a deseados")
        }
    }

    suspend fun removeFromWishlist(productId: Int) {
        val response = apiService.removeFromWishlist(productId)
        if (!response.isSuccessful) {
            throw Exception("Error al remover el producto de deseados")
        }
    }

    suspend fun getWishlist(): List<Product> = apiService.getWishlist()

}
