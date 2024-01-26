package com.example.newsapplication.presentation.allNews.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.newsapplication.domain.models.Category
import com.example.newsapplication.presentation.allNews.AllNewsViewModel
import com.example.newsapplication.presentation.ui.theme.Shapes
import com.example.newsapplication.presentation.ui.theme.categoryChip
import org.koin.androidx.compose.getViewModel

@Composable
fun CategoryRow(viewModel: AllNewsViewModel = getViewModel()) {

    val categoryScrollState = rememberScrollState()
    val categoryChip = viewModel.categoryChip.observeAsState()

    Row(
        Modifier
            .fillMaxWidth()
            .horizontalScroll(categoryScrollState),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Category.entries.forEach { category ->
            CategoryChip(
                isSelected = category == categoryChip.value,
                text = category.value,
                onChecked = {
                    viewModel.updateCategory(category)
                })
        }
    }
}

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