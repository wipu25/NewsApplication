package com.example.newsapplication

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.presentation.newsArticle.NewsArticleScreen
import com.example.newsapplication.presentation.ui.theme.NewsApplicationTheme
import org.junit.Rule
import org.junit.Test
import java.util.*

class NewsArticleScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun checkContentDisplay() {
        val testArticle = Article(
            author = "Steve",
            title = "1 man",
            content = null,
            description = null,
            publishedAt = Date(),
            source = null,
            url = null,
            urlToImage = null
        )
        composeTestRule.setContent {
            NewsApplicationTheme {
                NewsArticleScreen(testArticle)
            }
        }

        composeTestRule.onNodeWithText("Steve, Unknown").assertIsDisplayed()
        composeTestRule.onNodeWithText("1 man").assertIsDisplayed()
    }
}