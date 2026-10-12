package com.example.studentmanager.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/*
 * Overlay ini digambar di dalam pohon Compose yang sama dengan layar (bukan jendela Popup/Dialog
 * terpisah), sehingga selalu menerima sentuhan dan tidak bergantung pada fokus jendela perangkat.
 */

/** Lapisan layar penuh yang menutup dirinya saat area di luar konten disentuh. */
@Composable
private fun Scrim(
    onDismiss: () -> Unit,
    dimmed: Boolean,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (dimmed) Color.Black.copy(alpha = 0.5f) else Color.Transparent)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss,
            ),
        content = content,
    )
}

/** Modifier agar sentuhan pada konten tidak "tembus" ke scrim di belakangnya. */
@Composable
private fun Modifier.consumeTaps(): Modifier = clickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = null,
    onClick = {},
)

/** Menu overflow yang menempel di pojok kanan atas, tepat di bawah app bar. */
@Composable
fun OverlayMenu(onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Scrim(onDismiss = onDismiss, dimmed = false) {
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 56.dp, end = 8.dp)
                .width(220.dp)
                .consumeTaps(),
            shape = ButtonShape,
            tonalElevation = 3.dp,
            shadowElevation = 8.dp,
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp), content = content)
        }
    }
}

/** Dialog konfirmasi di tengah layar dengan latar gelap. */
@Composable
fun OverlayDialog(
    onDismiss: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    title: String,
    text: String,
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)? = null,
) {
    Scrim(onDismiss = onDismiss, dimmed = true) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .consumeTaps(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (icon != null) {
                    icon()
                    Spacer(Modifier.height(12.dp))
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (dismissButton != null) {
                        Box(Modifier.weight(1f)) { dismissButton() }
                    }
                    Box(Modifier.weight(1f)) { confirmButton() }
                }
            }
        }
    }
}
