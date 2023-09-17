package company.tap.tapcardformkit.open.web_wrapper

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.open.web_wrapper.model.ThreeDsResponse

const val chunkSize = 2048
const val keyValueForAuthPayer = "auth_payer"

class ThreeDsWebViewActivity : AppCompatActivity() {

    lateinit var webView: WebView

    var threeDsResponse: ThreeDsResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_ds_web_view)
        webView = findViewById(R.id.webview3ds)
        initSupportActionBar()
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = MyWebViewClient()


        val intent = intent.extras
        threeDsResponse = intent?.getParcelable<ThreeDsResponse>(parcelableThreeDsKey)
        webView.loadUrl(threeDsResponse?.threeDsUrl ?: "google.com")


    }

    private fun initSupportActionBar() {
//        actionBar?.title = Html.fromHtml("<font color='#ff0000'>3ds Authentication </font>");


    }

    inner class MyWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun shouldOverrideUrlLoading(
            webView: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val queryParams = request?.url?.getQueryParameter(keyValueForAuthPayer).toString()
            val intent = Intent()
            intent.putExtra(authDataPayerKey, queryParams)
            setResult(Activity.RESULT_OK, intent)
            finish()

            return true

        }

        override fun onPageFinished(view: WebView, url: String) {

        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.nothing, R.anim.bottom_down);

    }
}