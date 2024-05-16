package company.tap.tapcardformkit.open.web_wrapper.presentation.threeDsWebView

import android.annotation.SuppressLint
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.doAfterSpecificTime
import company.tap.tapcardformkit.getDeviceSpecs
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit
import company.tap.tapcardformkit.open.web_wrapper.handleSSlError
import company.tap.taplocalizationkit.LocalizationManager
import java.util.*


const val delayTime = 2000L

class ThreeDsWebViewActivity : AppCompatActivity() {
    lateinit var threeDsBottomsheet: BottomSheetDialogFragment
    var loadedBottomSheet = false

    companion object {
        lateinit var tapCardKit: TapCardKit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_ds_web_view)
        LocalizationManager.setLocale(this, Locale(DataConfiguration.lanuage.toString()))

        val webView = WebView(this)
        webView.layoutParams = this.getDeviceSpecs().first.let {
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                it
            )
        }
        with(webView) {
            settings.javaScriptEnabled = true
            isVerticalScrollBarEnabled = true
            requestFocus()
            webView.loadUrl(TapCardKit.threeDsResponse.threeDsUrl)
            webViewClient = threeDsWebViewClient()
        }

        threeDsBottomsheet = ThreeDsBottomSheetFragment(webView, onCancel = {
            tapCardKit.init()
            DataConfiguration.getTapCardStatusListener()?.onError("User canceled 3ds")

        })


        TapCardKit.alreadyEvaluated = false


    }

    inner class threeDsWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun shouldOverrideUrlLoading(
            webView: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            Log.e("url3ds", request?.url.toString())
            when (request?.url?.toString()?.contains(TapCardKit.threeDsResponse.keyword)) {
                true -> {
                    threeDsBottomsheet.dialog?.dismiss()
                    TapCardKit.generateTapAuthenticate(request.url?.toString().toString())
                }

                false -> {}
                else -> {}
            }
            return true

        }

        override fun onPageFinished(view: WebView, url: String) {
            if (loadedBottomSheet) {
                return
            } else {
                doAfterSpecificTime(time = delayTime) {
                    threeDsBottomsheet.show(supportFragmentManager, "")
                }
            }
            loadedBottomSheet = true
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError) {
            view?.handleSSlError(error, handler)
        }

    }


}
