package com.joffreherrera.demoapp.ui.wishlist

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.joffreherrera.demoapp.R
import com.joffreherrera.demoapp.data.model.Product
import com.joffreherrera.demoapp.ui.common.ProgressDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    wishlistViewModel: WishlistViewModel = viewModel(),
    onBack: () -> Unit
) {
    val wishlist by wishlistViewModel.wishlist
    val isDeleting by wishlistViewModel.isDeleting
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        wishlistViewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Usamos Scaffold para definir la estructura
    Scaffold(
        topBar = {
            // Barra superior con título y botón de retroceso
            TopAppBar(
                title = { Text("Lista de Deseados") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Contenido principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Espacio entre la barra y el contenido
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(wishlist) { product ->
                        WishlistItem(product = product, onRemove = {
                            wishlistViewModel.removeFromWishlist(product.id)
                        })
                    }
                }
            }
            // Muestra el ProgressDialog si se está eliminando un ítem
            if (isDeleting) {
                ProgressDialog()
            }
        }
    }
}


@Composable
fun WishlistItem(product: Product, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            val painter = rememberAsyncImagePainter(product.imageUrl)
            Image(
                painter = painter,
                contentDescription = product.name,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "$${product.price}", style = MaterialTheme.typography.bodyMedium)
            }
            // Botón con ícono de tacho de basura para remover
            IconButton(onClick = onRemove) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_24),
                    contentDescription = "Remover de deseados"
                )
            }
        }
    }
}