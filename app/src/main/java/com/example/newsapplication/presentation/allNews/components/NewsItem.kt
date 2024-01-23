package com.example.newsapplication.presentation.allNews.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.example.newsapplication.R
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.presentation.ui.theme.Shapes
import com.example.newsapplication.utils.DateConverter

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NewsItem(article: Article, onClick: () -> Unit) {
    Box(
        Modifier
            .padding(8.dp)
            .clip(shape = Shapes.medium)
            .background(color = Color.White)
            .clickable {
                onClick.invoke()
            }
    )
    {
        Row(
            Modifier
                .padding(16.dp)
                .height(82.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            GlideImage(
                loading = placeholder(R.drawable.globe),
                model = article.urlToImage,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .width(120.dp)
                    .height(80.dp)
            )
            Column(
                Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    fontSize = 16.sp,
                    text = article.title ?: stringResource(id = R.string.no_article),
                )
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        article.author ?: stringResource(id = R.string.unknown),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = DateConverter.stringToTime(article.publishedAt ?: ""),
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}