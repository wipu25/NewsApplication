package com.example.newsapplication.presentation.allNews.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.newsapplication.presentation.ui.theme.Shapes
import com.example.newsapplication.presentation.ui.theme.categoryChip

@Composable
fun CategoryChip(
    isSelected: Boolean,
    text: String,
    onChecked: (Boolean) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clip(shape = Shapes.large)
            .background(
                color = if (isSelected) categoryChip else Color.White,
            )
            .clickable {
                onChecked(!isSelected)
            }
    ) {
        Text(
            text = text,
            color = Color.Black,
            modifier = Modifier.padding(4.dp)
        )
    }
}