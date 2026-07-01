package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.data.*
import com.example.ui.*
import com.example.ui.theme.*
import com.example.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialise local Room database & Repository mapping
        val database = PrimarkDatabase.getDatabase(applicationContext)
        val dao = database.primarkDao()
        val repository = PrimarkRepository(dao)

        // 2. Custom Factory injection for MVVM VM construction
        val factory = PrimarkViewModelFactory(application, repository)
        val viewModel = ViewModelProvider(this, factory)[PrimarkViewModel::class.java]

        setContent {
            MyApplicationTheme {
                MainAppContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContainer(viewModel: PrimarkViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val basketItems by viewModel.basketItems.collectAsState()
    val basketCount = basketItems.sumOf { it.quantity }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            // Standard M3 Bottom Navigation pill indicators
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .testTag("bottom_nav_bar"),
                containerColor = SurfaceCard,
                tonalElevation = 8.dp
            ) {
                // Item 1: Home
                val isHomeActive = currentRoute == "home"
                NavigationBarItem(
                    selected = isHomeActive,
                    onClick = {
                        if (!isHomeActive) {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = false }
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isHomeActive) Icons.Filled.Home else Icons.Outlined.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimarkBlueDark,
                        selectedTextColor = PrimarkBlueDark,
                        indicatorColor = PrimarkBlueLight
                    ),
                    modifier = Modifier.testTag("nav_home")
                )

                // Item 2: Shop
                val isShopActive = currentRoute?.startsWith("shop") == true
                NavigationBarItem(
                    selected = isShopActive,
                    onClick = {
                        if (!isShopActive) {
                            navController.navigate("shop/All") {
                                popUpTo("home")
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isShopActive) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                            contentDescription = "Shop"
                        )
                    },
                    label = { Text("Shop") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimarkBlueDark,
                        selectedTextColor = PrimarkBlueDark,
                        indicatorColor = PrimarkBlueLight
                    ),
                    modifier = Modifier.testTag("nav_shop")
                )

                // Item 3: Store Mode
                val isStoreActive = currentRoute == "store"
                NavigationBarItem(
                    selected = isStoreActive,
                    onClick = {
                        if (!isStoreActive) {
                            navController.navigate("store") {
                                popUpTo("home")
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isStoreActive) Icons.Filled.QrCodeScanner else Icons.Outlined.QrCodeScanner,
                            contentDescription = "Store Mode"
                        )
                    },
                    label = { Text("In-Store") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimarkBlueDark,
                        selectedTextColor = PrimarkBlueDark,
                        indicatorColor = PrimarkBlueLight
                    ),
                    modifier = Modifier.testTag("nav_store")
                )

                // Item 4: Wishlist / Favorites
                val isWishlistActive = currentRoute == "wishlist"
                NavigationBarItem(
                    selected = isWishlistActive,
                    onClick = {
                        if (!isWishlistActive) {
                            navController.navigate("wishlist") {
                                popUpTo("home")
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isWishlistActive) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Wishlist"
                        )
                    },
                    label = { Text("Wishlist") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimarkBlueDark,
                        selectedTextColor = PrimarkBlueDark,
                        indicatorColor = PrimarkBlueLight
                    ),
                    modifier = Modifier.testTag("nav_wishlist")
                )

                // Item 5: Basket / Cart
                val isBasketActive = currentRoute == "basket"
                NavigationBarItem(
                    selected = isBasketActive,
                    onClick = {
                        if (!isBasketActive) {
                            navController.navigate("basket") {
                                popUpTo("home")
                            }
                        }
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (basketCount > 0) {
                                    Badge(containerColor = PrimarkSaleRed) {
                                        Text(text = basketCount.toString(), color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isBasketActive) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                                contentDescription = "Basket"
                            )
                        }
                    },
                    label = { Text("Basket") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimarkBlueDark,
                        selectedTextColor = PrimarkBlueDark,
                        indicatorColor = PrimarkBlueLight
                    ),
                    modifier = Modifier.testTag("nav_basket")
                )

                // Item 6: Account & Admin Launcher
                val isAccountActive = currentRoute == "account"
                NavigationBarItem(
                    selected = isAccountActive,
                    onClick = {
                        if (!isAccountActive) {
                            navController.navigate("account") {
                                popUpTo("home")
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (isAccountActive) Icons.Filled.Person else Icons.Outlined.Person,
                            contentDescription = "Account"
                        )
                    },
                    label = { Text("Account") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimarkBlueDark,
                        selectedTextColor = PrimarkBlueDark,
                        indicatorColor = PrimarkBlueLight
                    ),
                    modifier = Modifier.testTag("nav_account")
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Home screen route
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToShop = { category ->
                        navController.navigate("shop/$category")
                    },
                    onProductClick = { prod ->
                        viewModel.selectProduct(prod)
                        navController.navigate("product_detail/${prod.id}")
                    },
                    onNavigateToStoreMode = {
                        navController.navigate("store")
                    }
                )
            }

            // Shop screen route with dynamic category argument
            composable(
                route = "shop/{category}",
                arguments = listOf(navArgument("category") { type = NavType.StringType })
            ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: "All"
                ShopScreen(
                    viewModel = viewModel,
                    initialCategory = category,
                    onProductClick = { prod ->
                        viewModel.selectProduct(prod)
                        navController.navigate("product_detail/${prod.id}")
                    }
                )
            }

            // In-store helper route
            composable("store") {
                StoreModeScreen(
                    viewModel = viewModel,
                    onNavigateToProduct = { prod ->
                        viewModel.selectProduct(prod)
                        navController.navigate("product_detail/${prod.id}")
                    }
                )
            }

            // Wishlist route
            composable("wishlist") {
                WishlistScreen(
                    viewModel = viewModel,
                    onNavigateToProduct = { prod ->
                        viewModel.selectProduct(prod)
                        navController.navigate("product_detail/${prod.id}")
                    },
                    onNavigateToShop = {
                        navController.navigate("shop/All")
                    }
                )
            }

            // Basket route
            composable("basket") {
                BasketScreen(
                    viewModel = viewModel,
                    onNavigateToProduct = { prod ->
                        viewModel.selectProduct(prod)
                        navController.navigate("product_detail/${prod.id}")
                    },
                    onNavigateToShop = {
                        navController.navigate("shop/All")
                    }
                )
            }

            // User Profile Account & Staff Admin route
            composable("account") {
                AccountAndAdminScreen(
                    viewModel = viewModel,
                    onNavigateToProduct = { prod ->
                        viewModel.selectProduct(prod)
                        navController.navigate("product_detail/${prod.id}")
                    }
                )
            }

            // Product Detail screen route
            composable(
                route = "product_detail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                val products by viewModel.allProducts.collectAsState()
                val selectedProd = products.find { it.id == productId }

                if (selectedProd != null) {
                    ProductDetailPage(
                        viewModel = viewModel,
                        product = selectedProd,
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Loading product data...")
                    }
                }
            }
        }
    }
}
