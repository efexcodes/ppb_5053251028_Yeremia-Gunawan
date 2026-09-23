package com.example.yerefood

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.yerefood.ui.theme.FoodGreenSurface
import com.example.yerefood.ui.theme.FoodRed
import com.example.yerefood.ui.theme.YereFoodTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YereFoodTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FoodOrderingScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class MenuItem(
    val name: String,
    val price: Int,
    val imageUrl: String
)

private val mainDish = MenuItem(
    name = "Nasi Goreng Spesial",
    price = 25_000,
    imageUrl = "https://images.unsplash.com/photo-1512058564366-18510be2db19?w=800&q=80"
)

private val otherMenuItems = listOf(
    MenuItem(
        name = "Burger Spesial",
        price = 32_000,
        imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400&q=80"
    ),
    MenuItem(
        name = "Chicken Rice Bowl",
        price = 28_000,
        imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400&q=80"
    ),
    MenuItem(
        name = "Minuman Segar",
        price = 12_000,
        imageUrl = "https://images.unsplash.com/photo-1544145945-f90425340c7e?w=400&q=80"
    )
)

private fun formatRupiah(amount: Int): String {
    val raw = amount.toString()
    val builder = StringBuilder()
    for ((index, char) in raw.reversed().withIndex()) {
        if (index != 0 && index % 3 == 0) builder.append('.')
        builder.append(char)
    }
    return "Rp " + builder.reverse().toString()
}

@Composable
fun FoodOrderingScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var quantity by rememberSaveable { mutableIntStateOf(1) }
    var cartCount by rememberSaveable { mutableIntStateOf(0) }
    var isFavorite by rememberSaveable { mutableStateOf(false) }
    var showMenu by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            AppHeader(cartCount = cartCount)

            Spacer(modifier = Modifier.height(20.dp))

            FoodDetailCard(
                item = mainDish,
                quantity = quantity,
                isFavorite = isFavorite,
                onIncrease = { quantity++ },
                onDecrease = { if (quantity > 1) quantity-- },
                onAddToCart = {
                    cartCount += quantity
                    Toast.makeText(
                        context,
                        "${quantity}x ${mainDish.name} ditambahkan ke keranjang",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onToggleMenu = { showMenu = !showMenu },
                onToggleFavorite = { isFavorite = !isFavorite }
            )

            Spacer(modifier = Modifier.height(20.dp))

            AnimatedVisibility(visible = showMenu) {
                MenuList(items = otherMenuItems)
            }
        }
    }
}

@Composable
private fun AppHeader(cartCount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "🍔", fontSize = 26.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Yere Food",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.weight(1f))
        Box {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Keranjang",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
            if (cartCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(FoodRed),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cartCount.toString(),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun FoodDetailCard(
    item: MenuItem,
    quantity: Int,
    isFavorite: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onAddToCart: () -> Unit,
    onToggleMenu: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 10f)
            )

            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = item.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatRupiah(item.price),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Nasi goreng dengan telur, ayam, dan sayuran segar.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    QuantityButton(icon = Icons.Filled.Remove, onClick = onDecrease)
                    Text(
                        text = quantity.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(24.dp),
                        textAlign = TextAlign.Center
                    )
                    QuantityButton(icon = Icons.Filled.Add, onClick = onIncrease)

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Total: ${formatRupiah(item.price * quantity)}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onAddToCart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tambah ke Keranjang")
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onToggleMenu,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.RestaurantMenu, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Lihat Menu")
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, FoodRed),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FoodRed)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Favorit")
                }
            }
        }
    }
}

@Composable
private fun QuantityButton(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(FoodGreenSurface)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun MenuList(items: List<MenuItem>) {
    Column {
        Text(
            text = "Menu Lainnya",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        items.forEach { menuItem ->
            MenuRow(item = menuItem)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun MenuRow(item: MenuItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(10.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Text(
                    text = formatRupiah(item.price),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
fun FoodOrderingScreenPreview() {
    YereFoodTheme {
        FoodOrderingScreen()
    }
}
