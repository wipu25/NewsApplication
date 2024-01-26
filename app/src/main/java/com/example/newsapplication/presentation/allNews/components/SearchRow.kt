package com.example.newsapplication.presentation.allNews.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.newsapplication.R
import com.example.newsapplication.presentation.allNews.AllNewsViewModel
import com.example.newsapplication.presentation.ui.theme.Shapes
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchRow(viewModel: AllNewsViewModel = getViewModel()) {
    val searchText = viewModel.searchText.observeAsState("")
    Row(
        Modifier.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .padding(all = 8.dp),
            value = searchText.value,
            onValueChange = {
                viewModel.updateSearch(it)
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_search_24),
                    stringResource(id = R.string.search_icon)
                )
            },
            placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
            singleLine = true,
            shape = Shapes.large,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                disabledIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                if (!viewModel.searchText.value.isNullOrEmpty())
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        stringResource(id = R.string.close_icon),
                        Modifier.clickable {
                            viewModel.updateSearch("")
                        }
                    )
            }
        )
    }
}