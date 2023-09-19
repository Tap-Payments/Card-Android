package company.tap.tapcardformkit.open.web_wrapper

import TapCardConfigurations
import TapCardEdges
import TapLocal
import TapTheme
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.*
import cards.pay.paycardsrecognizer.sdk.Card
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import company.tap.tapcardformkit.*
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.web_wrapper.enums.CardFormWebStatus
import company.tap.tapcardformkit.open.web_wrapper.model.ThreeDsResponse
import company.tap.tapcardformkit.open.web_wrapper.scanner_activity.ScannerActivity
import company.tap.tapuilibrary.themekit.ThemeManager
import company.tap.tapuilibrary.uikit.atoms.*
import java.net.URLEncoder
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("ViewConstructor")
class TapCardKit : LinearLayout {
    lateinit var hideableWebView: WebView
    lateinit var threeDsBottomsheet: BottomSheetDialog
    lateinit var lottieAnimationView: LottieAnimationView
    private var isRedirected = false
    lateinit var constraintLayout: ConstraintLayout
    lateinit var webViewFrame: FrameLayout
    lateinit var webFrame3ds: FrameLayout
    private var alreadyEvaluated = false

    companion object{
        lateinit var threeDsResponse: ThreeDsResponse
        lateinit var cardWebview: WebView
        lateinit var cardConfiguraton: CardConfiguraton

        var card:Card?=null
        fun fillCardNumber(cardNumber:String,expiryDate:String,cvv:String,cardHolderName:String){
            cardWebview.loadUrl("javascript:window.fillCardInputs({cardNumber:'$cardNumber',expiryDate:'$expiryDate',cvv:'$cvv',cardHolderName:'$cardHolderName'})")
        }

        fun generateTapAuthenticate(authIdPayer: String) {
            cardWebview.loadUrl("javascript:window.loadAuthentication('$authIdPayer')")
        }



    }

    /**
     * Simple constructor to use when creating a TapPayCardSwitch from code.
     *  @param context The Context the view is running in, through which it can
     *  access the current theme, resources, etc.
     **/
    constructor(context: Context) : super(context)

