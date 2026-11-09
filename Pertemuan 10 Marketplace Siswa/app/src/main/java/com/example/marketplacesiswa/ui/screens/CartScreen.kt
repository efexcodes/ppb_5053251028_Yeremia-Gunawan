package com.example.marketplacesiswa.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.marketplacesiswa.data.CartItem
import com.example.marketplacesiswa.data.formatRupiah
import com.example.marketplacesiswa.ui.components.EmptyState
import com.example.marketplacesiswa.ui.components.PriceRow
import com.example.marketplacesiswa.ui.components.ProductImage
import com.example.marketplacesiswa.ui.components.QuantityStepper
import com.example.marketplacesiswa.viewmodel.MarketUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    state: MarketUiState,
    onBack: () -> Unit,
    onChangeQuantity: (productId: Int, delta: Int) -> Unit,
    onRemove: (Int) -> Unit,
    onCheckout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Keranjang") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            if (state.cart.isNotEmpty()) {
                Surface(tonalElevation = 3.dp, shadowElevation = 6.dp) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .widthIn(max = 640.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PriceRow("Subtotal", formatRupiah(state.subtotal))
                            PriceRow("Ongkir", formatRupiah(state.shipping))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                            PriceRow("Total", formatRupiah(state.total), emphasized = true)
                            Spacer(Modifier.height(4.dp))
                            Button(
                                onClick = onCheckout,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                            ) { Text("Checkout") }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (state.cart.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    emoji = "🛒",
                    title = "Keranjang masih kosong",
                    message = "Yuk, cari produk menarik dari teman-temanmu."
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    modifier = Modifier
                        .widthIn(max = 640.dp)
                        .fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.cart, key = { it.product.id }) { item ->
                        CartRow(
                            item = item,
                            onDecrease = { onChangeQuantity(item.product.id, -1) },
                            onIncrease = { onChangeQuantity(item.product.id, 1) },
                            onRemove = { onRemove(item.product.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CartRow(
    item: CartItem,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImage(
                emoji = item.product.emoji,
                modifier = Modifier.size(72.dp),
                emojiSize = 34
            )
            Spacer(Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    formatRupiah(item.product.price),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                QuantityStepper(
                    quantity = item.quantity,
                    onDecrease = onDecrease,
                    onIncrease = onIncrease
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Hapus ${item.product.name}",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
