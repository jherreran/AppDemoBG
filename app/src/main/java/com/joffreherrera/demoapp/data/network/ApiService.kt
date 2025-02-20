package com.joffreherrera.demoapp.data.network

import com.joffreherrera.demoapp.data.model.Product
import com.joffreherrera.demoapp.data.model.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Query

interface ApiService {

    @GET("api/Products")
    suspend fun getProducts(
        @Query("pageNumber") page: Int,
        @Query("pageSize") pageSize: Int
    ): ProductResponse

    @GET("api/Products/search")
    suspend fun searchProducts(
        @Query("pageNumber") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("query") query: String
    ): ProductResponse

    @GET("api/Products/{id}")
    suspend fun getProductDetail(@Path("id") id: Int): Product

    @POST("api/Products/addWishlist/{id}")
    suspend fun addToWishlist(@Path("id") id: Int): retrofit2.Response<Unit>

    @POST("api/Products/removeWishlist/{id}")
    suspend fun removeFromWishlist(@Path("id") id: Int): retrofit2.Response<Unit>

    @GET("api/Products/Wishlist")
    suspend fun getWishlist(): List<Product>
}
