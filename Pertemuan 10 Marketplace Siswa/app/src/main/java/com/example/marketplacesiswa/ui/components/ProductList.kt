package com.example.marketplacesiswa.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.marketplacesiswa.data.Product

/**
 * Daftar produk. Di layar sempit memakai LazyColumn, di layar lebar (tablet / landscape)
 * otomatis menjadi grid adaptif supaya layout tetap proporsional.
 */
@Composable
fun ProductList(
    products: List<Product>,
    favoriteIds: Set<Int>,
    wide: Boolean,
    onToggleFavorite: (Int) -> Unit,
    onOpenDetail: (Int) -> Unit,
    modifier: Modifier = Modifier,
    header: (@Composable () -> Unit)? = null,
    empty: (@Composable () -> Unit)? = null
) {
    val padding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp)
    val spacing = Arrangement.spacedBy(16.dp)

    if (wide) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 340.dp),
            modifier = modifier.fillMaxSize(),
            contentPadding = padding,
            verticalArrangement = spacing,
            horizontalArrangement = spacing
        ) {
            if (header != null) {
                item(span = { GridItemSpan(maxLineSpan) }) { header() }
            }
            if (products.isEmpty() && empty != null) {
                item(span = { GridItemSpan(maxLineSpan) }) { empty() }
            }
            items(products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    isFavorite = product.id in favoriteIds,
                    onToggleFavorite = { onToggleFavorite(product.id) },
                    onOpenDetail = { onOpenDetail(product.id) }
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = padding,
            verticalArrangement = spacing
        ) {
            if (header != null) {
                item { header() }
            }
            if (products.isEmpty() && empty != null) {
                item { empty() }
            }
            items(products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    isFavorite = product.id in favoriteIds,
                    onToggleFavorite = { onToggleFavorite(product.id) },
                    onOpenDetail = { onOpenDetail(product.id) }
                )
            }
        }
    }
}
