package com.aryan.cetreader.ui

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ReaderScreen(
    url: String,
    onClose: () -> Unit
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.loadsImagesAutomatically = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true

                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        injectCleanerScript(view)
                    }
                }

                loadUrl(url)
            }
        }
    )
}
private fun injectCleanerScript(webView: WebView?) {
    val script = """
        (function() {
            const headline = document.querySelector('h1') ? document.querySelector('h1').innerText : '';
            
            let mainImage = '';
            const imgElement = document.querySelector('._302Yc img, .story_content img');
            if (imgElement) {
                mainImage = `<img src="${'$'}{imgElement.src}" style="max-width:100%;border-radius:10px;margin-bottom:20px;">`;
            }

            let articleContent = document.querySelector('._s30J, .arttextxml');
            let textHTML = '';

            if (articleContent) {
                articleContent.querySelectorAll('.vdo_embedd, iframe, script, style, ads').forEach(e => e.remove());
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

            document.body.innerHTML = `
                <div style="
                    max-width: 760px;
                    margin: auto;
                    font-family: Georgia, serif;
                    font-size: 19px;
                    line-height: 1.65;
                    padding: 24px;
                    color: #111;
                ">
                    <h1 style="font-family: Arial; font-size: 30px;">${'$'}{headline}</h1>
                    ${'$'}{mainImage}
                    <div style="text-align: justify;">${'$'}{textHTML}</div>
                </div>
            `;
        })();
    """.trimIndent()

    webView?.evaluateJavascript(script, null)
}
