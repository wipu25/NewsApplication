package com.example.newsapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.newsapplication.di.appModule
import com.example.newsapplication.presentation.NewsApp
import com.example.newsapplication.presentation.ui.theme.NewsApplicationTheme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin

class MainActivity : ComponentActivity() {

//    private val allNewsViewModel: AllNewsViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(appModule)
        }

        setContent {
            NewsApplicationTheme {
                NewsApp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }
}