package com.aryan.cetreader.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aryan.cetreader.ui.model.Article
import com.aryan.cetreader.ui.rss.RssRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = RssRepository()

    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    val articles: StateFlow<List<Article>> = _articles

    fun loadArticles() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repository.fetchRss(
                url = "https://timesofindia.indiatimes.com/rssfeeds/-2128936835.cms",
                source = "Times of India"
            )
            _articles.value = list.take(10)
        }
    }
}
