package com.example.newsapplication.presentation.newsArticle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.newsapplication.R
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.presentation.ui.theme.Shapes
import com.example.newsapplication.presentation.ui.theme.Typography
import java.text.DateFormat

@Composable
fun NewsArticleScreen(article: Article?) {
    val uriHandler = LocalUriHandler.current
    Box(
            Modifier.fillMaxSize().background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if(article == null) {
                Text(text = stringResource(id = R.string.something_wrong))
            } else {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = article.title ?: stringResource(id = R.string.no_article),
                        style = Typography.h3
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (article.urlToImage != null)
                        SubcomposeAsyncImage(
                            article.urlToImage,
                            contentDescription = stringResource(id = R.string.news_image),
                            loading = {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            },
                            modifier = Modifier.clip(Shapes.large),
                        ) else
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
                                article.source?.name ?: stringResource(
                                    id = R.string.unknown
                                )
                            }", style = Typography.body2,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT)
                                .format(article.publishedAt)
                                ?: stringResource(id = R.string.unknown),
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
                                    uriHandler.openUri(article.url)
                                }
                            },
                        style = if (article.url != null) Typography.caption else Typography.subtitle2
                    )
                }
            }
        }
}