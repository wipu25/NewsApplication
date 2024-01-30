package com.example.newsapplication.domain.repositories

import com.example.newsapplication.data.network.APIInterface
import com.example.newsapplication.domain.models.AllNews
import com.example.newsapplication.domain.models.SearchQuery
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.test.assertEquals

class NetworkRepositoryTest {
    @Mock
    lateinit var networkLayer: APIInterface
    private lateinit var newsRepository: NewsRepository

    @Before
    internal fun setUp() {
        MockitoAnnotations.initMocks(this)
        newsRepository = NewsRepository(networkLayer)
    }

    @Test
    fun `Get news test success`() {
        runBlocking {
            whenever(networkLayer.getNewsByPage(1,""))
                .thenReturn(Response.success(AllNews(status = "200", totalResult = 0, articles = arrayListOf())))
            val result = newsRepository.getNews(1, SearchQuery())
            assertEquals(result,AllNews(status = "200", totalResult = 0, articles = arrayListOf()))
        }
    }

    @Test(expected = Exception::class)
    fun `Get news test failed`() {
        runBlocking {
            whenever(networkLayer.getNewsByPage(1,""))
                .thenReturn(Response.error(404, ResponseBody.create(null,"Not found")))
            newsRepository.getNews(1, SearchQuery())
        }
    }
}