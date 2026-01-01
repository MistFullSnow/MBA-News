package com.aryan.cetreader.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryan.cetreader.data.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = NewsRepository()

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    fun loadArticles(rssUrl: String) {
        viewModelScope.launch {
            val data = repository.fetchArticles(rssUrl)
            _articles.value = data
        }
    }
}
