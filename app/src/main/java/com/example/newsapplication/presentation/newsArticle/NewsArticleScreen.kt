package com.example.newsapplication.presentation.newsArticle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.example.newsapplication.R
import com.example.newsapplication.di.allNewsModule
import com.example.newsapplication.di.newsArticleModule
import com.example.newsapplication.presentation.ui.theme.Shapes
import com.example.newsapplication.presentation.ui.theme.Typography
import com.example.newsapplication.utils.DateConverter
import org.koin.androidx.compose.getViewModel
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalGlideComposeApi::class, KoinExperimentalAPI::class)
@Composable
fun NewsArticleScreen(newsArticleViewModel: NewsArticleViewModel = getViewModel()) {
    rememberKoinModules(unloadOnForgotten = true, modules = { listOf(newsArticleModule) })
    val article = newsArticleViewModel.selectArticle
    val uriHandler = LocalUriHandler.current
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.Top) {
            Text(
                text = article.title ?: stringResource(id = R.string.no_article),
                style = Typography.h3
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (article.urlToImage != null)
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    GlideSubcomposition(
                        article.urlToImage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                    ) {
                        when (state) {
                            RequestState.Failure, RequestState.Loading -> CircularProgressIndicator(
                                modifier = Modifier.height(40.dp)
                            )

                            is RequestState.Success -> Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painter,
                                contentDescription = null,
                            )
                        }
                    }
                } else
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(color = Color.White, shape = Shapes.large)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    Image(
                        painter = painterResource(
                            R.drawable.globe
                        ),
                        contentScale = ContentScale.FillHeight,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            Spacer(modifier = Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = "${article.author ?: " ${stringResource(id = R.string.unknown)} "}, ${
                        article.source ?: stringResource(
                            id = R.string.unknown
                        )
                    }", style = Typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = if (article.publishedAt != null) DateConverter.stringToTime(article.publishedAt!!)
                    else stringResource(id = R.string.unknown),
                    style = Typography.body2,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = article.description ?: stringResource(id = R.string.empty),
                style = Typography.body1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Divider(color = Color.Black, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "    ${article.content ?: stringResource(id = R.string.empty)}",
                style = Typography.h4
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.more_info),
                style = Typography.subtitle2
            )
            Text(
                text = article.url ?: stringResource(id = R.string.unknown),
                modifier = Modifier
                    .clickable {
                        article.url?.let {
                            uriHandler.openUri(article.url.toString())
                        }
                    },
                style = if (article.url != null) Typography.caption else Typography.subtitle2
            )
        }
    }
}