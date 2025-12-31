package com.aryan.cetreader.ui

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import com.aryan.cetreader.ui.model.Article
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    LaunchedEffect(Unit) {
        viewModel.loadArticles()
    }
    val articles = viewModel.articles.collectAsState().value
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {


    var showThemeDialog by remember { mutableStateOf(false) }

    val bgColor = Color(0xFFF7F9FC)

    Scaffold(        
        topBar = {
                TopAppBar(
                    title = { Text("CET Reader") },
                    actions = {
                        IconButton(onClick = { showThemeDialog = true }) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "Settings"
                            )
                        }
                    }
                )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(bgColor)
                .fillMaxSize()
        ) {

            FeaturedCard()

            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(fakeNews) {
                    NewsCard(it)
                }
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
