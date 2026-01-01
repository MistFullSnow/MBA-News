package com.aryan.cetreader.ui

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDark) Color.Black else Color.White
            )
    ) {

        // 🔹 WEBVIEW (Initially hidden)
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.loadsImagesAutomatically = true
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true

                    // Hide WebView initially
                    alpha = 0f

                    webViewClient = object : WebViewClient() {

                        override fun onPageFinished(view: WebView?, url: String?) {
                            Handler(Looper.getMainLooper()).postDelayed({

                                evaluateJavascript(
                                    buildCleanReaderScript(isDark)
                                ) {
                                    // Fade in ONLY after cleanup
                                    animate()
                                        .alpha(1f)
                                        .setDuration(300)
                                        .start()

                                    isLoading = false
                                }

                            }, 1200) // Small delay for dynamic TOI content
                        }
                    }

                    loadUrl(url)
                }
            }
        )

        // 🔹 LOADER OVERLAY
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = if (isDark)
                        Color.White
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


fun buildCleanReaderScript(isDark: Boolean): String {
    return """
        (function() {
            // PASS KOTLIN STATE TO JS
            const isDark = $isDark;

            // 1. ROBUST WAITER
            function waitUntilReady(callback) {
                let attempts = 0;
                // Check every 200ms
                const interval = setInterval(() => {
                    // Look for substantial text
                    const hasText = document.querySelectorAll('p').length > 5;
                    const hasTitle = document.querySelector('h1');
                    
                    if (hasText && hasTitle) {
                        clearInterval(interval);
                        callback();
                    }
                    
                    attempts++;
                    // Stop checking after 6 seconds (30 attempts) to save battery
                    if (attempts > 30) {
                        clearInterval(interval);
                        console.log("Timeout waiting for content");
                        // Try to clean anyway with what we have
                        callback();
                    }
                }, 200);
            }

            // 2. THE CLEANER LOGIC
            function runCleaner() {
                console.log("Starting cleanup...");

                // Theme Colors
                const bgColor = isDark ? '#121212' : '#f4f4f4'; // #000 is too harsh for OLED, #121212 is standard dark
                const cardBg = isDark ? '#1e1e1e' : '#fff';
                const textCol = isDark ? '#e0e0e0' : '#222';
                const hrCol = isDark ? '#333' : '#eee';

                // SMART SELECTOR (The Heuristic Fallback)
                function findContent() {
                    // A. Try specific known classes (TOI and others)
                    const selectors = [
                        '._s30J', '.arttextxml', '.story_content', 
                        '.article_content', 'div[data-articlebody]', 
                        'article', '.main-content'
                    ];
                    
                    for (let s of selectors) {
                        const el = document.querySelector(s);
                        if (el && el.innerText.length > 300) return el;
                    }

                    // B. Fallback: Find the <div> with the most <p> tags
                    let allDivs = document.querySelectorAll('div');
                    let maxP = 0;
                    let bestDiv = null;
                    allDivs.forEach(div => {
                        let pCount = div.querySelectorAll('p').length;
                        if(pCount > maxP) {
                            maxP = pCount;
                            bestDiv = div;
                        }
                    });
                    
                    if(maxP > 3) return bestDiv;
                    return null;
                }

                // Gather Data
                const headline = document.querySelector('h1')?.innerText || 'Article';
                
                // Better Image Selector
                const imgEl = document.querySelector('._302Yc img, .story_content img, figure img, article img');
                const imageHTML = imgEl && imgEl.src ? `<img src="${'$'}{imgEl.src}" style="max-width:100%; height:auto; border-radius:8px; margin-bottom:20px; display:block;">` : '';

                const content = findContent();
                if (!content) {
                    console.log("No content found to clean.");
                    return; 
                }

                // SURGERY: Remove junk from the content node
                const clone = content.cloneNode(true);
                clone.querySelectorAll('script, iframe, style, .vdo_embedd, .ad-container, .twitter-tweet').forEach(e => e.remove());

                // 3. NUCLEAR SWAP
                document.documentElement.innerHTML = `
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                            body { font-family: 'Georgia', serif; transition: background 0.3s; }
                            img { max-width: 100%; height: auto; }
                            p { font-size: 18px; line-height: 1.6; margin-bottom: 1.2em; }
                            a { color: ${'$'}{isDark ? '#8ab4f8' : '#1a0dab'}; text-decoration: none; }
                        </style>
                    </head>
                    <body style="margin:0; background:${'$'}{bgColor};">
                        <div style="
                            max-width: 800px;
                            margin: auto;
                            padding: 24px 20px;
                            color: ${'$'}{textCol};
                            background: ${'$'}{cardBg};
                            min-height: 100vh;
                        ">
                            <h1 style="font-family: sans-serif; font-size: 28px; line-height: 1.3; margin-bottom: 16px;">${'$'}{headline}</h1>
                            <hr style="border: 0; border-top: 1px solid ${'$'}{hrCol}; margin: 20px 0;">
                            ${'$'}{imageHTML}
                            <div id="clean-content">
                                ${'$'}{clone.innerHTML}
                            </div>
                        </div>
                    </body>
                `;
            }

            // Start the process
            waitUntilReady(runCleaner);

        })();
    """.trimIndent()
}
