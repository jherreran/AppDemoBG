package com.joffreherrera.demoapp.ui.wishlist

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joffreherrera.demoapp.data.model.Product
import com.joffreherrera.demoapp.data.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WishlistViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _wishlist = mutableStateOf<List<Product>>(emptyList())
    val wishlist get() = _wishlist

    private val _isDeleting = mutableStateOf(false)
    val isDeleting get() = _isDeleting

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage = _toastMessage.asSharedFlow()

    init {
        fetchWishlist()
    }

    fun fetchWishlist() {
        viewModelScope.launch {
            try {
                val list = repository.getWishlist()
                _wishlist.value = list.toList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeFromWishlist(productId: Int, onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            try {
                _isDeleting.value = true
                repository.removeFromWishlist(productId)
                delay(1000)
                fetchWishlist()
                _isDeleting.value = false
                _toastMessage.emit("Producto eliminado")
                onSuccess()
            } catch (e: Exception) {
                _isDeleting.value = false
                onError(e)
            }
        }
    }
}
