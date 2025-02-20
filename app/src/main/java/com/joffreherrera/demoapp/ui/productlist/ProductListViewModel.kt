package com.joffreherrera.demoapp.ui.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.joffreherrera.demoapp.data.model.Product
import com.joffreherrera.demoapp.data.repository.ProductRepository
import kotlinx.coroutines.flow.*

class ProductListViewModel : ViewModel() {
    private val repository = ProductRepository()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    fun updateQuery(newQuery: String) {
        _query.value = newQuery
    }

    val pagedProducts: Flow<PagingData<Product>> = _query
        .debounce(1000)
        .distinctUntilChanged()
        .flatMapLatest { currentQuery ->
            repository.getPagedProducts(if (currentQuery.isEmpty()) null else currentQuery)
        }
        .cachedIn(viewModelScope)
}