    /**
     *  @param context The Context the view is running in, through which it can
     *  access the current theme, resources, etc.
     *  @param attrs The attributes of the XML Button tag being used to inflate the view.
     *
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
//    val resultLauncher = (context as AppCompatActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                // There are no request codes
//                val data: Intent? = result?.data
//                val card = data?.getParcelableExtra<Card>(ScanCardIntent.RESULT_PAYCARDS_CARD)
//
//
//                fillCardNumber(cardNumber = card?.cardNumber.toString(), cardHolderName =card?.cardHolderName ?: "" , cvv ="" , expiryDate =card?.expirationDate ?: "" )
//
//
//
//            }
//        }


    init {
     LayoutInflater.from(context).inflate(R.layout.activity_card_web_wrapper, this)
        initWebView()

    }

     fun initWebView() {
        cardWebview = findViewById(R.id.webview)
        hideableWebView = findViewById(R.id.hideableWebView)
        webViewFrame = findViewById(R.id.webViewFrame)
        webFrame3ds = findViewById(R.id.webViewFrame3ds)
        constraintLayout = findViewById(R.id.constraint)
        lottieAnimationView = findViewById(R.id.shimmer_view)
        cardWebview.settings.javaScriptEnabled = true
        hideableWebView.settings.javaScriptEnabled = true
        cardWebview.settings.cacheMode = WebSettings.LOAD_NO_CACHE;
        cardWebview.settings.domStorageEnabled = true;
        cardWebview.setBackgroundColor(Color.TRANSPARENT);
        cardWebview.setLayerType(LAYER_TYPE_SOFTWARE, null)
        cardWebview.webViewClient = MyWebViewClient()
        hideableWebView.webViewClient = HideableWebViewClient()

    }


    fun init(configuraton: CardConfiguraton) {
        cardConfiguraton = configuraton
        applyThemeForShimmer()
        startShimmer()
        when (configuraton) {
            CardConfiguraton.ModelConfiguration -> {
                with(DataConfiguration.configurations) {
                    Log.e(
                        "urlToLoad", "${urlWebStarter}${
                            encodeConfigurationToUrl(
                                DataConfiguration.configurations
                            )
                        }"
                    )
                    var url  = "${urlWebStarter}${
                        encodeConfigurationToUrl(
                            DataConfiguration.configurations
                        )
                    }"

                    cardWebview.loadUrl(
                        "${urlWebStarter}${
                            encodeConfigurationToUrl(
                                DataConfiguration.configurations
                            )
                        }"
                    )
                }
            }
            CardConfiguraton.MapConfigruation -> {
                Log.e(
                    "urlToLoad", "${urlWebStarter}${encodeConfigurationMapToUrl(DataConfiguration.configurationsAsHashMap)}"
                )
                var url  = "${urlWebStarter}${encodeConfigurationMapToUrl(DataConfiguration.configurationsAsHashMap)}"
                cardWebview.loadUrl("${urlWebStarter}${encodeConfigurationMapToUrl(DataConfiguration.configurationsAsHashMap)}")
            }
        }
    }

    private fun applyThemeForShimmer() {
        when(cardConfiguraton){
            CardConfiguraton.ModelConfiguration->{
                with(DataConfiguration.configurations?.tapCardConfigurationInterface) {
                    when (this?.theme) {
                        TapTheme.light -> {
                            lottieAnimationView.setAnimation(
                                context.getAssetFile("lottie_light")
                            )

                        }
                        TapTheme.dark -> {
                            lottieAnimationView.setAnimation(
                                context.getAssetFile("lottie_dark")
                            )
                        }
                        else -> {}
                    }

                    when (this?.edges) {
                        TapCardEdges.flat -> {}
                        TapCardEdges.curved -> {
                            val radius = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                10f,
                                context.resources.displayMetrics
                            )
                            findViewById<CardView>(R.id.card_lottie).radius = radius
                        }
                        else -> {}
                    }

                  setTapThemeAndLanguage(
                        context,
                        TapLocal.valueOf(this?.locale?.name.toString()),
                        TapTheme.valueOf(this?.theme?.name.toString())
                    )
                }
            }
            CardConfiguraton.MapConfigruation ->{
                val tapInterface = DataConfiguration.configurationsAsHashMap?.get("interface") as Map<*, *>
                with(tapInterface["theme"].toString()) {
                    when (this) {
                        TapTheme.light.name -> {
                            lottieAnimationView.setAnimation(
                                context.getAssetFile("lottie_light")
                            )
                        }
                        TapTheme.dark.name -> {
                            lottieAnimationView.setAnimation(
                                context.getAssetFile("lottie_dark")
                            )
                        }
                        else -> {}
                    }


                }
                with(tapInterface["edges"].toString()) {
                    when (this) {
                        TapCardEdges.flat.name -> {}
                        TapCardEdges.curved.name -> {
                            val radius = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                10f,
                                context.resources.displayMetrics
                            )
                            findViewById<CardView>(R.id.card_lottie).radius = radius
                        }
                        else -> {}
                    }
                }

              setTapThemeAndLanguage(
                    context,
                    TapLocal.valueOf(tapInterface["locale"].toString()),
                    TapTheme.valueOf(tapInterface["theme"].toString())
                )
            }
        }


    }


    fun startShimmer() {
        lottieAnimationView.visibility = View.VISIBLE;
        cardWebview.visibility = View.GONE
        findViewById<CardView>(R.id.card_lottie).visibility = View.VISIBLE


    }

    fun setTapThemeAndLanguage(context: Context, language: TapLocal, themeMode: TapTheme) {
        when (themeMode) {
            TapTheme.light -> {
                DataConfiguration.setTheme(
                    context, context.resources, null,
                    R.raw.defaultlighttheme, TapTheme.light.name
                )
                ThemeManager.currentThemeName = TapTheme.light.name
            }
            TapTheme.dark -> {
                DataConfiguration.setTheme(
                    context, context.resources, null,
                    R.raw.defaultdarktheme, TapTheme.dark.name
                )
                ThemeManager.currentThemeName = TapTheme.dark.name
            }
            else -> {}
        }

        DataConfiguration.setLocale(context, language.name, null, context.resources, R.raw.lang)
    }

    fun stopShimmer() {
        lottieAnimationView.visibility = View.GONE;
        cardWebview.visibility = View.VISIBLE
        findViewById<CardView>(R.id.card_lottie).visibility = View.GONE

    }



    fun encodeConfigurationToUrl(configuraton: TapCardConfigurations?): String? {
        val gson = Gson()
        val json: String = gson.toJson(configuraton)

        val encodedUrl = URLEncoder.encode(json, "UTF-8")
        return encodedUrl

    }
    fun encodeConfigurationMapToUrl(configuraton: HashMap<String,Any>?): String? {
        val gson = Gson()
        val json: String = gson.toJson(configuraton)

        val encodedUrl = URLEncoder.encode(json, "UTF-8")
        return encodedUrl

    }


    inner class MyWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun shouldOverrideUrlLoading(
            webView: WebView?,
            request: WebResourceRequest?
        ): Boolean {

            /**
             * main checker if url start with "tapCardWebSDK://"
             */
            if (request?.url.toString().startsWith(CardWebUrlPrefix, ignoreCase = true)) {

                if (::threeDsBottomsheet.isInitialized)
                    threeDsBottomsheet.dismiss()
                /**
                 * listen for states of cardWebStatus of onReady , onValidInput .. etc
                 */
                if (request?.url.toString().contains(CardFormWebStatus.onReady.name)) {
                    DataConfiguration.getTapCardStatusListener()?.onReady()
                    stopShimmer()
                }
                if (request?.url.toString().contains(CardFormWebStatus.onValidInput.name)) {
                    val validInputValue =
                        request?.url?.getQueryParameterFromUri(keyValueName).toString()
                    when (validInputValue.toBoolean()) {
                        true -> {
                            DataConfiguration.getTapCardStatusListener()?.onValidInput(
                                request?.url?.getQueryParameterFromUri(keyValueName).toString()
                            )

                        }
                        false -> {}
                    }

                }
                if (request?.url.toString().contains(CardFormWebStatus.onError.name)) {
                    DataConfiguration.getTapCardStatusListener()
                        ?.onError(request?.url?.getQueryParameterFromUri(keyValueName).toString())
                }
                if (request?.url.toString().contains(CardFormWebStatus.onFocus.name)) {
                    DataConfiguration.getTapCardStatusListener()?.onFocus()

                }
                if (request?.url.toString().contains(CardFormWebStatus.onSuccess.name)) {
                    DataConfiguration.getTapCardStatusListener()
                        ?.onSuccess(request?.url?.getQueryParameterFromUri(keyValueName).toString())
                }
                if (request?.url.toString().contains(CardFormWebStatus.onHeightChange.name)) {
                    val newHeight = request?.url?.getQueryParameter(keyValueName)
                    val params: ViewGroup.LayoutParams? = webViewFrame.layoutParams
                    params?.height =
                        webViewFrame.context.getDimensionsInDp(newHeight?.toInt() ?: 95)
                    webViewFrame.layoutParams = params


                }
                if (request?.url.toString().contains(CardFormWebStatus.onBinIdentification.name)) {
                    DataConfiguration.getTapCardStatusListener()
                        ?.onBindIdentification(
                            request?.url?.getQueryParameterFromUri(keyValueName).toString()
                        )
                }

                if (request?.url.toString().contains(CardFormWebStatus.on3dsRedirect.name)) {
                    /**
                     * navigate to 3ds Activity
                     */
                    val queryParams = request?.url?.getQueryParameterFromUri(keyValueName).toString()
                    threeDsResponse = queryParams.getModelFromJson()
                    Log.e("data", threeDsResponse.toString())
                    hideableWebView.loadUrl(threeDsResponse.threeDsUrl)


                }
                if (request?.url.toString().contains(CardFormWebStatus.onScannerClick.name)) {
                    /**
                     * navigate to Scanner Activity
                     */
                    val intent = Intent(context,ScannerActivity::class.java)
                    (context).startActivity(intent)
//                    resultLauncher.launch(Intent(context, ScannerActivity::class.java))



                }
                if (request?.url.toString().contains(CardFormWebStatus.onNfcClick.name)) {
                    /**
                     * navigate to NFC Activity
                     */
//                    resultLauncher.launch(Intent(context, ScannerActivity::class.java))



                }
                return true

            } else return false
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


