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
    val isDark = theme == AppTheme.DARK || theme == AppTheme.AMOLED

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
                            evaluateJavascript(
                                buildCleanReaderScript(isDark),
                                null
                            )
                        }, 1500) // short delay ONLY to allow hydration
                    }
                }

                loadUrl(url)
            }
        }
    )
}

fun buildCleanReaderScript(isDark: Boolean): String {
    return """
        (function() {

            const isDark = ${isDark};

            function waitUntilReady(callback) {
                let attempts = 0;
                const interval = setInterval(() => {
                    const hasText = document.querySelectorAll('p').length > 5;
                    const hasTitle = document.querySelector('h1');
                    if (hasText && hasTitle) {
                        clearInterval(interval);
                        callback();
                    }
                    attempts++;
                    if (attempts > 20) {
                        clearInterval(interval);
                        console.log("Content not detected");
                    }
                }, 300);
            }

            function runCleaner() {

                console.log("Starting cleanup");

                const bgColor = isDark ? '#000' : '#f4f4f4';
                const cardBg = isDark ? '#000' : '#fff';
                const textCol = isDark ? '#eaeaea' : '#222';
                const hrCol = isDark ? '#222' : '#eee';

                function findContent() {
                    const selectors = [
                        '._s30J',
                        '.arttextxml',
                        '.story_content',
                        '.article_content',
                        'div[data-articlebody]'
                    ];
                    for (let s of selectors) {
                        const el = document.querySelector(s);
                        if (el && el.innerText.length > 200) return el;
                    }
                    return null;
                }

                const headline = document.querySelector('h1')?.innerText || 'Article';
                const img = document.querySelector('figure img');
                const imageHTML = img ? `<img src="${'$'}{img.src}" style="max-width:100%;border-radius:8px;margin-bottom:20px;">` : '';

                const content = findContent();
                if (!content) return;

                const clone = content.cloneNode(true);
                clone.querySelectorAll('script, iframe, style, .vdo_embedd, .ad-container').forEach(e => e.remove());

                document.documentElement.innerHTML = `
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    </head>
                    <body style="margin:0;background:${'$'}{bgColor};">
                        <div style="
                            max-width:800px;
                            margin:auto;
                            padding:20px;
                            font-family:Georgia, serif;
                            font-size:18px;
                            line-height:1.6;
                            color:${'$'}{textCol};
                            background:${'$'}{cardBg};
                        ">
                            <h1>${'$'}{headline}</h1>
                            <hr style="border-top:1px solid ${'$'}{hrCol}">
                            ${'$'}{imageHTML}
                            ${'$'}{clone.innerHTML}
                        </div>
                    </body>
                `;
            }

            waitUntilReady(runCleaner);

        })();
    """.trimIndent()
}
