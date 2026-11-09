package com.example.marketplacesiswa.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.marketplacesiswa.data.PaymentMethod
import com.example.marketplacesiswa.data.formatRupiah
import com.example.marketplacesiswa.ui.components.PriceRow
import com.example.marketplacesiswa.viewmodel.MarketUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    state: MarketUiState,
    onBack: () -> Unit,
    onPlaceOrder: (name: String, className: String, payment: PaymentMethod) -> Unit,
    onViewOrders: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by rememberSaveable { mutableStateOf("") }
    var className by rememberSaveable { mutableStateOf("") }
    var payment by rememberSaveable { mutableStateOf(PaymentMethod.COD) }
    var submitted by rememberSaveable { mutableStateOf(false) }
    var showConfirm by rememberSaveable { mutableStateOf(false) }
    var showSuccess by rememberSaveable { mutableStateOf(false) }

    val nameError = submitted && name.isBlank()
    val classError = submitted && className.isBlank()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { innerPadding ->
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Data Pemesan", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama lengkap") },
                    singleLine = true,
                    isError = nameError,
                    supportingText = { if (nameError) Text("Nama wajib diisi") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = className,
                    onValueChange = { className = it },
                    label = { Text("Kelas (mis. XI IPA 2)") },
                    singleLine = true,
                    isError = classError,
                    supportingText = { if (classError) Text("Kelas wajib diisi, untuk tempat serah terima") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Metode Pembayaran", style = MaterialTheme.typography.titleMedium)
                Column(Modifier.selectableGroup()) {
                    PaymentMethod.entries.forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = payment == method,
                                    onClick = { payment = method },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = payment == method, onClick = null)
                            Spacer(Modifier.size(12.dp))
                            Text(method.label, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                Text("Ringkasan Pesanan", style = MaterialTheme.typography.titleMedium)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.cart.forEach { item ->
                            PriceRow(
                                label = "${item.product.name}  x${item.quantity}",
                                value = formatRupiah(item.subtotal)
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        PriceRow("Ongkir", formatRupiah(state.shipping))
                        PriceRow("Total Pembayaran", formatRupiah(state.total), emphasized = true)
                    }
                }

                Button(
                    onClick = {
                        submitted = true
                        if (name.isNotBlank() && className.isNotBlank() && state.cart.isNotEmpty()) {
                            showConfirm = true
                        }
                    },
                    enabled = state.cart.isNotEmpty() || showSuccess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) { Text("Buat Pesanan") }
            }
        }
    }

    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            title = { Text("Konfirmasi Pesanan") },
            text = {
                Text("Buat pesanan senilai ${formatRupiah(state.total)} dengan pembayaran ${payment.label}?")
            },
            confirmButton = {
                Button(onClick = {
                    showConfirm = false
                    onPlaceOrder(name, className, payment)
                    showSuccess = true
                }) { Text("Ya, Pesan") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) { Text("Batal") }
            }
        )
    }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = {},
            icon = {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(56.dp)
                )
            },
            title = { Text("Pesanan Berhasil!", textAlign = TextAlign.Center) },
            text = {
                Text(
                    "Terima kasih, $name. Penjual akan menghubungi kamu untuk serah terima di sekolah.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(onClick = {
                    showSuccess = false
                    onViewOrders()
                }) { Text("Lihat Pesanan") }
            }
        )
    }
}
