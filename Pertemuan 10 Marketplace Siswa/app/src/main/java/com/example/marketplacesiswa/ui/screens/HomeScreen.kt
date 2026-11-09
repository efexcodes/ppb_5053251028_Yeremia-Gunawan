package com.example.marketplacesiswa.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marketplacesiswa.data.Category
import com.example.marketplacesiswa.ui.components.EmptyState
import com.example.marketplacesiswa.ui.components.ProductList
import com.example.marketplacesiswa.viewmodel.MarketUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: MarketUiState,
    wide: Boolean,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (Category?) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onOpenDetail: (Int) -> Unit,
    onOpenCart: () -> Unit,
    onAddProduct: (String, String, Int, Category) -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("🛍️")
                            }
                        }
                        Spacer(Modifier.size(10.dp))
                        Text("MarketSiswa", style = MaterialTheme.typography.titleLarge)
                    }
                },
                actions = {
                    IconButton(onClick = onOpenCart) {
                        BadgedBox(
                            badge = {
                                if (state.cartCount > 0) Badge { Text(state.cartCount.toString()) }
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Keranjang")
                        }
                    }
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("JS", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Jual produk")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        ProductList(
            products = state.visibleProducts,
            favoriteIds = state.favoriteIds,
            wide = wide,
            onToggleFavorite = onToggleFavorite,
            onOpenDetail = onOpenDetail,
            modifier = Modifier.padding(innerPadding),
            header = {
                HomeHeader(
                    query = state.query,
                    selected = state.selectedCategory,
                    onQueryChange = onQueryChange,
                    onCategorySelected = onCategorySelected
                )
            },
            empty = {
                EmptyState(
                    emoji = "🔎",
                    title = "Produk tidak ditemukan",
                    message = "Coba kata kunci atau kategori lain."
                )
            }
        )
    }

    if (showSheet) {
        AddProductSheet(
            onDismiss = { showSheet = false },
            onSave = { name, desc, price, category ->
                onAddProduct(name, desc, price, category)
                showSheet = false
                scope.launch { snackbarHostState.showSnackbar("\"$name\" sudah tayang di MarketSiswa 🎉") }
            }
        )
    }
}

@Composable
private fun HomeHeader(
    query: String,
    selected: Category?,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (Category?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column {
            Text("Halo, Siswa! 👋", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Temukan produk kreatif dari teman-temanmu.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cari produk...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Hapus pencarian")
                    }
                }
            },
            singleLine = true,
            shape = MaterialTheme.shapes.large
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            item {
                FilterChip(
                    selected = selected == null,
                    onClick = { onCategorySelected(null) },
                    label = { Text("Semua") }
                )
            }
            items(Category.entries.size) { index ->
                val category = Category.entries[index]
                FilterChip(
                    selected = selected == category,
                    onClick = { onCategorySelected(category) },
                    label = { Text(category.label) }
                )
            }
        }
        Spacer(Modifier.height(0.dp))
    }
}
