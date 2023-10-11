package company.tap.tapcardformkit.open.web_wrapper

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.doAfterSpecificTime
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.twoThirdHeightView
import company.tap.tapuilibrary.uikit.views.TapBrandView
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.roundToInt

class ThreeDsBottomSheetFragment : BottomSheetDialogFragment() {

    companion object{
        lateinit var tapCardKit: TapCardKit
    }


    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog, null)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tapBrandView = view.findViewById<TapBrandView>(R.id.tab_brand_view)

        try {
       //     val interfacee =  DataConfiguration.configurationsAsHashMap?.get("interface") as HashMap<*, *>
      //      val powerd  = interfacee.get("powered") as Boolean
            val powerd  = TapCardKit.threeDsResponse.powered


            when(powerd){
                false ->tapBrandView.poweredByImage.visibility = View.INVISIBLE
                else -> {}
            }
        }catch (e:java.lang.Exception){
            Log.e("excption",e.toString())
        }


        val webView = view.findViewById<WebView>(R.id.webview3ds)

        webView.layoutParams = context?.twoThirdHeightView()?.roundToInt()?.let {
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                it
            )
        }
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = threeDsWebViewClient()
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = true;
        webView.loadUrl(TapCardKit.threeDsResponse.threeDsUrl)

        tapBrandView.backButtonLinearLayout.setOnClickListener {
            this.dismiss()
            requireActivity().finish()
            tapCardKit.init(TapCardKit.cardConfiguraton)
            DataConfiguration.getTapCardStatusListener()?.onError("User canceled 3ds")

        }
        this.dialog?.setOnDismissListener {
            doAfterSpecificTime {
                requireActivity().finish()
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimations

        setStyle(STYLE_NORMAL,R.style.CustomBottomSheetDialogFragment)

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
                    this@ThreeDsBottomSheetFragment.dialog?.dismiss()
               //     requireActivity().finish()
                    TapCardKit.generateTapAuthenticate(request.url?.toString().toString())
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


    override fun getTheme(): Int = R.style.CustomBottomSheetDialogFragment




}