    inner class HideableWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun shouldOverrideUrlLoading(
            webView: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            Log.e("urlToLoad", webView?.url.toString())

            isRedirected = true;
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            Log.e("onfinishedLoading", url.toString())
            Log.e("webviewProgress", cardWebview.progress.toString())
            Log.e("onfinishedRedirection", isRedirected.toString())


            if (!alreadyEvaluated) {
                alreadyEvaluated = true;
                Handler().postDelayed({
                    navigateTo3dsActivity()
                }, waitDelaytime)
            } else {
                alreadyEvaluated = false;
            }


        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            isRedirected = false;

        }

        fun navigateTo3dsActivity() {
            // on below line we are creating a new bottom sheet dialog.
            /**
             * put buttomsheet in separate class
             */

            threeDsBottomsheet = ThreeDsBottomSheet(context,R.style.CustomBottomSheetDialog,this@TapCardKit)
            threeDsBottomsheet.show()
//            threeDsBottomsheet.behavior.isFitToContents = false
//            threeDsBottomsheet.behavior.maxHeight = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._450sdp)
//            threeDsBottomsheet.behavior.peekHeight = (getScreenHeight() * 2 / 3) + 100
//            threeDsBottomsheet.behavior.isDraggable = false
//



            // on below line we are inflating a layout file which we have created.
//            val view = (context as Activity).layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
//            val tapBrandView = view.findViewById<TapBrandView>(R.id.tab_brand_view)
//            val cardview = view.findViewById<MaterialCardView>(R.id.card_view)
//
//
//            val webView = view.findViewById<WebView>(R.id.webview3ds)
//            webView.layoutParams = FrameLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                context.twoThirdHeightView().roundToInt()
//            )
//            webView.settings.javaScriptEnabled = true
//            webView.webViewClient = threeDsWebViewClient()
//            webView.settings.loadWithOverviewMode = true
//            webView.settings.useWideViewPort = true
//            webView.settings.builtInZoomControls = true;

//            webView.loadUrl(threeDsResponse.threeDsUrl)
//
//            threeDsBottomsheet.setCancelable(false)
//            threeDsBottomsheet.setContentView(view)
//            threeDsBottomsheet.show()
//
//
//            tapBrandView.backButtonLinearLayout.setOnClickListener {
//                threeDsBottomsheet.dismiss()
//                init(cardConfiguraton)
//                DataConfiguration.getTapCardStatusListener()?.onError(resources.getString(R.string.user_cancell))
//
//            }


        }

        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
        }
    }

    fun generateTapToken() {
        cardWebview.loadUrl("javascript:window.generateTapToken()")
    }

//    fun generateTapAuthenticate(authIdPayer: String) {
//        cardWebview.loadUrl("javascript:window.loadAuthentication('$authIdPayer')")
//    }



//    inner class threeDsWebViewClient : WebViewClient() {
//        @RequiresApi(Build.VERSION_CODES.O)
//        override fun shouldOverrideUrlLoading(
//            webView: WebView?,
//            request: WebResourceRequest?
//        ): Boolean {
//            Log.e("url3ds", request?.url.toString())
//            when (request?.url?.toString()?.contains(threeDsResponse.keyword)) {
//                true -> {
//                    if (::threeDsBottomsheet.isInitialized)
//                        threeDsBottomsheet.dismiss()
//                    generateTapAuthenticate(request?.url?.toString().toString())
//                }
//                false -> {}
//                else -> {}
//            }
//            return true
//
//        }
//
//        override fun onPageFinished(view: WebView, url: String) {
//
//        }
//
//        override fun onReceivedError(
//            view: WebView,
//            request: WebResourceRequest,
//            error: WebResourceError
//        ) {
//            super.onReceivedError(view, request, error)
//        }
//    }
}



enum class CardConfiguraton() {
    MapConfigruation, ModelConfiguration
}




