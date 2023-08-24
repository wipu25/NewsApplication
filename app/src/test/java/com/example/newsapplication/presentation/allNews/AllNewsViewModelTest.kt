package com.example.newsapplication.presentation.allNews

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.newsapplication.domain.models.Article
import com.example.newsapplication.domain.models.Category
import com.example.newsapplication.domain.repositories.NetworkRepository
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import kotlin.test.assertEquals

class AllNewsViewModelTest {
    private val mockNetworkRepository = Mockito.mock(NetworkRepository::class.java)
    private val allNewsViewModel = AllNewsViewModel(mockNetworkRepository)

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `Test Search Query`() {
        allNewsViewModel.updateSearch("search")
        assertEquals("search",allNewsViewModel.searchText.value!!)
    }

    @Test
    fun `Test Select new Article`() {
        val testArticle = Article(
            author = "Steve",
            title = "1 man",
            content = null,
            description = null,
            publishedAt = null,
            source = null,
            url = null,
            urlToImage = null
        )
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