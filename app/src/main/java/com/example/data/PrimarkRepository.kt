package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrimarkRepository(private val dao: PrimarkDao) {

    // Products Flow
    val allProducts: Flow<List<Product>> = dao.getAllProducts()
    val allStores: Flow<List<Store>> = dao.getAllStores()
    val allReservations: Flow<List<Reservation>> = dao.getAllReservations()
    val allNotifications: Flow<List<NotificationItem>> = dao.getAllNotifications()
    val basketItems: Flow<List<BasketItem>> = dao.getBasketItems()
    val wishlistItems: Flow<List<WishlistItem>> = dao.getWishlistItems()

    fun getProductsByCategory(category: String): Flow<List<Product>> = dao.getProductsByCategory(category)
    fun getProductById(id: Int): Flow<Product?> = dao.getProductById(id)
    fun searchProducts(query: String): Flow<List<Product>> = dao.searchProducts(query)
    fun isProductInWishlist(productId: Int): Flow<Boolean> = dao.isProductInWishlist(productId)
    fun getStockForProductInStore(storeId: Int, productId: Int): Flow<StockLevel?> = dao.getStockForProductInStore(storeId, productId)

    // Suspend operations
    suspend fun insertBasketItem(item: BasketItem) = withContext(Dispatchers.IO) {
        dao.insertBasketItem(item)
    }

    suspend fun updateBasketItemQuantity(id: Int, quantity: Int) = withContext(Dispatchers.IO) {
        dao.updateBasketItemQuantity(id, quantity)
    }

    suspend fun deleteBasketItem(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteBasketItem(id)
    }

    suspend fun clearBasket() = withContext(Dispatchers.IO) {
        dao.clearBasket()
    }

    suspend fun toggleWishlist(productId: Int) = withContext(Dispatchers.IO) {
        val currentWishlist = dao.getWishlistItems().first()
        val exists = currentWishlist.any { it.productId == productId }
        if (exists) {
            dao.deleteWishlistItem(productId)
        } else {
            dao.insertWishlistItem(WishlistItem(productId = productId))
        }
    }

    suspend fun createReservation(reservation: Reservation) = withContext(Dispatchers.IO) {
        dao.insertReservation(reservation)
        // Add a helpful confirmation notification
        dao.insertNotification(
            NotificationItem(
                title = "Reservation Confirmed",
                description = "Your item is reserved! Pickup code: ${reservation.pickupCode} at Store #${reservation.storeId}",
                type = "Reservation"
            )
        )
    }

    suspend fun updateReservationStatus(id: String, status: String) = withContext(Dispatchers.IO) {
        dao.updateReservationStatus(id, status)
    }

    suspend fun addNotification(title: String, description: String, type: String) = withContext(Dispatchers.IO) {
        dao.insertNotification(NotificationItem(title = title, description = description, type = type))
    }

    suspend fun markNotificationAsRead(id: Int) = withContext(Dispatchers.IO) {
        dao.markNotificationAsRead(id)
    }

    suspend fun deleteNotification(id: Int) = withContext(Dispatchers.IO) {
        dao.deleteNotification(id)
    }

    suspend fun addCustomProduct(product: Product) = withContext(Dispatchers.IO) {
        dao.insertProducts(listOf(product))
    }

    // Population of Sample Datasets
    suspend fun prepopulateIfNeeded() = withContext(Dispatchers.IO) {
        val existingProducts = dao.getAllProducts().first()
        if (existingProducts.isEmpty()) {
            val sampleProducts = listOf(
                Product(
                    id = 1,
                    name = "Essential Crew Neck T-Shirt",
                    price = 4.50,
                    originalPrice = 4.50,
                    category = "Men",
                    subCategory = "Basics",
                    description = "A sustainable cotton-rich crew neck tee. Super soft, breathable, and designed for daily casual wear. Part of Primark Cares.",
                    imageResName = "ic_tee",
                    colors = "White, Black, Navy, Khaki, Grey",
                    sizes = "S, M, L, XL, XXL",
                    rating = 4.6f,
                    isUnderTen = true,
                    isNewArrival = false,
                    isTrending = true
                ),
                Product(
                    id = 2,
                    name = "High-Waisted Stretch Skinny Jeans",
                    price = 13.00,
                    originalPrice = 16.00,
                    category = "Women",
                    subCategory = "Jeans",
                    description = "Figure-hugging skinny jeans in vintage medium wash denim. Features classic five-pocket styling and extra elasticity for ultimate comfortable movement.",
                    imageResName = "ic_jeans",
                    colors = "Mid Blue, Dark Blue, Grey, Black",
                    sizes = "6, 8, 10, 12, 14, 16, 18",
                    rating = 4.5f,
                    isPromo = true,
                    isTrending = true
                ),
                Product(
                    id = 3,
                    name = "Kids Stripe Terry Cotton Hoodie",
                    price = 7.00,
                    originalPrice = 7.00,
                    category = "Kids",
                    subCategory = "Outerwear",
                    description = "Super comfortable striped cotton hoodie with rib-knit cuffs and spacious kangaroo pocket. Easy-care machine washable material.",
                    imageResName = "ic_hoodie",
                    colors = "Blue/White, Red/White, Yellow/Grey",
                    sizes = "3-4Y, 5-6Y, 7-8Y, 9-10Y",
                    rating = 4.7f,
                    isUnderTen = true,
                    isNewArrival = true
                ),
                Product(
                    id = 4,
                    name = "Organic Cotton 3-Pack Sleepsuits",
                    price = 8.50,
                    originalPrice = 11.00,
                    category = "Baby",
                    subCategory = "Sleepwear",
                    description = "Three-pack of ultra-soft ribbed organic cotton baby sleepsuits. Built-in fold-over scratch mitts and nickel-free snap fastenings.",
                    imageResName = "ic_sleepsuit",
                    colors = "Soft Pink, Mint Green, Cloud White",
                    sizes = "Newborn, 0-3M, 3-6M, 6-9M, 9-12M",
                    rating = 4.9f,
                    isUnderTen = true,
                    isPromo = true,
                    isSeasonal = true
                ),
                Product(
                    id = 5,
                    name = "Waffle Weave Pure Cotton Duvet Set",
                    price = 18.00,
                    originalPrice = 18.00,
                    category = "Home",
                    subCategory = "Bedding",
                    description = "Premium looking waffle texture on the face with a soft plain reverse. Crafted from breathable cotton to keep you cool and cosy all night.",
                    imageResName = "ic_bedding",
                    colors = "Charcoal, Soft Lilac, Ochre, White",
                    sizes = "Single, Double, King",
                    rating = 4.4f,
                    isSeasonal = true
                ),
                Product(
                    id = 6,
                    name = "Double Strap Cork Footbed Sandals",
                    price = 9.00,
                    originalPrice = 9.00,
                    category = "Women",
                    subCategory = "Footwear",
                    description = "Summer essential slip-on sliders featuring adjustable double metal buckles, contoured cork footbed for customized support, and grippy EVA soles.",
                    imageResName = "ic_sandals",
                    colors = "Tan Brown, Black, Rose Gold, Off-White",
                    sizes = "3, 4, 5, 6, 7, 8",
                    rating = 4.3f,
                    isUnderTen = true,
                    isSeasonal = true,
                    isTrending = true
                ),
                Product(
                    id = 7,
                    name = "Hyaluronic Acid Hydrating Face Serum",
                    price = 3.50,
                    originalPrice = 3.50,
                    category = "Beauty",
                    subCategory = "Skincare",
                    description = "PS... Skin Hydration Booster containing 2% pure hyaluronic acid and provitamin B5. Replenishes deep skin moisture for a dewy, glowing finish.",
                    imageResName = "ic_serum",
                    colors = "Clear",
                    sizes = "30ml",
                    rating = 4.8f,
                    isUnderTen = true,
                    isNewArrival = true
                ),
                Product(
                    id = 8,
                    name = "Classic Retro Canvas Trainers",
                    price = 10.00,
                    originalPrice = 10.00,
                    category = "Men",
                    subCategory = "Footwear",
                    description = "Timeless low-top lace-up shoes. Heavyweight canvas upper, steel eyelets, rubber toe bumper and a flexible vulcanised sole for durable daily style.",
                    imageResName = "ic_trainers",
                    colors = "Black, White, Burgundy, Forest Green",
                    sizes = "7, 8, 9, 10, 11, 12",
                    rating = 4.2f,
                    isUnderTen = true,
                    isTrending = false
                ),
                Product(
                    id = 9,
                    name = "Gold Effect Chunky Hoop Earrings",
                    price = 2.50,
                    originalPrice = 2.50,
                    category = "Accessories",
                    subCategory = "Jewellery",
                    description = "A versatile 3-pair pack of polished chunky hoop earrings. Highly durable post closure. Perfect for upgrading any casual fashion look.",
                    imageResName = "ic_hoops",
                    colors = "Gold, Silver",
                    sizes = "One Size",
                    rating = 4.5f,
                    isUnderTen = true,
                    isNewArrival = false,
                    isTrending = true
                ),
                Product(
                    id = 10,
                    name = "Cropped Lightweight Denim Jacket",
                    price = 15.00,
                    originalPrice = 20.00,
                    category = "Women",
                    subCategory = "Outerwear",
                    description = "Stylishly relaxed fit denim jacket with cropped hemline, button closures, and button-flap chest pockets. Pure durable cotton denim.",
                    imageResName = "ic_jacket",
                    colors = "Light Acid Wash, Mid Indigo, Pristine White",
                    sizes = "4, 6, 8, 10, 12, 14, 16",
                    rating = 4.6f,
                    isPromo = true,
                    isTrending = true
                ),
                Product(
                    id = 11,
                    name = "Liquid Matte Lipstick Duo Pack",
                    price = 4.00,
                    originalPrice = 5.00,
                    category = "Beauty",
                    subCategory = "Makeup",
                    description = "Two complimentary shades of long-wear liquid matte lipstick. Non-drying, transfer-proof formulation with rich pigment payoff and sweet cocoa scent.",
                    imageResName = "ic_lipstick",
                    colors = "Nude Elegance, Bold Crimson, Dusty Rose",
                    sizes = "Standard",
                    rating = 4.7f,
                    isUnderTen = true,
                    isPromo = true,
                    isNewArrival = true
                ),
                Product(
                    id = 12,
                    name = "Unisex Cozy Flannel Pyjama Bottoms",
                    price = 9.50,
                    originalPrice = 9.50,
                    category = "Home",
                    subCategory = "Loungewear",
                    description = "Relaxed fit lounge trousers crafted in ultra-brushed warm cotton flannel. Elasticated drawcord waistband and side seam pockets.",
                    imageResName = "ic_pajama",
                    colors = "Red Tartan, Navy Plaid, Green Check",
                    sizes = "XS, S, M, L, XL, XXL",
                    rating = 4.8f,
                    isUnderTen = true,
                    isSeasonal = true,
                    isNewArrival = true
                )
            )
            dao.insertProducts(sampleProducts)
        }

        val existingStores = dao.getAllStores().first()
        if (existingStores.isEmpty()) {
            val sampleStores = listOf(
                Store(
                    id = 101,
                    name = "Primark Birmingham (Flagship)",
                    address = "38 High St, Birmingham, B4 7SL",
                    openingHours = "Mon-Sat: 08:00 - 21:00 | Sun: 11:00 - 17:00",
                    telephone = "+44 121 643 9012",
                    latitude = 52.4801,
                    longitude = -1.8954,
                    clickAndCollectSupported = true
                ),
                Store(
                    id = 102,
                    name = "Primark London Oxford Street East",
                    address = "14-28 Oxford St, London, W1D 1AU",
                    openingHours = "Mon-Sat: 09:00 - 22:00 | Sun: 11:30 - 18:00",
                    telephone = "+44 20 7580 5510",
                    latitude = 51.5165,
                    longitude = -0.1306,
                    clickAndCollectSupported = true
                ),
                Store(
                    id = 103,
                    name = "Primark Manchester Market Street",
                    address = "106 Market St, Manchester, M1 1PF",
                    openingHours = "Mon-Sat: 08:30 - 21:00 | Sun: 11:00 - 17:00",
                    telephone = "+44 161 834 6815",
                    latitude = 53.4828,
                    longitude = -2.2405,
                    clickAndCollectSupported = true
                ),
                Store(
                    id = 104,
                    name = "Primark Cardiff",
                    address = "St David's Dewi Sant, Cardiff, CF10 2EQ",
                    openingHours = "Mon-Fri: 09:00 - 20:00 | Sat: 09:00 - 19:00 | Sun: 11:00 - 17:00",
                    telephone = "+44 29 2038 3180",
                    latitude = 51.4804,
                    longitude = -3.1741,
                    clickAndCollectSupported = false
                )
            )
            dao.insertStores(sampleStores)

            // Populate mock stock levels for products 1 to 12 across stores 101 to 104
            val sampleStock = mutableListOf<StockLevel>()
            for (storeId in listOf(101, 102, 103, 104)) {
                for (productId in 1..12) {
                    val stockCount = if (productId % 3 == 0 && storeId == 104) 0 else (10..150).random()
                    sampleStock.add(
                        StockLevel(
                            id = "${storeId}_${productId}",
                            storeId = storeId,
                            productId = productId,
                            stockCount = stockCount
                        )
                    )
                }
            }
            dao.insertStockLevels(sampleStock)
        }

        val existingNotifications = dao.getAllNotifications().first()
        if (existingNotifications.isEmpty()) {
            dao.insertNotification(
                NotificationItem(
                    title = "Welcome to Primark!",
                    description = "Enjoy your high-performance retail helper. Access store catalogs, click & collect reservation, and scan product codes directly in-store!",
                    type = "Store"
                )
            )
            dao.insertNotification(
                NotificationItem(
                    title = "Super Basics Under £10",
                    description = "Check out our premium-feel organic cotton t-shirts, loungewear, and active essentials—all for less than ten pounds.",
                    type = "Promo"
                )
            )
        }
    }
}
