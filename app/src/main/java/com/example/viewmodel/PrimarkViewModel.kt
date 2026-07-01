package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class PrimarkViewModel(
    application: Application,
    private val repository: PrimarkRepository
) : AndroidViewModel(application) {

    // Initialize database defaults
    init {
        viewModelScope.launch {
            repository.prepopulateIfNeeded()
        }
    }

    // ------------------------------------------
    // FLOW DATA READS
    // ------------------------------------------
    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allStores: StateFlow<List<Store>> = repository.allStores
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allReservations: StateFlow<List<Reservation>> = repository.allReservations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<NotificationItem>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val basketItems: StateFlow<List<BasketItem>> = repository.basketItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistItems: StateFlow<List<WishlistItem>> = repository.wishlistItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // ------------------------------------------
    // ACTIVE SELECTED UI STATES
    // ------------------------------------------
    private val _selectedStoreId = MutableStateFlow(101) // Default to Flagship Birmingham
    val selectedStoreId: StateFlow<Int> = _selectedStoreId.asStateFlow()

    val activeStore: StateFlow<Store?> = combine(allStores, selectedStoreId) { stores, id ->
        stores.find { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _selectedProduct = MutableStateFlow<Product?>(null)
    val selectedProduct: StateFlow<Product?> = _selectedProduct.asStateFlow()

    // Search Query & Suggestions
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val searchResults: StateFlow<List<Product>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                repository.searchProducts(query)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _recentSearches = MutableStateFlow(listOf("T-Shirt", "Sandals", "Home Duvet", "Skincare Booster"))
    val recentSearches: StateFlow<List<String>> = _recentSearches.asStateFlow()

    // ------------------------------------------
    // SHOPPING CART / BASKET CALCULATIONS
    // ------------------------------------------
    val basketTotal: StateFlow<Double> = combine(basketItems, allProducts) { items, products ->
        items.sumOf { item ->
            val prod = products.find { it.id == item.productId }
            (prod?.price ?: 0.0) * item.quantity
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _appliedPromoCode = MutableStateFlow<String?>(null)
    val appliedPromoCode: StateFlow<String?> = _appliedPromoCode.asStateFlow()

    private val _promoDiscountPercent = MutableStateFlow(0)
    val promoDiscountPercent: StateFlow<Int> = _promoDiscountPercent.asStateFlow()

    val basketDiscountedTotal: StateFlow<Double> = combine(basketTotal, promoDiscountPercent) { total, discount ->
        if (discount > 0) total * (1.0 - (discount / 100.0)) else total
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // ------------------------------------------
    // SCANNER STATE
    // ------------------------------------------
    private val _scannedProduct = MutableStateFlow<Product?>(null)
    val scannedProduct: StateFlow<Product?> = _scannedProduct.asStateFlow()

    private val _scannerMessage = MutableStateFlow<String?>(null)
    val scannerMessage: StateFlow<String?> = _scannerMessage.asStateFlow()

    // ------------------------------------------
    // AUTHENTICATION & USER PROFILE
    // ------------------------------------------
    private val _userName = MutableStateFlow("Guest User")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("guest@primark.com")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    private val _isUserSignedIn = MutableStateFlow(false)
    val isUserSignedIn: StateFlow<Boolean> = _isUserSignedIn.asStateFlow()

    // ------------------------------------------
    // BRAND CORE ACTIONS
    // ------------------------------------------
    fun selectProduct(product: Product?) {
        _selectedProduct.value = product
    }

    fun selectStore(storeId: Int) {
        _selectedStoreId.value = storeId
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addRecentSearch(query: String) {
        val current = _recentSearches.value.toMutableList()
        current.remove(query)
        current.add(0, query)
        _recentSearches.value = current.take(5)
    }

    fun toggleWishlist(productId: Int) {
        viewModelScope.launch {
            repository.toggleWishlist(productId)
        }
    }

    fun addToBasket(product: Product, size: String, color: String, qty: Int = 1) {
        viewModelScope.launch {
            val currentBasket = basketItems.value
            val match = currentBasket.find {
                it.productId == product.id && it.selectedSize == size && it.selectedColor == color
            }
            if (match != null) {
                repository.updateBasketItemQuantity(match.id, match.quantity + qty)
            } else {
                repository.insertBasketItem(
                    BasketItem(
                        productId = product.id,
                        quantity = qty,
                        selectedSize = size,
                        selectedColor = color
                    )
                )
            }
        }
    }

    fun updateBasketQuantity(itemId: Int, quantity: Int) {
        viewModelScope.launch {
            if (quantity <= 0) {
                repository.deleteBasketItem(itemId)
            } else {
                repository.updateBasketItemQuantity(itemId, quantity)
            }
        }
    }

    fun removeBasketItem(itemId: Int) {
        viewModelScope.launch {
            repository.deleteBasketItem(itemId)
        }
    }

    fun applyPromo(code: String): Boolean {
        return if (code.trim().uppercase() == "PRIMARK10" || code.trim().uppercase() == "BUDGET10") {
            _appliedPromoCode.value = code.trim().uppercase()
            _promoDiscountPercent.value = 10
            true
        } else if (code.trim().uppercase() == "SUPERFASHION") {
            _appliedPromoCode.value = code.trim().uppercase()
            _promoDiscountPercent.value = 20
            true
        } else {
            false
        }
    }

    fun clearPromo() {
        _appliedPromoCode.value = null
        _promoDiscountPercent.value = 0
    }

    fun simulateInStoreReservation(): Boolean {
        val items = basketItems.value
        if (items.isEmpty()) return false
        val storeId = selectedStoreId.value

        viewModelScope.launch {
            for (item in items) {
                val reservationId = "RES-${(100000..999999).random()}"
                val pickupCode = (1000..9999).random().toString()
                repository.createReservation(
                    Reservation(
                        id = reservationId,
                        storeId = storeId,
                        productId = item.productId,
                        quantity = item.quantity,
                        size = item.selectedSize,
                        color = item.selectedColor,
                        status = "Ready for Pickup", // Set it ready for instant in-store experience!
                        pickupCode = pickupCode
                    )
                )
            }
            repository.clearBasket()
        }
        return true
    }

    // QR Code Scanning simulation
    fun simulateBarcodeScan(barcode: String) {
        val allProds = allProducts.value
        // Match product by id or part of description/name
        val matched = allProds.find { it.id.toString() == barcode.trim() }
            ?: allProds.find { it.name.contains(barcode, ignoreCase = true) }

        if (matched != null) {
            _scannedProduct.value = matched
            _scannerMessage.value = "Success: Product Found!"
        } else {
            _scannedProduct.value = null
            _scannerMessage.value = "Code not recognized. Try typing a product number 1 to 12."
        }
    }

    fun clearScannedProduct() {
        _scannedProduct.value = null
        _scannerMessage.value = null
    }

    // Sign in simulator
    fun simulateSignIn(name: String, email: String) {
        _userName.value = name.ifBlank { "Primark Shopper" }
        _userEmail.value = email.ifBlank { "shopper@primark.com" }
        _isUserSignedIn.value = true
        viewModelScope.launch {
            repository.addNotification(
                title = "Signed In Successfully",
                description = "Welcome back, ${_userName.value}! You can now track your click & collect reservations securely.",
                type = "Store"
            )
        }
    }

    fun simulateSignOut() {
        _userName.value = "Guest User"
        _userEmail.value = "guest@primark.com"
        _isUserSignedIn.value = false
    }

    // Notification controls
    fun markNotificationAsRead(id: Int) {
        viewModelScope.launch {
            repository.markNotificationAsRead(id)
        }
    }

    fun deleteNotification(id: Int) {
        viewModelScope.launch {
            repository.deleteNotification(id)
        }
    }

    // ------------------------------------------
    // ADMIN PANEL STAFF ACTIONS
    // ------------------------------------------
    fun adminAddProduct(name: String, price: Double, category: String, subCategory: String, description: String, colors: String, sizes: String) {
        viewModelScope.launch {
            val newProd = Product(
                name = name,
                price = price,
                originalPrice = price,
                category = category,
                subCategory = subCategory,
                description = description,
                imageResName = "ic_tee", // default fallback placeholder
                colors = colors,
                sizes = sizes,
                rating = 4.5f,
                isNewArrival = true
            )
            repository.addCustomProduct(newProd)
            repository.addNotification(
                title = "New Product Added!",
                description = "Staff added $name to the $category inventory.",
                type = "Store"
            )
        }
    }

    fun adminUpdatePrice(productId: Int, newPrice: Double) {
        viewModelScope.launch {
            val productFlow = repository.getProductById(productId)
            val currentProd = productFlow.firstOrNull()
            if (currentProd != null) {
                val updated = currentProd.copy(
                    price = newPrice,
                    originalPrice = currentProd.price // Keep current price as original for discount representation!
                )
                repository.addCustomProduct(updated)
                repository.addNotification(
                    title = "Price Drop Alert: ${currentProd.name}",
                    description = "Price updated to only £${"%.2f".format(newPrice)}! Check out the sale now.",
                    type = "Promo"
                )
            }
        }
    }
}

// ------------------------------------------
// VIEWMODEL FACTORY PROTOCOL
// ------------------------------------------
class PrimarkViewModelFactory(
    private val application: Application,
    private val repository: PrimarkRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrimarkViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrimarkViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
