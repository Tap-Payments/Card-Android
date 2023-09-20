package company.tap.tapcardformkit.open.web_wrapper

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.getScreenHeight
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit.Companion.cardConfiguraton
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit.Companion.generateTapAuthenticate
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit.Companion.threeDsResponse
import company.tap.tapcardformkit.twoThirdHeightView
import company.tap.tapuilibrary.uikit.views.TapBrandView
import kotlin.math.roundToInt

class ThreeDsBottomSheet(context: Context, style: Int, var tapCardKit: TapCardKit) : BottomSheetDialog(context,style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog, null)
        this.setContentView(view)
        this.setCancelable(false)

        with(this){
            behavior.isFitToContents = false
            behavior.maxHeight = context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._450sdp)
            behavior.peekHeight = (getScreenHeight() * 2 / 3) + 100
            behavior.isDraggable = false
        }

        val tapBrandView = view.findViewById<TapBrandView>(R.id.tab_brand_view)


        val webView = view.findViewById<WebView>(R.id.webview3ds)

        webView.layoutParams = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            context.twoThirdHeightView().roundToInt()
        )
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = threeDsWebViewClient()
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true;
        webView.loadUrl(threeDsResponse.threeDsUrl)

        tapBrandView.backButtonLinearLayout.setOnClickListener {
            this.dismiss()
            tapCardKit.init(cardConfiguraton)
            DataConfiguration.getTapCardStatusListener()?.onError("User canceled 3ds")

        }



    }

    inner class threeDsWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun shouldOverrideUrlLoading(
            webView: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            Log.e("url3ds", request?.url.toString())
            when (request?.url?.toString()?.contains(threeDsResponse.keyword)) {
                true -> {
                    this@ThreeDsBottomSheet.dismiss()
                    generateTapAuthenticate(request.url?.toString().toString())
                }
                false -> {}
                else -> {}
            }
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


    fun getTheme(): Int = R.style.CustomBottomSheetDialog




}