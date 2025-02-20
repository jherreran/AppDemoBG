package com.joffreherrera.demoapp.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.joffreherrera.demoapp.data.model.Product
import com.joffreherrera.demoapp.data.model.ProductResponse
import com.joffreherrera.demoapp.data.network.ApiService

class ProductPagingSource(
    private val apiService: ApiService,
    private val query: String? = null
) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key ?: 1
        Log.d("ProductPagingSource", "Fetching page $page with query: $query")
        return try {
            val response: ProductResponse = if (!query.isNullOrEmpty()) {
                apiService.searchProducts(page, params.loadSize, query)
            } else {
                apiService.getProducts(page, params.loadSize)
            }
            val products = response.items
            Log.d("ProductPagingSource", "Received ${products.size} products on requested page $page")

            val computedTotalPages = if (response.pageSize > 0) {
                (response.totalItems + response.pageSize - 1) / response.pageSize
            } else {
                1
            }
            Log.d("ProductPagingSource", "Computed total pages: $computedTotalPages")

            val nextKey = if (page < computedTotalPages) page + 1 else null

            LoadResult.Page(
                data = products,
                prevKey = if (page == 1) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.e("ProductPagingSource", "Error fetching data", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
