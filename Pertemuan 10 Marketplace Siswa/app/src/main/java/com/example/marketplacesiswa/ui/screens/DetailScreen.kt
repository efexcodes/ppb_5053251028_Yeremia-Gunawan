package com.example.marketplacesiswa.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.marketplacesiswa.data.Product
import com.example.marketplacesiswa.data.formatRupiah
import com.example.marketplacesiswa.ui.components.CategoryTag
import com.example.marketplacesiswa.ui.components.EmptyState
import com.example.marketplacesiswa.ui.components.FavoriteButton
import com.example.marketplacesiswa.ui.components.ProductImage
import com.example.marketplacesiswa.ui.components.QuantityStepper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    product: Product?,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onAddToCart: (Int) -> Unit,
    onOpenCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    var quantity by rememberSaveable { mutableIntStateOf(1) }
    var showAddedDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detail Produk") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    if (product != null) FavoriteButton(isFavorite, onToggleFavorite)
                }
            )
        },
        bottomBar = {
            if (product != null) {
                Surface(tonalElevation = 3.dp, shadowElevation = 6.dp) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                onAddToCart(quantity)
                                showAddedDialog = true
                            },
                            modifier = Modifier
                                .widthIn(max = 640.dp)
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                            Spacer(Modifier.size(8.dp))
                            Text("Tambah ke Keranjang • ${formatRupiah(product.price * quantity)}")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (product == null) {
            EmptyState(
                emoji = "😕",
                title = "Produk tidak ditemukan",
                message = "Produk ini mungkin sudah tidak tersedia.",
                modifier = Modifier.padding(innerPadding)
            )
            return@Scaffold
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 640.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProductImage(
                    emoji = product.emoji,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    emojiSize = 96
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CategoryTag(product.category.label)
                    Text(
                        formatRupiah(product.price),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(product.name, style = MaterialTheme.typography.headlineMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(
                        "Dijual oleh ${product.seller}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Text("Deskripsi", style = MaterialTheme.typography.titleMedium)
                Text(
                    product.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Jumlah", style = MaterialTheme.typography.titleMedium)
                    QuantityStepper(
                        quantity = quantity,
                        onDecrease = { if (quantity > 1) quantity-- },
                        onIncrease = { if (quantity < 99) quantity++ }
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }

    if (showAddedDialog && product != null) {
        AlertDialog(
            onDismissRequest = { showAddedDialog = false },
            icon = {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("Berhasil Ditambahkan!", textAlign = TextAlign.Center) },
            text = {
                Text(
                    "${quantity}x ${product.name} telah ditambahkan ke keranjang.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(onClick = {
                    showAddedDialog = false
                    onOpenCart()
                }) { Text("Lihat Keranjang") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showAddedDialog = false
                    onBack()
                }) { Text("Lanjut Belanja") }
            }
        )
    }
}
