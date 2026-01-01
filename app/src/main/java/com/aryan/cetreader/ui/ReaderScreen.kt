package com.aryan.cetreader.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.aryan.cetreader.ui.theme.AppTheme

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ReaderScreen(
    url: String,
    theme: AppTheme,
    onClose: () -> Unit
) {
    val isDarkReader = theme == AppTheme.DARK || theme == AppTheme.AMOLED

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
                        Handler(Looper.getMainLooper()).postDelayed({

                            injectCleanerScript(
                                webView = view,
                                isDark = isDarkReader
                            )

                        }, 3500) // ⏱ 3.5 sec delay as YOU requested
                    }
                }

                loadUrl(url)
            }
        }
    )
}

private fun injectCleanerScript(
    webView: WebView?,
    isDark: Boolean
) {
    val script = """
        (function() {

            const isDark = $isDark;

            const bgColor  = isDark ? '#000000' : '#f4f4f4';
            const cardBg  = isDark ? '#000000' : '#ffffff';
            const textCol = isDark ? '#eaeaea' : '#222';
            const hrCol   = isDark ? '#222' : '#eee';

            function findContent() {
                const selectors = [
                    '._s30J',
                    '.arttextxml',
                    '.story_content',
                    '.article_content',
                    'div[data-articlebody]',
                    '.main-content'
                ];

                for (let s of selectors) {
                    const el = document.querySelector(s);
                    if (el && el.innerText.length > 200) return el;
                }

                let best = null;
                let maxP = 0;
                document.querySelectorAll('div').forEach(d => {
                    const p = d.querySelectorAll('p').length;
                    if (p > maxP) {
                        maxP = p;
                        best = d;
                    }
                });
                return maxP > 3 ? best : null;
            }

            let headline = document.querySelector('h1')?.innerText || 'Article';

            let mainImage = '';
            const img = document.querySelector('figure img, .story_content img');
            if (img?.src) {
                mainImage = `<img src="${'$'}{img.src}" style="max-width:100%;border-radius:8px;margin-bottom:20px;">`;
            }

            const content = findContent();
            let bodyHtml = '<p>Unable to extract article.</p>';

            if (content) {
                const clone = content.cloneNode(true);
                clone.querySelectorAll('script, style, iframe, .vdo_embedd, .ad-container').forEach(e => e.remove());
                bodyHtml = clone.innerHTML;
            }

            document.documentElement.innerHTML = '';
            document.documentElement.innerHTML = `
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>${'$'}{headline}</title>
                </head>
                <body style="margin:0;background:${'$'}{bgColor};">
                    <div style="
                        max-width:800px;
                        margin:auto;
                        padding:20px;
                        background:${'$'}{cardBg};
                        color:${'$'}{textCol};
                        font-family:Georgia,serif;
                        line-height:1.6;
                        min-height:100vh;">
                        <h1 style="font-family:Arial;">${'$'}{headline}</h1>
                        <hr style="border-top:1px solid ${'$'}{hrCol};">
                        ${'$'}{mainImage}
                        ${'$'}{bodyHtml}
                    </div>
                </body>
            `;

        })();
    """.trimIndent()

    webView?.evaluateJavascript(script, null)
}
