package com.example.newsapplication.presentation.allNews.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsapplication.R
import com.example.newsapplication.presentation.allNews.AllNewsViewModel
import org.koin.androidx.compose.getViewModel

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