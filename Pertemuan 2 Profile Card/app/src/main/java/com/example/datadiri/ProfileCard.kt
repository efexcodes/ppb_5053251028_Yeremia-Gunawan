package com.example.datadiri

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.datadiri.ui.theme.AccentBlue
import com.example.datadiri.ui.theme.CardBackground
import com.example.datadiri.ui.theme.DataDiriTheme
import com.example.datadiri.ui.theme.InfoIconBackground
import com.example.datadiri.ui.theme.ItsBlue
import com.example.datadiri.ui.theme.ItsBlueLight
import com.example.datadiri.ui.theme.PlaceholderGray
import com.example.datadiri.ui.theme.TextPrimary
import com.example.datadiri.ui.theme.TextSecondary

@Composable
fun ProfileCardScreen(
    modifier: Modifier = Modifier,
    logoPainter: Painter? = null,
    photoPainter: Painter? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(ItsBlue, ItsBlueLight)))
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        CampusHeader(
            logoPainter = logoPainter,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        ProfileDataCard(
            name = "Yeremia Gunawan",
            nrp = "5053251028",
            prodi = "Rekayasa Perangkat Lunak",
            status = "Mahasiswa",
            semester = "Semester 3",
            photoPainter = photoPainter,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun CampusHeader(logoPainter: Painter?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (logoPainter != null) {
            Image(
                painter = logoPainter,
                contentDescription = "Logo Institut Teknologi Sepuluh Nopember",
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(92.dp)
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(CardBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ITS",
                        color = ItsBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Text(
                    text = "INSTITUT TEKNOLOGI\nSEPULUH NOPEMBER",
                    color = CardBackground,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun ProfileDataCard(
    name: String,
    nrp: String,
    prodi: String,
    status: String,
    semester: String,
    photoPainter: Painter?,
    modifier: Modifier = Modifier,
) {
    val photoSize = 100.dp
    val photoRadius = photoSize / 2

    Box(modifier = modifier) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = photoRadius),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = photoRadius + 16.dp, bottom = 24.dp)
            ) {
                Text(
                    text = "PROFIL MAHASISWA",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    ProfileInfoRow(icon = Icons.Default.Person, label = "Nama", value = name)
                    ProfileInfoRow(icon = Icons.Default.Badge, label = "NRP", value = nrp)
                    ProfileInfoRow(
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        label = "Program Studi",
                        value = prodi
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(color = PlaceholderGray, thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatusChip(icon = Icons.Default.Groups, label = status)
                    StatusChip(icon = Icons.Default.CalendarMonth, label = semester)
                }
            }
        }

        ProfilePhoto(
            photoPainter = photoPainter,
            size = photoSize,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun ProfilePhoto(
    photoPainter: Painter?,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(CardBackground)
            .padding(3.dp)
            .clip(CircleShape)
            .background(PlaceholderGray),
        contentAlignment = Alignment.Center
    ) {
        if (photoPainter != null) {
            Image(
                painter = photoPainter,
                contentDescription = "Foto Profil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Foto Profil",
                modifier = Modifier.size(size / 2),
                tint = CardBackground
            )
        }
    }
}

@Composable
private fun ProfileInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(InfoIconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = AccentBlue,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.size(14.dp))

        Column {
            Text(text = label, fontSize = 14.sp, color = TextSecondary)
            Text(
                text = value,
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }
    }
}

@Composable
private fun StatusChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = AccentBlue,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = label, fontSize = 16.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun ProfileCardScreenPreview() {
    DataDiriTheme {
        ProfileCardScreen(
            logoPainter = androidx.compose.ui.res.painterResource(id = R.drawable.logo_its),
            photoPainter = androidx.compose.ui.res.painterResource(id = R.drawable.foto_profil)
        )
    }
}
