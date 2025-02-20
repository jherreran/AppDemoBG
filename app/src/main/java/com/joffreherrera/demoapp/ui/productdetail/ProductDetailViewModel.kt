package com.joffreherrera.demoapp.ui.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joffreherrera.demoapp.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel : ViewModel() {
    private val repository = ProductRepository()

    fun addToWishlist(
        productId: Int,
        onSuccess: () -> Unit = {},
        onError: (Throwable) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.addToWishlist(productId)
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}