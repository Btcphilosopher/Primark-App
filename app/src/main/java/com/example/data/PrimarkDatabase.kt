package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ==========================================
// ROOM ENTITIES (DATA MODELS)
// ==========================================

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val originalPrice: Double, // Used for showing retail-friendly deals/discounts
    val category: String, // Women, Men, Kids, Baby, Home, Accessories, Beauty, Sale
    val subCategory: String, // T-shirts, Jeans, Shoes, Skincare, Duvets, etc.
    val description: String,
    val imageResName: String, // Name of drawable or illustration fallback
    val colors: String, // Comma-separated list e.g., "White,Blue,Yellow"
    val sizes: String, // Comma-separated list e.g., "S,M,L,XL"
    val rating: Float = 4.0f,
    val isUnderTen: Boolean = false,
    val isNewArrival: Boolean = false,
    val isTrending: Boolean = false,
    val isSeasonal: Boolean = false,
    val isPromo: Boolean = false
) {
    fun getColorList(): List<String> = colors.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    fun getSizeList(): List<String> = sizes.split(",").map { it.trim() }.filter { it.isNotEmpty() }
}

@Entity(tableName = "stores")
data class Store(
    @PrimaryKey val id: Int,
    val name: String,
    val address: String,
    val openingHours: String, // Comma or pipe-separated e.g., "Mon-Fri: 09:00 - 21:00 | Sat: 09:00 - 19:00"
    val telephone: String,
    val latitude: Double,
    val longitude: Double,
    val clickAndCollectSupported: Boolean = true
)

@Entity(tableName = "stock_levels")
data class StockLevel(
    @PrimaryKey val id: String, // Combined key: storeId_productId
    val storeId: Int,
    val productId: Int,
    val stockCount: Int
)

@Entity(tableName = "basket")
data class BasketItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val productId: Int,
    val quantity: Int,
    val selectedSize: String,
    val selectedColor: String
)

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey val productId: Int,
    val addedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reservations")
data class Reservation(
    @PrimaryKey val id: String, // RES-XXXXXX
    val storeId: Int,
    val productId: Int,
    val quantity: Int,
    val size: String,
    val color: String,
    val status: String, // Pending, Ready for Pickup, Collected, Cancelled
    val pickupCode: String,
    val date: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String // Promo, Order, Reservation, Store
)

// ==========================================
// UNIFIED DATA ACCESS OBJECT (DAO)
// ==========================================

@Dao
interface PrimarkDao {

    // Products
    @Query("SELECT * FROM products ORDER BY id DESC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE category = :category ORDER BY id DESC")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    fun getProductById(id: Int): Flow<Product?>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' OR subCategory LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    // Stores
    @Query("SELECT * FROM stores ORDER BY name ASC")
    fun getAllStores(): Flow<List<Store>>

    @Query("SELECT * FROM stores WHERE id = :id")
    fun getStoreById(id: Int): Flow<Store?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(stores: List<Store>)

    // Stock levels
    @Query("SELECT * FROM stock_levels WHERE storeId = :storeId AND productId = :productId")
    fun getStockForProductInStore(storeId: Int, productId: Int): Flow<StockLevel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStockLevels(stockLevels: List<StockLevel>)

    // Basket Items
    @Query("SELECT * FROM basket ORDER BY id DESC")
    fun getBasketItems(): Flow<List<BasketItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBasketItem(item: BasketItem)

    @Query("UPDATE basket SET quantity = :quantity WHERE id = :id")
    suspend fun updateBasketItemQuantity(id: Int, quantity: Int)

    @Query("DELETE FROM basket WHERE id = :id")
    suspend fun deleteBasketItem(id: Int)

    @Query("DELETE FROM basket")
    suspend fun clearBasket()

    // Wishlist Items
    @Query("SELECT * FROM wishlist ORDER BY addedAt DESC")
    fun getWishlistItems(): Flow<List<WishlistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(item: WishlistItem)

    @Query("DELETE FROM wishlist WHERE productId = :productId")
    suspend fun deleteWishlistItem(productId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE productId = :productId)")
    fun isProductInWishlist(productId: Int): Flow<Boolean>

    // Reservations
    @Query("SELECT * FROM reservations ORDER BY date DESC")
    fun getAllReservations(): Flow<List<Reservation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: Reservation)

    @Query("UPDATE reservations SET status = :status WHERE id = :id")
    suspend fun updateReservationStatus(id: String, status: String)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: Int)

    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: Int)
}

// ==========================================
// DATABASE CONTROLLER
// ==========================================

@Database(
    entities = [
        Product::class,
        Store::class,
        StockLevel::class,
        BasketItem::class,
        WishlistItem::class,
        Reservation::class,
        NotificationItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PrimarkDatabase : RoomDatabase() {
    abstract fun primarkDao(): PrimarkDao

    companion object {
        @Volatile
        private var INSTANCE: PrimarkDatabase? = null

        fun getDatabase(context: Context): PrimarkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PrimarkDatabase::class.java,
                    "primark_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
