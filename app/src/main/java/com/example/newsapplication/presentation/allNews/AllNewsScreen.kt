package com.example.newsapplication.presentation.allNews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapplication.R
import com.example.newsapplication.domain.models.Category
import com.example.newsapplication.presentation.allNews.components.CategoryChip
import com.example.newsapplication.presentation.allNews.components.NewsItem
import com.example.newsapplication.presentation.ui.theme.Shapes

@Composable
fun AllNewsScreen(viewModel: AllNewsViewModel, navigateToArticle: () -> Unit) {
    var reload by remember { mutableStateOf(false) }
    val categoryScrollState = rememberScrollState()

    var articleList = viewModel.getNews().collectAsLazyPagingItems()
    val searchText = viewModel.searchText.observeAsState("")
    val categoryChip = viewModel.categoryChip.observeAsState()

    if (reload) {
        articleList = viewModel.getNews().collectAsLazyPagingItems()
        reload = false
    }

    Box {
        Column {
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
                        reload = true
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
            Row(
                Modifier
                    .fillMaxWidth()
                    .horizontalScroll(categoryScrollState),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Category.values().forEach { category ->
                    CategoryChip(
                        isSelected = category == categoryChip.value,
                        text = category.value,
                        onChecked = {
                            viewModel.updateCategory(category)
                            reload = true
                        })
                }
            }
            LazyColumn {
                items(
                    count = articleList.itemCount,
                ) { index ->
                    articleList[index]?.let {
                        NewsItem(article = articleList[index]!!, onClick = {
                            viewModel.selectNewArticle(articleList[index]!!)
                            navigateToArticle.invoke()
                        })
                    }
                }
                when (articleList.loadState.refresh) { //FIRST LOAD
                    is LoadState.Error -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillParentMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(8.dp),
                                    text = stringResource(id = R.string.error_loading),
                                    color = Color.Red
                                )
                            }
                        }
                    }
                    is LoadState.Loading -> { // Loading UI
                        item {
                            Column(
                                modifier = Modifier
                                    .fillParentMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(8.dp),
                                    text = stringResource(id = R.string.loading)
                                )
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    }
                    else -> {}
                }

                when (articleList.loadState.append) { // Pagination
                    is LoadState.Error -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillParentMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(8.dp),
                                    text = stringResource(id = R.string.error_loading)
                                )
                            }
                        }
                    }
                    is LoadState.Loading -> { // Pagination Loading UI
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Text(text = stringResource(id = R.string.loading_more))
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}