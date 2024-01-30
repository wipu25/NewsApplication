package com.example.newsapplication.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.usecase.SelectArticleUseCase
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals

class SelectArticleUseCaseTest {
    private val selectArticleUseCase = SelectArticleUseCase()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `Test select article` () {
        selectArticleUseCase.setSelectedArticle(Article().apply { author = "test" })
        assertEquals(selectArticleUseCase.selectedArticle.author, "test")
    }
}