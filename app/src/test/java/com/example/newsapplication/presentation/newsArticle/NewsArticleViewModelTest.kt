package com.example.newsapplication.presentation.newsArticle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.usecase.SelectArticleUseCase
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import kotlin.test.assertEquals

class NewsArticleViewModelTest {
    private val mockSelectArticleUseCase = Mockito.mock(SelectArticleUseCase::class.java)
    private val newsArticleViewModel = NewsArticleViewModel(mockSelectArticleUseCase)

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `Test Search Query`() {
        Mockito.`when`(mockSelectArticleUseCase.selectedArticle).thenReturn(Article().apply {
            author = "me"
        })
        assertEquals(newsArticleViewModel.selectArticle.author , "me")
    }
}