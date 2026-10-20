package com.example.registrasiperpustakaan.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.registrasiperpustakaan.data.JenisKeanggotaan
import com.example.registrasiperpustakaan.ui.theme.HijauLabel
import com.example.registrasiperpustakaan.ui.theme.HijauTeks
import com.example.registrasiperpustakaan.ui.theme.IndigoLabel
import com.example.registrasiperpustakaan.ui.theme.IndigoTeks
import com.example.registrasiperpustakaan.ui.theme.KuningLabel
import com.example.registrasiperpustakaan.ui.theme.KuningTeks

fun inisial(nama: String): String =
    nama.trim().split(" ").filter { it.isNotEmpty() }.take(2).joinToString("") { it.first().uppercase() }

@Composable
fun AvatarInisial(nama: String, ukuran: Dp = 44.dp, teks: TextUnit = 16.sp) {
    Box(
        modifier = Modifier
            .size(ukuran)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = inisial(nama),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = teks
        )
    }
}

@Composable
fun LabelJenis(jenis: JenisKeanggotaan, modifier: Modifier = Modifier) {
    val (latar, warna) = when (jenis) {
        JenisKeanggotaan.MAHASISWA -> HijauLabel to HijauTeks
        JenisKeanggotaan.DOSEN -> KuningLabel to KuningTeks
        JenisKeanggotaan.STAF -> IndigoLabel to IndigoTeks
    }
    Text(
        text = jenis.label,
        color = warna,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(latar)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}
