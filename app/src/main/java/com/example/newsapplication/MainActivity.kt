package com.example.newsapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.newsapplication.di.allNewsModule
import com.example.newsapplication.di.appModule
import com.example.newsapplication.di.newsArticleModule
import com.example.newsapplication.di.newsModule
import com.example.newsapplication.presentation.NewsApp
import com.example.newsapplication.presentation.ui.theme.NewsApplicationTheme
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.compose.KoinApplication
import org.koin.core.context.stopKoin
import org.koin.core.scope.Scope

class MainActivity : ComponentActivity(), AndroidScopeComponent {
    override val scope: Scope by activityScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NewsApplicationTheme {
                KoinApplication(moduleList = {
                    listOf(
                        appModule,
                        newsModule,
                        allNewsModule,
                        newsArticleModule
                    )
                }) {
                    NewsApp()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}