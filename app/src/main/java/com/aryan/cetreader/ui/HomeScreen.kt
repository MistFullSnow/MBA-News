package com.aryan.cetreader.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aryan.cetreader.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    var showThemeDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadArticles()
    }

    val articles = viewModel.articles.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CET Reader") },
                actions = {
                    IconButton(onClick = { showThemeDialog = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            FeaturedCard(article = articles.firstOrNull())

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(articles.drop(1)) { article ->
                    NewsCard(
                        item = NewsItem(
                            title = article.title,
                            source = article.source,
                            time = article.pubDate
                        )
                    )
                }
            }
        }
    }

    if (showThemeDialog) {
        ThemeDialog(
            selectedTheme = currentTheme,
            onThemeSelected = {
                onThemeChange(it)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}
