package com.example.newsapplication.presentation.allNews.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.newsapplication.R
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.presentation.ui.theme.Shapes
import java.text.DateFormat

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
            Image(
                painter = if (article.urlToImage != null) rememberAsyncImagePainter(article.urlToImage) else painterResource(
                    R.drawable.globe
                ),
                contentScale = ContentScale.FillHeight,
                contentDescription = null,
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
                        text = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(article.publishedAt) ?: "Unknown",
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}