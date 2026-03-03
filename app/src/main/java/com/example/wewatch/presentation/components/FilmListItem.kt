package com.example.wewatch.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wewatch.data.local.FilmEntity

@Composable
fun FilmListItem(
    film: FilmEntity,
    onCheckChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Постер
            AsyncImage(
                model = film.posterUrl.ifEmpty { "https://via.placeholder.com/60x90?text=No+Poster" },
                contentDescription = film.title,
                modifier = Modifier
                    .size(60.dp, 90.dp),
                contentScale = ContentScale.Crop
            )

            // Информация о фильме
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = film.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Text(
                    text = film.year,
                    style = MaterialTheme.typography.bodyMedium
                )
                film.genre?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Чекбокс для выбора
            Checkbox(
                checked = film.isSelected,
                onCheckedChange = onCheckChanged
            )
        }
    }
}