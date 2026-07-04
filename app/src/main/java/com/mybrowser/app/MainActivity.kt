package com.mybrowser.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import com.mybrowser.app.adapter.ShortcutAdapter
import com.mybrowser.app.model.Shortcut

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var gridView: GridView
    private lateinit var homeLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        gridView = findViewById(R.id.gridShortcuts)
        homeLayout = findViewById(R.id.homeLayout)

        setupWebView()
        setupShortcuts()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.cacheMode = android.webkit.WebSettings.LOAD_NO_CACHE

        webView.webViewClient = WebViewClient()

        webView.clearHistory()
        webView.clearCache(true)
        webView.clearFormData()

        android.webkit.CookieManager.getInstance().setAcceptCookie(false)
        android.webkit.CookieManager.getInstance().removeAllCookies(null)
    }

    private fun setupShortcuts() {
        val shortcuts = listOf(
            Shortcut("Google", "https://www.google.com", R.drawable.ic_google),
            Shortcut("YouTube", "https://www.youtube.com", R.drawable.ic_youtube),
            Shortcut("Facebook", "https://www.facebook.com", R.drawable.ic_facebook),
            Shortcut("Instagram", "https://www.instagram.com", R.drawable.ic_instagram),
            Shortcut("WhatsApp Web", "https://web.whatsapp.com", R.drawable.ic_whatsapp),
            Shortcut("JioSaavn (Lofi/Remix)", "https://www.jiosaavn.com", R.drawable.ic_music)
        )

        gridView.adapter = ShortcutAdapter(this, shortcuts)

        gridView.setOnItemClickListener { _, _, position, _ ->
            val chosen = shortcuts[position]
            openSite(chosen.url)
        }
    }

    private fun openSite(url: String) {
        homeLayout.visibility = View.GONE
        webView.visibility = View.VISIBLE
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.visibility == View.VISIBLE) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                goHome()
            }
        } else {
            super.onBackPressed()
        }
    }

    private fun goHome() {
        webView.stopLoading()
        webView.loadUrl("about:blank")
        webView.visibility = View.GONE
        homeLayout.visibility = View.VISIBLE
        clearEverything()
    }

    private fun clearEverything() {
        webView.clearHistory()
        webView.clearCache(true)
        webView.clearFormData()
        android.webkit.CookieManager.getInstance().removeAllCookies(null)
        android.webkit.WebStorage.getInstance().deleteAllData()
    }

    override fun onDestroy() {
        clearEverything()
        super.onDestroy()
    }

    override fun onPause() {
        clearEverything()
        super.onPause()
    }
}