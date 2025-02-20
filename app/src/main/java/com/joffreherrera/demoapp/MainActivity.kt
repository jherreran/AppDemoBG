package com.joffreherrera.demoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joffreherrera.demoapp.ui.productdetail.ProductDetailScreen
import com.joffreherrera.demoapp.ui.productlist.ProductListScreen
import com.joffreherrera.demoapp.ui.productlist.ProductListViewModel
import com.joffreherrera.demoapp.ui.shared.SharedProductViewModel
import com.joffreherrera.demoapp.ui.theme.DemoAppTheme
import com.joffreherrera.demoapp.ui.wishlist.WishlistScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoAppTheme {
                AppNavigator()
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val productListViewModel: ProductListViewModel = viewModel()
    val sharedProductViewModel: SharedProductViewModel = viewModel()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "productList",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("productList") {
                ProductListScreen(
                    viewModel = productListViewModel,
                    sharedProductViewModel = sharedProductViewModel,
                    onNavigateToDetail = { navController.navigate("productDetail") },
                    onNavigateToWishlist = { navController.navigate("wishlist") }
                )
            }
            composable("productDetail") {
                val product = sharedProductViewModel.selectedProduct.collectAsState().value
                if (product != null) {
                    ProductDetailScreen(product = product, onBack = { navController.popBackStack() })
                }
            }
            composable("wishlist") {
                WishlistScreen(
                    wishlistViewModel = viewModel(),
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
