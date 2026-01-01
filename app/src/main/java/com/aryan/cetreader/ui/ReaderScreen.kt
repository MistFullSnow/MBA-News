package com.aryan.cetreader.ui

import android.annotation.SuppressLint
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@SuppressLint("SetJavaScriptEnabled")

private const val CLEAN_READER_JS = """
(function() {
    const headline = document.querySelector('h1')
        ? document.querySelector('h1').innerText
        : 'No Headline Found';

    let mainImage = '';
    const imgElement = document.querySelector('._302Yc img, .story_content img');
    if (imgElement) {
        mainImage = `<img src="${'$'}{imgElement.src}" style="max-width:100%;height:auto;border-radius:8px;margin-bottom:20px;">`;
    }

    let articleContent = document.querySelector('._s30J, .arttextxml');
    let textHTML = '';

    if (articleContent) {
        const unwantedVideos = articleContent.querySelectorAll('.vdo_embedd');
        unwantedVideos.forEach(v => v.remove());
        textHTML = articleContent.innerHTML;
    } else {
        document.querySelectorAll('p').forEach(p => {
            if (p.innerText.length > 40) {
                textHTML += `<p>${'$'}{p.innerText}</p>`;
            }
        });
    }

    document.body.innerHTML = '';
    document.head.innerHTML = '';

    const newPage = `
        <div style="
            max-width:800px;
            margin:0 auto;
            font-family:Georgia,'Times New Roman',serif;
            font-size:20px;
            line-height:1.6;
            color:#333;
            padding:40px 20px;
            background-color:#f9f9f9;
        ">
            <h1 style="font-family:Arial,sans-serif;font-size:36px;margin-bottom:20px;line-height:1.2;">
                ${'$'}{headline}
            </h1>
            <hr style="border:0;border-top:1px solid #ccc;margin:20px 0;">
            ${'$'}{mainImage}
            <div style="text-align:justify;">
                ${'$'}{textHTML}
            </div>
        </div>
    `;

    document.body.style.backgroundColor = '#f9f9f9';
    document.body.innerHTML = newPage;
})();
"""

@Composable
fun ReaderScreen(
    url: String,
    onClose: () -> Unit
) {
    val view = LocalView.current
    var webView: WebView? by remember { mutableStateOf(null) }

    // 🔹 Enter immersive mode
    LaunchedEffect(Unit) {
        val window = (view.context as android.app.Activity).window
    
        window.attributes.layoutInDisplayCutoutMode =
            android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    
        WindowCompat.setDecorFitsSystemWindows(window, false)
    
        WindowInsetsControllerCompat(window, view).apply {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }


    // 🔹 Restore system bars on exit
    DisposableEffect(Unit) {
        onDispose {
            val window = (view.context as android.app.Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, true)

            WindowInsetsControllerCompat(window, view).show(
                WindowInsetsCompat.Type.systemBars()
            )
        }
    }

    BackHandler {
        if (webView?.canGoBack() == true) {
            webView?.goBack()
        } else {
            onClose()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                
                        view?.evaluateJavascript(
                            "javascript:$CLEAN_READER_JS",
                            null
                        )
                    }
                }

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                overScrollMode = View.OVER_SCROLL_NEVER
                loadUrl(url)
                webView = this
            }
        }
    )
}
