package com.joffreherrera.demoapp.ui.productlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.joffreherrera.demoapp.data.model.Product
import com.joffreherrera.demoapp.ui.common.ProgressDialog
import com.joffreherrera.demoapp.ui.shared.SharedProductViewModel

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    sharedProductViewModel: SharedProductViewModel,
    onNavigateToDetail: () -> Unit,
    onNavigateToWishlist: () -> Unit
) {
    val currentQuery by viewModel.query.collectAsState()
    val lazyPagingItems = viewModel.pagedProducts.collectAsLazyPagingItems()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(
                query = currentQuery,
                onQueryChanged = { newQuery ->
                    viewModel.updateQuery(newQuery)
                }
            )
            Button(
                onClick = onNavigateToWishlist,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Ver Lista de Deseados")
            }
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(lazyPagingItems) { product ->
                    product?.let {
                        ProductItem(product = it, onProductClick = { selectedProduct ->
                            sharedProductViewModel.selectProduct(selectedProduct)
                            onNavigateToDetail()
                        })
                    }
                }
                lazyPagingItems.apply {
                    when (loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                        is LoadState.Error -> {
                            val e = loadState.append as LoadState.Error
                            item {
                                Text(
                                    text = "Error: ${e.error.localizedMessage}",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        else -> { }
                    }
                }
            }
        }
        if(lazyPagingItems.loadState.refresh is LoadState.Loading) {
            ProgressDialog()
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        label = { Text("Buscar producto") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun ProductItem(product: Product, onProductClick: (Product) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 3.dp)
            .fillMaxWidth()
            .clickable { onProductClick(product) }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            val painter = rememberAsyncImagePainter(product.imageUrl)
            Image(
                painter = painter,
                contentDescription = product.name,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = product.name)
                Text(text = "$${product.price}")
            }
        }
    }
}
