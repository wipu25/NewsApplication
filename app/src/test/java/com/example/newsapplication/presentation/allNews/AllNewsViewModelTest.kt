package com.example.newsapplication.presentation.allNews

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapplication.domain.models.AllNews
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.models.Category
import com.example.newsapplication.domain.repositories.NewsCachingRepository
import com.example.newsapplication.domain.repositories.NewsRepository
import com.example.newsapplication.domain.usecase.SelectArticleUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import kotlin.test.assertEquals

class AllNewsViewModelTest {
    private val mockNewsRepository = Mockito.mock(NewsRepository::class.java)
    private val mockNewsCachingRepository = Mockito.mock(NewsCachingRepository::class.java)
    private val mockSelectArticleUseCase = Mockito.mock(SelectArticleUseCase::class.java)
    private lateinit var allNewsViewModel: AllNewsViewModel

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Before fun setUp() {
        try {
            allNewsViewModel = AllNewsViewModel(mockNewsRepository, mockSelectArticleUseCase, mockNewsCachingRepository)
        } catch (_: Exception) {}
    }

    @Test
    fun `Test Search Query`() {
        allNewsViewModel.updateSearch("search")
        assertEquals("search",allNewsViewModel.searchText.value!!)
    }

    @Test
    fun `Test Select new Article`() {
        val testArticle = Article()
        testArticle.apply {
            author = "Steve"
            title = "1 man"
            content = null
            description = null
            publishedAt = null
            source = null
            url = null
            urlToImage = null
        }
        Mockito.`when`(mockSelectArticleUseCase.selectedArticle).thenReturn(testArticle)
        allNewsViewModel.selectNewArticle(testArticle)
        assertEquals(testArticle.author, allNewsViewModel.selectedArticle.value!!.author)
        assertEquals(testArticle.title, allNewsViewModel.selectedArticle.value!!.title)
    }

    @Test
    fun `Update category`() {
        val category = Category.BUSINESS

        allNewsViewModel.updateCategory(category)
        assertEquals(category,allNewsViewModel.categoryChip.value)
    }
}