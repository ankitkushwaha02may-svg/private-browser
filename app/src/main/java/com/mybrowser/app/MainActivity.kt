package com.mybrowser.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.mybrowser.app.adapter.ShortcutAdapter
import com.mybrowser.app.model.Shortcut

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var gridView: GridView
    private lateinit var homeLayout: View
    private lateinit var toolbar: Toolbar
    private lateinit var titleText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        gridView = findViewById(R.id.gridShortcuts)
        homeLayout = findViewById(R.id.homeLayout)
        toolbar = findViewById(R.id.toolbar)
        titleText = findViewById(R.id.titleText)

        setSupportActionBar(toolbar)

        setupWebView()
        setupShortcuts()
        playEntryAnimations()
    }

    private fun playEntryAnimations() {
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        titleText.startAnimation(fadeIn)

        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        gridView.startAnimation(slideUp)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_create_account -> {
                Toast.makeText(this, "Create Account - Coming soon", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.action_google_signin -> {
                Toast.makeText(this, "Sign in with Google - Coming soon", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
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