package com.example.ui

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import com.example.ui.theme.*
import com.example.viewmodel.PrimarkViewModel

// ==========================================================
// 1. HOME SCREEN
// ==========================================================
@Composable
fun HomeScreen(
    viewModel: PrimarkViewModel,
    onNavigateToShop: (String) -> Unit,
    onProductClick: (Product) -> Unit,
    onNavigateToStoreMode: () -> Unit
) {
    val products by viewModel.allProducts.collectAsState()
    val wishlist by viewModel.wishlistItems.collectAsState()
    val activeStore by viewModel.activeStore.collectAsState()

    val trendingProducts = products.filter { it.isTrending }
    val newArrivals = products.filter { it.isNewArrival }
    val underTen = products.filter { it.isUnderTen || it.price < 10.0 }
    val seasonalDeals = products.filter { it.isSeasonal || it.isPromo }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // App header containing Brand Name and Active Store Locator Quick-select
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "PRIMARK",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = PrimarkBlue,
                                letterSpacing = (-1).sp
                            )
                            Text(
                                text = "Amazing Fashion, Amazing Prices",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLight,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Store quick-select card
                        Card(
                            onClick = onNavigateToStoreMode,
                            colors = CardDefaults.cardColors(containerColor = BgLight),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = "Active Store",
                                    tint = PrimarkBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = activeStore?.name?.substringBefore(" ") ?: "Find Store",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sleek Simulated Search Bar (opens shop search)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(BgLight)
                            .clickable { onNavigateToShop("All") }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = TextLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Search clothing, home, beauty...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextLight
                            )
                        }
                    }
                }
            }
        }

        // Hero Image Banner (generated via AI tool)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Primark Summer Campaign Hero Banner",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                // Overlay tint and promo highlights
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                                startY = 100f
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "NEW SUMMER ARRIVALS",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Vibrant fabrics, relaxed fits. From just £4.50!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }

        // Featured Categories Row
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                SectionHeader(title = "Featured Categories")
                Spacer(modifier = Modifier.height(12.dp))

                val categories = listOf(
                    "Women" to Icons.Default.Face,
                    "Men" to Icons.Default.DirectionsRun,
                    "Kids" to Icons.Default.ChildCare,
                    "Beauty" to Icons.Default.AutoAwesome,
                    "Home" to Icons.Default.Home
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(categories) { (name, icon) ->
                        Column(
                            modifier = Modifier
                                .clickable { onNavigateToShop(name) }
                                .testTag("home_category_$name"),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(SurfaceCard)
                                    .border(1.dp, BorderLight, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = name,
                                    tint = TextDark,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = name.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextLight,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }

        // Sleek Store Mode Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .testTag("home_store_mode_section")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Store Mode",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        // Green "Nearby" badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFDCFCE7)) // bg-green-100
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "NEARBY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D) // text-green-700
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Store locator helper block
                    Card(
                        onClick = onNavigateToStoreMode,
                        colors = CardDefaults.cardColors(containerColor = BgLight),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = PrimarkBlue,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = activeStore?.name ?: "London Oxford Street",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                Text(
                                    text = "Open until 10:00 PM • 0.4 miles away",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextLight
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "Go",
                                tint = TextLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Two grid buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Scan Product button
                        Card(
                            onClick = onNavigateToStoreMode,
                            colors = CardDefaults.cardColors(containerColor = BgLight),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCodeScanner,
                                    contentDescription = "Scan",
                                    tint = TextDark,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Scan Product",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }
                        }

                        // Stock Check button
                        Card(
                            onClick = onNavigateToStoreMode,
                            colors = CardDefaults.cardColors(containerColor = BgLight),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Inventory,
                                    contentDescription = "Stock Check",
                                    tint = TextDark,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Stock Check",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Seasonal Deals Horizontal Carousel
        item {
            if (seasonalDeals.isNotEmpty()) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    SectionHeader(
                        title = "Seasonal Promotional Deals",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(seasonalDeals) { prod ->
                            PrimarkProductCard(
                                product = prod,
                                isWishlisted = wishlist.any { it.productId == prod.id },
                                onProductClick = { onProductClick(prod) },
                                onWishlistClick = { viewModel.toggleWishlist(prod.id) },
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
            }
        }

        // Under £10 Highlights
        item {
            if (underTen.isNotEmpty()) {
                Column(modifier = Modifier.padding(top = 24.dp)) {
                    SectionHeader(
                        title = "Budget Highlights Under £10",
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(underTen) { prod ->
                            PrimarkProductCard(
                                product = prod,
                                isWishlisted = wishlist.any { it.productId == prod.id },
                                onProductClick = { onProductClick(prod) },
                                onWishlistClick = { viewModel.toggleWishlist(prod.id) },
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
            }
        }

        // Trending Products Grid
        item {
            Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 24.dp)) {
                SectionHeader(title = "Trending Now")
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        items(trendingProducts.chunked(2)) { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (prod in pair) {
                    PrimarkProductCard(
                        product = prod,
                        isWishlisted = wishlist.any { it.productId == prod.id },
                        onProductClick = { onProductClick(prod) },
                        onWishlistClick = { viewModel.toggleWishlist(prod.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (pair.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// ==========================================================
// 2. SHOP SCREEN (BROWSE & SEARCH & FILTER)
// ==========================================================
@Composable
fun ShopScreen(
    viewModel: PrimarkViewModel,
    initialCategory: String,
    onProductClick: (Product) -> Unit
) {
    val products by viewModel.allProducts.collectAsState()
    val wishlist by viewModel.wishlistItems.collectAsState()

    var selectedCategory by remember { mutableStateOf(initialCategory.ifBlank { "All" }) }
    var searchQuery by remember { mutableStateOf("") }
    var sortByPriceAsc by remember { mutableStateOf<Boolean?>(null) } // null = default, true = low-high, false = high-low
    var filterUnderTenOnly by remember { mutableStateOf(false) }

    val categories = listOf("All", "Women", "Men", "Kids", "Baby", "Home", "Accessories", "Beauty", "Sale")

    // Filtered & sorted products list
    val filteredProducts = products.filter { prod ->
        val categoryMatch = selectedCategory == "All" || prod.category.equals(selectedCategory, ignoreCase = true) ||
                (selectedCategory == "Sale" && prod.isPromo)
        val searchMatch = searchQuery.isBlank() || prod.name.contains(searchQuery, ignoreCase = true) ||
                prod.subCategory.contains(searchQuery, ignoreCase = true)
        val budgetMatch = !filterUnderTenOnly || prod.price < 10.0

        categoryMatch && searchMatch && budgetMatch
    }.let { list ->
        when (sortByPriceAsc) {
            true -> list.sortedBy { it.price }
            false -> list.sortedByDescending { it.price }
            null -> list
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .testTag("shop_screen")
    ) {
        // Upper search and brand headers
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                // Search bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search cheap fashion, bedding, beauty...", color = TextLight) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = PrimarkBlue) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextLight)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimarkBlue,
                        unfocusedBorderColor = BorderLight
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable category filter pills
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        val isSel = selectedCategory == cat
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimarkBlue,
                                selectedLabelColor = TextWhite
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSel,
                                borderColor = BorderLight,
                                selectedBorderColor = PrimarkBlue
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Sort and quick budget filter controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Sorting options
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Sort Price: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextLight,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(
                            onClick = {
                                sortByPriceAsc = if (sortByPriceAsc == true) false else if (sortByPriceAsc == false) null else true
                            }
                        ) {
                            Icon(
                                imageVector = when (sortByPriceAsc) {
                                    true -> Icons.Default.ArrowUpward
                                    false -> Icons.Default.ArrowDownward
                                    null -> Icons.Default.SwapVert
                                },
                                contentDescription = "Sort Icon",
                                tint = if (sortByPriceAsc != null) PrimarkBlue else TextLight,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        if (sortByPriceAsc != null) {
                            Text(
                                text = if (sortByPriceAsc == true) "Low to High" else "High to Low",
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimarkBlue,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Budget filter checkbox
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { filterUnderTenOnly = !filterUnderTenOnly }
                    ) {
                        Checkbox(
                            checked = filterUnderTenOnly,
                            onCheckedChange = { filterUnderTenOnly = it },
                            colors = CheckboxDefaults.colors(checkedColor = PrimarkBlue)
                        )
                        Text(
                            text = "Under £10 Only",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = if (filterUnderTenOnly) PrimarkBlue else TextLight
                        )
                    }
                }
            }
        }

        // Active Product Grid
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = "No products found",
                        tint = TextLight.copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Products Found",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "Try adjusting your search query, sorting, or category filter to find options.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextLight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredProducts) { prod ->
                    PrimarkProductCard(
                        product = prod,
                        isWishlisted = wishlist.any { it.productId == prod.id },
                        onProductClick = { onProductClick(prod) },
                        onWishlistClick = { viewModel.toggleWishlist(prod.id) }
                    )
                }
            }
        }
    }
}

// ==========================================================
// 3. PRODUCT DETAIL PAGE
// ==========================================================
@Composable
fun ProductDetailPage(
    viewModel: PrimarkViewModel,
    product: Product,
    onBackClick: () -> Unit
) {
    val wishlist by viewModel.wishlistItems.collectAsState()
    val activeStore by viewModel.activeStore.collectAsState()
    val isFav = wishlist.any { it.productId == product.id }

    val colors = product.getColorList()
    val sizes = product.getSizeList()

    var selectedSize by remember { mutableStateOf(sizes.firstOrNull() ?: "M") }
    var selectedColor by remember { mutableStateOf(colors.firstOrNull() ?: "White") }
    var quantity by remember { mutableStateOf(1) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .testTag("product_detail_${product.id}")
    ) {
        // Custom Back Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceCard)
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextDark)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(220.dp)
                )
            }

            Row {
                IconButton(onClick = { viewModel.toggleWishlist(product.id) }) {
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isFav) PrimarkSaleRed else TextDark
                    )
                }
                IconButton(
                    onClick = {
                        val shareText = "Check out this amazing value find at Primark: ${product.name} for only £${product.price}!"
                        Toast.makeText(context, "Link copied to share: $shareText", Toast.LENGTH_LONG).show()
                    }
                ) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = TextDark)
                }
            }
        }

        // Detailed Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Dynamic Illustration Card
            ProductImagePlaceholder(
                category = product.category,
                subCategory = product.subCategory,
                colors = product.colors,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceCard, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(20.dp)
            ) {
                // Category & Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${product.category} • ${product.subCategory}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextLight,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (product.isUnderTen) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = PrimarkYellowLight),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "UNDER £10 DEALS",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFB45309),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Price Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "£${"%.2f".format(product.price)}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = if (product.isPromo) PrimarkSaleRed else PrimarkBlueDark,
                        fontWeight = FontWeight.Black
                    )
                    if (product.isPromo) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "£${"%.2f".format(product.originalPrice)}",
                            style = TextStyle(
                                color = TextLight,
                                fontSize = 18.sp,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = PrimarkSaleRedLight),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "CLEARANCE PRICE",
                                style = MaterialTheme.typography.bodySmall,
                                color = PrimarkSaleRed,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Rating Details
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating Star",
                            tint = if (index < product.rating.toInt()) StarGolden else Color.LightGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${product.rating} / 5.0 (Customer Rating)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextLight,
                        fontWeight = FontWeight.Medium
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp), color = BorderLight)

                // Colors list
                if (colors.isNotEmpty()) {
                    Text(
                        text = "Select Colour: $selectedColor",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        for (colorName in colors) {
                            val isColorSel = selectedColor == colorName
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (colorName.lowercase()) {
                                            "white" -> Color.White
                                            "black" -> Color.Black
                                            "navy" -> Color(0xFF1E3A8A)
                                            "khaki" -> Color(0xFF4D7C0F)
                                            "grey", "charcoal" -> Color.Gray
                                            "blue", "mid blue", "dark blue" -> Color(0xFF2563EB)
                                            "red", "red tartan" -> Color(0xFFDC2626)
                                            "clear" -> Color(0xFFE2E8F0)
                                            "gold" -> Color(0xFFFBBF24)
                                            "tan brown" -> Color(0xFFB45309)
                                            "pink", "soft pink" -> Color(0xFFF472B6)
                                            "mint green" -> Color(0xFF34D399)
                                            else -> PrimarkBlue
                                        }
                                    )
                                    .border(
                                        width = if (isColorSel) 3.dp else 1.dp,
                                        color = if (isColorSel) PrimarkBlue else Color.LightGray,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = colorName }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sizes list
                if (sizes.isNotEmpty()) {
                    Text(
                        text = "Select Size: $selectedSize",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        for (sizeName in sizes) {
                            val isSizeSel = selectedSize == sizeName
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSizeSel) PrimarkBlue else Color.Transparent)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSizeSel) PrimarkBlue else BorderLight,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedSize = sizeName }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = sizeName,
                                    color = if (isSizeSel) TextWhite else TextDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quantity selector
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Quantity:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BgLight),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease")
                            }
                            Text(
                                text = quantity.toString(),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            IconButton(onClick = { quantity++ }) {
                                Icon(Icons.Default.Add, contentDescription = "Increase")
                            }
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp), color = BorderLight)

                // Stock Availability & selected store information
                Card(
                    colors = CardDefaults.cardColors(containerColor = PrimarkBlueLight),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Store, contentDescription = "In Store Locator", tint = PrimarkBlueDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "In-Store Availability Support",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimarkBlueDark
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Checking stock at ${activeStore?.name ?: "Oxford Street Flagship"}:",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        // Real status check
                        val isAvailable = (product.id % 4 != 0) // Simulate slightly different stock
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(if (isAvailable) Color(0xFF10B981) else Color.Red, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isAvailable) "In Stock (Click & Collect pickup within 1 hr)" else "Out of stock locally (Avail. Online)",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isAvailable) Color(0xFF047857) else Color.Red
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Product Description
                Text(
                    text = "Product Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextLight,
                    lineHeight = 20.sp
                )
            }
        }

        // Bottom Fixed Cart Actions
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Wishlist quick button
                OutlinedButton(
                    onClick = {
                        viewModel.addToBasket(product, selectedSize, selectedColor, quantity)
                        Toast.makeText(context, "Added $quantity item(s) to basket!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimarkBlue),
                    border = BorderStroke(1.5.dp, PrimarkBlue),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("add_to_basket_btn")
                ) {
                    Icon(Icons.Default.AddShoppingCart, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Add to Basket", fontWeight = FontWeight.Bold)
                }

                // Click & Collect reservation
                Button(
                    onClick = {
                        viewModel.addToBasket(product, selectedSize, selectedColor, quantity)
                        viewModel.simulateInStoreReservation()
                        Toast.makeText(context, "Click & Collect Reservation Created!", Toast.LENGTH_LONG).show()
                        onBackClick() // Back to shop
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("click_collect_btn")
                ) {
                    Icon(Icons.Default.Store, contentDescription = "Reserve")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reserve Instant", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==========================================================
// 4. BASKET (SHOPPING CART)
// ==========================================================
@Composable
fun BasketScreen(
    viewModel: PrimarkViewModel,
    onNavigateToProduct: (Product) -> Unit,
    onNavigateToShop: () -> Unit
) {
    val basketItems by viewModel.basketItems.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val subtotal by viewModel.basketTotal.collectAsState()
    val appliedPromo by viewModel.appliedPromoCode.collectAsState()
    val discountedTotal by viewModel.basketDiscountedTotal.collectAsState()

    var promoInput by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .testTag("basket_screen")
    ) {
        // Simple Top Header
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            border = BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "My Shopping Basket",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = PrimarkBlue
                )
            }
        }

        if (basketItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Empty Basket",
                        tint = TextLight.copy(alpha = 0.5f),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Basket is Empty",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Fill it with incredibly affordable fashion, accessories, bedding and beauty specials!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextLight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onNavigateToShop,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Start Browsing Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                // Cart Items scroll list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(basketItems) { item ->
                        val product = allProducts.find { it.id == item.productId }
                        if (product != null) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                border = BorderStroke(1.dp, BorderLight),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Image
                                    ProductImagePlaceholder(
                                        category = product.category,
                                        subCategory = product.subCategory,
                                        colors = product.colors,
                                        modifier = Modifier
                                            .size(70.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )

                                    Spacer(modifier = Modifier.width(12.dp))

                                    // Details
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = product.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "Size: ${item.selectedSize} | Colour: ${item.selectedColor}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextLight
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "£${"%.2f".format(product.price)} each",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = PrimarkBlueDark
                                        )
                                    }

                                    // Action quantity modifier & delete buttons
                                    Column(horizontalAlignment = Alignment.End) {
                                        IconButton(
                                            onClick = { viewModel.removeBasketItem(item.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = PrimarkSaleRed, modifier = Modifier.size(18.dp))
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .background(BgLight, RoundedCornerShape(6.dp))
                                                .border(1.dp, BorderLight, RoundedCornerShape(6.dp))
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clickable { viewModel.updateBasketQuantity(item.id, item.quantity - 1) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("-", fontWeight = FontWeight.Bold)
                                            }
                                            Text(
                                                text = item.quantity.toString(),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clickable { viewModel.updateBasketQuantity(item.id, item.quantity + 1) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("+", fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Summary and Promotional codes container
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Apply Promo Code text field
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = promoInput,
                                onValueChange = { promoInput = it },
                                placeholder = { Text("Promo Code e.g. PRIMARK10", fontSize = 13.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimarkBlue,
                                    unfocusedBorderColor = BorderLight
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (promoInput.isNotBlank()) {
                                        val success = viewModel.applyPromo(promoInput)
                                        if (success) {
                                            Toast.makeText(context, "Promo Code applied successfully!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Invalid Promo Code! Try PRIMARK10", Toast.LENGTH_SHORT).show()
                                        }
                                        promoInput = ""
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue),
                                modifier = Modifier.height(50.dp)
                            ) {
                                Text("Apply")
                            }
                        }

                        if (appliedPromo != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocalOffer, contentDescription = "Offer", tint = Color(0xFF047857), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Discount Code Applied: $appliedPromo (-10%)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF047857),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "Remove",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PrimarkSaleRed,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { viewModel.clearPromo() }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Receipt calculations
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = TextLight)
                            Text("£${"%.2f".format(subtotal)}", style = MaterialTheme.typography.bodyMedium, color = TextDark)
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Click & Collect Pickup Fee", style = MaterialTheme.typography.bodyMedium, color = TextLight)
                            Text("FREE", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = BorderLight)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = TextDark)
                            Text(
                                text = "£${"%.2f".format(discountedTotal)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Black,
                                color = PrimarkBlueDark
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Checkout / reserve button
                        Button(
                            onClick = {
                                val ok = viewModel.simulateInStoreReservation()
                                if (ok) {
                                    Toast.makeText(context, "Click & Collect Reservation Confirmed!", Toast.LENGTH_LONG).show()
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("checkout_reserve_btn")
                        ) {
                            Icon(Icons.Default.FlashOn, contentDescription = "Instant reservation checkout")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Confirm Click & Collect Reservation", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================================
// 5. WISHLIST / FAVORITES
// ==========================================================
@Composable
fun WishlistScreen(
    viewModel: PrimarkViewModel,
    onNavigateToProduct: (Product) -> Unit,
    onNavigateToShop: () -> Unit
) {
    val wishlist by viewModel.wishlistItems.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .testTag("wishlist_screen")
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            border = BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "My Wishlist",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = PrimarkBlue
                )
            }
        }

        val wishlistProducts = allProducts.filter { prod -> wishlist.any { it.productId == prod.id } }

        if (wishlistProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Empty Wishlist",
                        tint = TextLight.copy(alpha = 0.5f),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Wishlist is Empty",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Save items you love so you can quickly buy them or reserve in-store later!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextLight,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onNavigateToShop,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Browse Hot Deals", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(wishlistProducts) { prod ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        PrimarkProductCard(
                            product = prod,
                            isWishlisted = true,
                            onProductClick = { onNavigateToProduct(prod) },
                            onWishlistClick = { viewModel.toggleWishlist(prod.id) }
                        )

                        // Quick action button to move item directly to shopping basket
                        IconButton(
                            onClick = {
                                viewModel.addToBasket(prod, prod.getSizeList().firstOrNull() ?: "M", prod.getColorList().firstOrNull() ?: "White")
                                Toast.makeText(context, "Moved to Basket!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                                .size(28.dp)
                                .background(PrimarkBlue, CircleShape)
                        ) {
                            Icon(Icons.Default.AddShoppingCart, contentDescription = "Move", tint = TextWhite, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}

// ==========================================================
// 6. STORE MODE (LOCATOR & QR CODE product lookup scanner simulator)
// ==========================================================
@Composable
fun StoreModeScreen(
    viewModel: PrimarkViewModel,
    onNavigateToProduct: (Product) -> Unit
) {
    val stores by viewModel.allStores.collectAsState()
    val activeStore by viewModel.activeStore.collectAsState()
    val scannedProduct by viewModel.scannedProduct.collectAsState()
    val scannerMessage by viewModel.scannerMessage.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()

    var manualBarcodeValue by remember { mutableStateOf("") }
    var qrScanningModeActive by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .testTag("store_mode_screen")
    ) {
        // App top tab header representing "Store Mode Helper"
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            border = BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "PRIMARK STORE HELPER",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = PrimarkBlue,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "Optimize your physical store shopping experience in real-time.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextLight,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // SECTION 1: IN-STORE QR LOOKUP SIMULATOR
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = BorderStroke(1.5.dp, PrimarkBlue),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.QrCodeScanner, contentDescription = "QR Look", tint = PrimarkBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "In-Store Barcode / QR Lookup",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Find any ticket in-store to check low pricing, colors, and stock instantly.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextLight
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        if (!qrScanningModeActive && scannedProduct == null) {
                            Button(
                                onClick = { qrScanningModeActive = true },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Cam")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Launch QR scanner", fontWeight = FontWeight.Bold)
                            }
                        }

                        // Active simulator camera panel
                        if (qrScanningModeActive) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .background(Color.Black, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                // Green flashing scanner guide lines
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawRoundRect(
                                        color = Color(0xFF10B981),
                                        topLeft = Offset(size.width * 0.15f, size.height * 0.15f),
                                        size = Size(size.width * 0.7f, size.height * 0.7f),
                                        cornerRadius = CornerRadius(12f, 12f),
                                        style = Stroke(width = 4f)
                                    )
                                    drawLine(
                                        color = Color.Red,
                                        start = Offset(size.width * 0.15f, size.height * 0.5f),
                                        end = Offset(size.width * 0.85f, size.height * 0.5f),
                                        strokeWidth = 3f
                                    )
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "[ CAMERA FEED SIMULATED ]",
                                        color = Color.LightGray,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Scan products 1 to 12. Or click shortcut below!",
                                        color = TextWhite,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Scan shortcut items list
                            Text(
                                text = "Scan shortcuts (Tap a product to simulate scan):",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(allProducts.take(6)) { p ->
                                    Card(
                                        onClick = {
                                            viewModel.simulateBarcodeScan(p.id.toString())
                                            qrScanningModeActive = false
                                        },
                                        colors = CardDefaults.cardColors(containerColor = BgLight),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = p.name.substringBefore(" "),
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = PrimarkBlue,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Manual input as alternative
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = manualBarcodeValue,
                                    onValueChange = { manualBarcodeValue = it },
                                    placeholder = { Text("Or type product id (e.g. 1)", fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f).height(50.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        if (manualBarcodeValue.isNotBlank()) {
                                            viewModel.simulateBarcodeScan(manualBarcodeValue)
                                            manualBarcodeValue = ""
                                            qrScanningModeActive = false
                                        }
                                    },
                                    modifier = Modifier.height(48.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue)
                                ) {
                                    Text("Find")
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            TextButton(
                                onClick = { qrScanningModeActive = false },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Cancel Scanner", color = PrimarkSaleRed)
                            }
                        }

                        // Scanned Product Details card
                        if (scannedProduct != null) {
                            val scanned = scannedProduct!!
                            Card(
                                colors = CardDefaults.cardColors(containerColor = PrimarkBlueLight),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ProductImagePlaceholder(
                                        category = scanned.category,
                                        subCategory = scanned.subCategory,
                                        colors = scanned.colors,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = scanned.name,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = TextDark
                                        )
                                        Text(
                                            text = "Ticket Price: £${"%.2f".format(scanned.price)}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = PrimarkSaleRed,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                    IconButton(onClick = { onNavigateToProduct(scanned) }) {
                                        Icon(Icons.Default.ArrowForward, contentDescription = "Go", tint = PrimarkBlue)
                                    }
                                    IconButton(onClick = { viewModel.clearScannedProduct() }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextLight)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SECTION 2: ACTIVE STORE SELECTOR & HOURS
            item {
                Text(
                    text = "Select Local Store Locator",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            items(stores) { store ->
                val isSel = store.id == activeStore?.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isSel) 2.dp else 1.dp,
                            color = if (isSel) PrimarkBlue else BorderLight,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { viewModel.selectStore(store.id) },
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = store.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) PrimarkBlueDark else TextDark
                            )

                            if (store.clickAndCollectSupported) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F4EA)),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "CLICK & COLLECT",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF137333),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Loc", tint = TextLight, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = store.address,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextLight
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.AccessTime, contentDescription = "Time", tint = TextLight, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = store.openingHours,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLight,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Default.Phone, contentDescription = "Phone", tint = TextLight, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = store.telephone,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextLight
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================================
// 7. ACCOUNT & RESERVATIONS & ADMIN LAUNCHER
// ==========================================================
@Composable
fun AccountAndAdminScreen(
    viewModel: PrimarkViewModel,
    onNavigateToProduct: (Product) -> Unit
) {
    val userName by viewModel.userName.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val isUserSignedIn by viewModel.isUserSignedIn.collectAsState()
    val reservations by viewModel.allReservations.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()

    var showStaffDashboard by remember { mutableStateOf(false) }

    // Admin fields
    var addProdName by remember { mutableStateOf("") }
    var addProdPrice by remember { mutableStateOf("") }
    var addProdCat by remember { mutableStateOf("Women") }
    var addProdSub by remember { mutableStateOf("Basics") }
    var addProdDesc by remember { mutableStateOf("") }
    var addProdColors by remember { mutableStateOf("White,Black") }
    var addProdSizes by remember { mutableStateOf("S,M,L") }

    var updatePriceProdId by remember { mutableStateOf("") }
    var updatePriceNewVal by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgLight)
            .testTag("account_screen")
    ) {
        // Simple Top Header with Staff Dashboard toggle button
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            border = BorderStroke(1.dp, BorderLight),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showStaffDashboard) "Primark Staff Admin" else "My Primark Account",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = PrimarkBlue
                )

                // STAFF PORTAL SHORTCUT TRIGGER
                TextButton(
                    onClick = { showStaffDashboard = !showStaffDashboard },
                    colors = ButtonDefaults.textButtonColors(contentColor = PrimarkBlue)
                ) {
                    Icon(
                        imageVector = if (showStaffDashboard) Icons.Default.Person else Icons.Default.Shield,
                        contentDescription = "Admin Toggle"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showStaffDashboard) "Shopper Mode" else "Staff Dashboard",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (showStaffDashboard) {
            // RETAIL STAFF ADMIN PORTAL
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PrimarkBlueLight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Primark Staff Control",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimarkBlueDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Manage store inventory, update prices, and view sales analytics on the fly.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextDark
                            )
                        }
                    }
                }

                // Analytics Summary
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Store Analytics Summary", fontWeight = FontWeight.Bold, color = TextDark)
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Active Items", style = MaterialTheme.typography.bodySmall, color = TextLight)
                                    Text("12 items", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = PrimarkBlue)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Store Locations", style = MaterialTheme.typography.bodySmall, color = TextLight)
                                    Text("4 Regional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = PrimarkBlue)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Reservation Status", style = MaterialTheme.typography.bodySmall, color = TextLight)
                                    Text("100% Ready", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFF10B981))
                                }
                            }
                        }
                    }
                }

                // 1. ADD NEW INVENTORY ITEM
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "1. Add New Retail Item",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = addProdName,
                                onValueChange = { addProdName = it },
                                label = { Text("Product Name") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = addProdPrice,
                                onValueChange = { addProdPrice = it },
                                label = { Text("Price (e.g. 5.50)") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = addProdCat,
                                onValueChange = { addProdCat = it },
                                label = { Text("Category (e.g. Women, Men, Beauty)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = addProdSub,
                                onValueChange = { addProdSub = it },
                                label = { Text("Subcategory (e.g. Loungewear)") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = addProdDesc,
                                onValueChange = { addProdDesc = it },
                                label = { Text("Product Description") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (addProdName.isNotBlank() && addProdPrice.isNotBlank()) {
                                        val price = addProdPrice.toDoubleOrNull() ?: 0.0
                                        viewModel.adminAddProduct(
                                            addProdName,
                                            price,
                                            addProdCat,
                                            addProdSub,
                                            addProdDesc,
                                            addProdColors,
                                            addProdSizes
                                        )
                                        Toast.makeText(context, "Product listed successfully!", Toast.LENGTH_SHORT).show()
                                        // clear
                                        addProdName = ""
                                        addProdPrice = ""
                                        addProdDesc = ""
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Publish to Primark Stores")
                            }
                        }
                    }
                }

                // 2. QUICK PRICE UPDATE
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "2. Fast Price Update (Triggers Promo Alerts)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = updatePriceProdId,
                                onValueChange = { updatePriceProdId = it },
                                label = { Text("Product ID (e.g., 1)") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = updatePriceNewVal,
                                onValueChange = { updatePriceNewVal = it },
                                label = { Text("New Lower Price (£)") },
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    val id = updatePriceProdId.toIntOrNull()
                                    val newP = updatePriceNewVal.toDoubleOrNull()
                                    if (id != null && newP != null) {
                                        viewModel.adminUpdatePrice(id, newP)
                                        Toast.makeText(context, "Price updated successfully!", Toast.LENGTH_SHORT).show()
                                        updatePriceProdId = ""
                                        updatePriceNewVal = ""
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Update Price & Send Notification")
                            }
                        }
                    }
                }
            }
        } else {
            // NORMAL SHOPPER ACCOUNT SCREEN
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // User Profile Header Info
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(PrimarkBlueLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userName.firstOrNull()?.toString()?.uppercase() ?: "P",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp,
                                    color = PrimarkBlue
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = userName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                Text(
                                    text = userEmail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextLight
                                )
                            }

                            if (!isUserSignedIn) {
                                Button(
                                    onClick = { viewModel.simulateSignIn("Tom", "tom@ahyx.org") },
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimarkBlue),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Sign In", fontSize = 12.sp)
                                }
                            } else {
                                TextButton(onClick = { viewModel.simulateSignOut() }) {
                                    Text("Sign Out", color = PrimarkSaleRed, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // IN-STORE CLICK & COLLECT RESERVATIONS
                item {
                    Text(
                        text = "My Store Reservations (${reservations.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                if (reservations.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BorderLight)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Storefront, contentDescription = "No res", tint = TextLight, modifier = Modifier.size(36.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "No Active Click & Collect Orders",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Text(
                                        text = "Items added to your basket can be instantly reserved in-store for offline payment & pickup.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextLight,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                } else {
                    items(reservations) { res ->
                        val product = allProducts.find { it.id == res.productId }
                        if (product != null) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                                border = BorderStroke(1.dp, BorderLight),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "ID: ${res.id}",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = PrimarkBlueDark
                                        )

                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F4EA)),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = res.status.uppercase(),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFF137333),
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        ProductImagePlaceholder(
                                            category = product.category,
                                            subCategory = product.subCategory,
                                            colors = product.colors,
                                            modifier = Modifier
                                                .size(54.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = product.name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = TextDark
                                            )
                                            Text(
                                                text = "Size: ${res.size} | Color: ${res.color} | Qty: ${res.quantity}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextLight
                                            )
                                        }
                                    }

                                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = BorderLight)

                                    // Display pickup Barcode Scanner graphics
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = "In-Store Pickup Code",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextLight
                                            )
                                            Text(
                                                text = "SCAN CODE: ${res.pickupCode}",
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Black,
                                                color = TextDark
                                            )
                                        }

                                        // Mini mock scan-bars drawing
                                        Canvas(modifier = Modifier.size(70.dp, 30.dp)) {
                                            val space = size.width / 10
                                            for (i in 0..9) {
                                                if (i % 3 != 0) {
                                                    drawRect(
                                                        color = Color.Black,
                                                        topLeft = Offset(i * space, 0f),
                                                        size = Size(space * 0.6f, size.height)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // NOTIFICATION LOGS
                item {
                    Text(
                        text = "My Inbox Alerts (${notifications.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                if (notifications.isEmpty()) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BorderLight)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Inbox is completely clear.", color = TextLight, fontSize = 12.sp)
                            }
                        }
                    }
                } else {
                    items(notifications) { notif ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            border = BorderStroke(1.dp, BorderLight),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = when (notif.type) {
                                        "Promo" -> Icons.Default.LocalOffer
                                        "Reservation" -> Icons.Default.Storefront
                                        else -> Icons.Default.Info
                                    },
                                    contentDescription = "Notif Type",
                                    tint = PrimarkBlue,
                                    modifier = Modifier.size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = notif.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Text(
                                        text = notif.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextLight
                                    )
                                }

                                IconButton(onClick = { viewModel.deleteNotification(notif.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
