package com.example.marketplacesiswa.data

enum class Category(val label: String) {
    MAKANAN("Makanan"),
    FASHION("Fashion"),
    KERAJINAN("Kerajinan"),
    ELEKTRONIK("Elektronik"),
    BUKU("Buku")
}

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val category: Category,
    val seller: String,
    val emoji: String
)

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val subtotal: Int get() = product.price * quantity
}

enum class PaymentMethod(val label: String) {
    TRANSFER("Transfer Bank"),
    EWALLET("E-Wallet (OVO, GoPay, DANA)"),
    COD("Bayar di tempat (COD di sekolah)")
}

data class Order(
    val id: Int,
    val items: List<CartItem>,
    val total: Int,
    val buyerName: String,
    val className: String,
    val payment: PaymentMethod,
    val time: String
)

enum class ThemeMode(val label: String) {
    SYSTEM("Ikuti sistem"),
    LIGHT("Light Mode (Terang)"),
    DARK("Dark Mode (Gelap)")
}

/** Format angka menjadi "Rp 15.000". */
fun formatRupiah(amount: Int): String {
    val digits = amount.toString().reversed().chunked(3).joinToString(".").reversed()
    return "Rp $digits"
}
