package com.example.newsapplication.presentation.allNews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.CircularProgressIndicator
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapplication.R
import com.example.newsapplication.di.allNewsModule
import com.example.newsapplication.domain.models.Category
import com.example.newsapplication.presentation.allNews.components.CategoryChip
import com.example.newsapplication.presentation.allNews.components.NewsItem
import com.example.newsapplication.presentation.ui.theme.Shapes
import org.koin.androidx.compose.getViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun AllNewsScreen(navigateToArticle: () -> Unit) {
    rememberKoinModules(unloadOnForgotten = true, modules = { listOf(allNewsModule) })
    Box {
        Column {
            SearchRow()
            CategoryRow()
            NewsList(navigateToArticle = navigateToArticle)
        }
    }
}

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
fun NewsList(viewModel: AllNewsViewModel = getViewModel(), navigateToArticle: () -> Unit) {
    val articleListObserve = viewModel.items.observeAsState()
    val articleList = articleListObserve.value!!.collectAsLazyPagingItems()

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