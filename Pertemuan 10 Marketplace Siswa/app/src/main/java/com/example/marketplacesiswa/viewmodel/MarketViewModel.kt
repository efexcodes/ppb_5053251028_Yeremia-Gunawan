package com.example.marketplacesiswa.viewmodel

import androidx.lifecycle.ViewModel
import com.example.marketplacesiswa.data.CartItem
import com.example.marketplacesiswa.data.Category
import com.example.marketplacesiswa.data.Order
import com.example.marketplacesiswa.data.PaymentMethod
import com.example.marketplacesiswa.data.Product
import com.example.marketplacesiswa.data.SampleData
import com.example.marketplacesiswa.data.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val SHIPPING_FEE = 2000

data class MarketUiState(
    val products: List<Product> = SampleData.products,
    val cart: List<CartItem> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val orders: List<Order> = emptyList(),
    val query: String = "",
    val selectedCategory: Category? = null,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColor: Boolean = false
) {
    val visibleProducts: List<Product>
        get() = products.filter { product ->
            (selectedCategory == null || product.category == selectedCategory) &&
                (query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true))
        }

    val favoriteProducts: List<Product>
        get() = products.filter { it.id in favoriteIds }

    val cartCount: Int get() = cart.sumOf { it.quantity }
    val subtotal: Int get() = cart.sumOf { it.subtotal }
    val shipping: Int get() = if (cart.isEmpty()) 0 else SHIPPING_FEE
    val total: Int get() = subtotal + shipping
}

class MarketViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MarketUiState())
    val uiState: StateFlow<MarketUiState> = _uiState.asStateFlow()

    fun productById(id: Int): Product? = _uiState.value.products.firstOrNull { it.id == id }

    // ---- Pencarian & filter ----
    fun onQueryChange(query: String) = _uiState.update { it.copy(query = query) }

    fun onCategorySelected(category: Category?) =
        _uiState.update { it.copy(selectedCategory = category) }

    // ---- Favorit ----
    fun toggleFavorite(productId: Int) = _uiState.update { state ->
        val ids = if (productId in state.favoriteIds) state.favoriteIds - productId
        else state.favoriteIds + productId
        state.copy(favoriteIds = ids)
    }

    // ---- Keranjang ----
    fun addToCart(product: Product, quantity: Int = 1) = _uiState.update { state ->
        val existing = state.cart.firstOrNull { it.product.id == product.id }
        val cart = if (existing == null) {
            state.cart + CartItem(product, quantity)
        } else {
            state.cart.map {
                if (it.product.id == product.id) it.copy(quantity = it.quantity + quantity) else it
            }
        }
        state.copy(cart = cart)
    }

    fun changeCartQuantity(productId: Int, delta: Int) = _uiState.update { state ->
        val cart = state.cart.mapNotNull {
            if (it.product.id != productId) it
            else it.copy(quantity = it.quantity + delta).takeIf { item -> item.quantity > 0 }
        }
        state.copy(cart = cart)
    }

    fun removeFromCart(productId: Int) = _uiState.update { state ->
        state.copy(cart = state.cart.filterNot { it.product.id == productId })
    }

    // ---- Checkout ----
    fun placeOrder(buyerName: String, className: String, payment: PaymentMethod) {
        _uiState.update { state ->
            if (state.cart.isEmpty()) return@update state
            val order = Order(
                id = (state.orders.maxOfOrNull { it.id } ?: 0) + 1,
                items = state.cart,
                total = state.total,
                buyerName = buyerName.trim(),
                className = className.trim(),
                payment = payment,
                time = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID")).format(Date())
            )
            state.copy(orders = listOf(order) + state.orders, cart = emptyList())
        }
    }

    // ---- Jual produk ----
    fun addProduct(name: String, description: String, price: Int, category: Category) {
        _uiState.update { state ->
            val product = Product(
                id = (state.products.maxOfOrNull { it.id } ?: 0) + 1,
                name = name.trim(),
                description = description.trim().ifBlank { "Belum ada deskripsi." },
                price = price,
                category = category,
                seller = "Kamu",
                emoji = when (category) {
                    Category.MAKANAN -> "🍱"
                    Category.FASHION -> "👗"
                    Category.KERAJINAN -> "🎨"
                    Category.ELEKTRONIK -> "🎧"
                    Category.BUKU -> "📖"
                }
            )
            state.copy(products = listOf(product) + state.products)
        }
    }

    // ---- Tema ----
    fun setThemeMode(mode: ThemeMode) = _uiState.update { it.copy(themeMode = mode) }

    fun setDynamicColor(enabled: Boolean) = _uiState.update { it.copy(dynamicColor = enabled) }
}
