package com.aryan.cetreader.ui

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView

val isDarkReader = theme == AppTheme.DARK || theme == AppTheme.AMOLED

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ReaderScreen(
    url: String,
    theme: AppTheme,
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

                    private var injected = false
                
                    override fun onPageFinished(view: WebView?, url: String?) {
                        Handler(Looper.getMainLooper()).postDelayed({
                    
                            val js = buildCleanReaderScript(isDarkReader)
                            webView.evaluateJavascript(js, null)
                    
                        }, 3500) // 3.5 seconds delay (as you requested)
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
            console.log("Starting cleanup...");

            function findContent() {
                const potentialSelectors = [
                    '._s30J',
                    '.arttextxml',
                    '.story_content',
                    '.article_content',
                    'div[data-articlebody]',
                    '.main-content'
                ];

                for (let selector of potentialSelectors) {
                    const el = document.querySelector(selector);
                    if (el && el.innerText.length > 200) {
                        console.log("Found content via selector:", selector);
                        return el;
                    }
                }

                let allDivs = document.querySelectorAll('div');
                let maxPCount = 0;
                let bestDiv = null;

                allDivs.forEach(div => {
                    let pCount = div.querySelectorAll('p').length;
                    if (pCount > maxPCount) {
                        maxPCount = pCount;
                        bestDiv = div;
                    }
                });

                if (bestDiv && maxPCount > 3) {
                    console.log("Found content via text density search");
                    return bestDiv;
                }

                return null;
            }

            let headline = "Article";
            const h1 = document.querySelector('h1');
            if (h1) headline = h1.innerText;

            let mainImage = '';
            const imgElement = document.querySelector('._302Yc img, .story_content img, figure img');
            if (imgElement && imgElement.src) {
                mainImage = `<img src="${'$'}{imgElement.src}" style="max-width:100%;border-radius:8px;margin-bottom:20px;">`;
            }

            const articleContent = findContent();
            let textHTML = "<p>Could not auto-detect text. Please reload.</p>";

            if (articleContent) {
                const clone = articleContent.cloneNode(true);

                const junkSelectors = ['.vdo_embedd', '.ad-container', '.twitter-tweet'];
                junkSelectors.forEach(sel => {
                    clone.querySelectorAll(sel).forEach(el => el.remove());
                });

                clone.querySelectorAll('script, style, iframe').forEach(el => el.remove());
                textHTML = clone.innerHTML;
            }

            const darkMode = window.matchMedia('(prefers-color-scheme: dark)').matches;

            document.documentElement.innerHTML = '';
            document.documentElement.innerHTML = '<body></body>';

            const newPage = `
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>${'$'}{headline}</title>
                </head>
                <body style="
                    margin:0;
                    background-color:${'$'}{darkMode ? '#000' : '#f4f4f4'};
                ">
                    <div style="
                        max-width:800px;
                        margin:auto;
                        font-family:Georgia, serif;
                        font-size:18px;
                        line-height:1.65;
                        color:${'$'}{darkMode ? '#EEE' : '#222'};
                        padding:20px;
                        background-color:${'$'}{darkMode ? '#000' : '#fff'};
                        min-height:100vh;
                    ">
                        <h1 style="font-family:Arial; font-size:28px; line-height:1.3;">
                            ${'$'}{headline}
                        </h1>
                        <hr style="border:none;border-top:1px solid ${'$'}{darkMode ? '#222' : '#eee'};">
                        ${'$'}{mainImage}
                        <div>${'$'}{textHTML}</div>
                    </div>
                </body>
            `;

            document.write(newPage);
            document.close();
            console.log("Cleanup complete.");
        })();
    """.trimIndent()

    webView?.evaluateJavascript(script, null)
}
