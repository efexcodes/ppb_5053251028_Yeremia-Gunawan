package com.example.yerefood

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.yerefood.ui.theme.FoodBlue
import com.example.yerefood.ui.theme.FoodGreen
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

data class MenuDetail(
    val category: String,
    val portion: String,
    val prepTime: String,
    val rating: String,
    val ingredients: List<String>
)

private val mainDishDetail = MenuDetail(
    category = "Makanan Utama",
    portion = "1 porsi",
    prepTime = "15 menit",
    rating = "4.8 (120 ulasan)",
    ingredients = listOf(
        "Nasi putih", "Telur", "Ayam suwir", "Wortel", "Kacang polong", "Kecap manis", "Bawang goreng"
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
        ) {
            AppHeader(cartCount = cartCount)

            Column(modifier = Modifier.padding(20.dp)) {
                FoodImage(item = mainDish)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = mainDish.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = formatRupiah(mainDish.price),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Nasi goreng dengan telur, ayam, dan sayuran segar.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    QuantityButton(icon = Icons.Filled.Remove, filled = false, onClick = {
                        if (quantity > 1) quantity--
                    })
                    Text(
                        text = quantity.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(20.dp),
                        textAlign = TextAlign.Center
                    )
                    QuantityButton(icon = Icons.Filled.Add, filled = true, onClick = { quantity++ })
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        cartCount += quantity
                        Toast.makeText(
                            context,
                            "${quantity}x ${mainDish.name} ditambahkan ke keranjang",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FoodGreen)
                ) {
                    Icon(Icons.Filled.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tambah ke Keranjang")
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { showMenu = !showMenu },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, FoodBlue),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FoodBlue)
                ) {
                    Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Lihat Menu")
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
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

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(visible = showMenu) {
                    MenuDetailSection(detail = mainDishDetail)
                }
            }
        }
    }
}

@Composable
private fun AppHeader(cartCount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(FoodGreen)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🍔", fontSize = 22.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Yere Food",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Box {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Keranjang",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                if (cartCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(16.dp)
                            .clip(RoundedCornerShape(50))
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
}

@Composable
private fun FoodImage(item: MenuItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f)
        )
    }
}

@Composable
private fun QuantityButton(icon: ImageVector, filled: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(40.dp)
            .clip(shape)
            .background(if (filled) FoodBlue else Color.White)
            .border(width = 1.dp, color = if (filled) FoodBlue else Color.LightGray, shape = shape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (filled) Color.White else Color.DarkGray,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun MenuDetailSection(detail: MenuDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Detail Menu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            DetailRow(label = "Kategori", value = detail.category)
            Spacer(modifier = Modifier.height(6.dp))
            DetailRow(label = "Porsi", value = detail.portion)
            Spacer(modifier = Modifier.height(6.dp))
            DetailRow(label = "Waktu Saji", value = detail.prepTime)
            Spacer(modifier = Modifier.height(6.dp))
            DetailRow(label = "Rating", value = "⭐ ${detail.rating}")

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Bahan-bahan",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = detail.ingredients.joinToString(", "),
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.width(100.dp))
        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
fun FoodOrderingScreenPreview() {
    YereFoodTheme {
        FoodOrderingScreen()
    }
}
