package company.tap.tapcardformkit.open.web_wrapper

import TapTheme
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.http.SslError
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.*
import cards.pay.paycardsrecognizer.sdk.Card
import com.google.gson.Gson
import company.tap.nfcreader.open.utils.TapNfcUtils
import company.tap.tapcardformkit.*
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.web_wrapper.data.CardFormWebStatus
import company.tap.tapcardformkit.open.web_wrapper.data.CardWebUrlPrefix
import company.tap.tapcardformkit.open.web_wrapper.data.cache.pref.Pref
import company.tap.tapcardformkit.open.web_wrapper.data.firstRunKeySharedPrefrence
import company.tap.tapcardformkit.open.web_wrapper.data.keyValueName
import company.tap.tapcardformkit.open.web_wrapper.data.network.model.ThreeDsResponse
import company.tap.tapcardformkit.open.web_wrapper.data.urlWebStarter
import company.tap.tapcardformkit.open.web_wrapper.presentation.nfc_activity.nfcbottomsheet.NFCBottomSheetActivity
import company.tap.tapcardformkit.open.web_wrapper.presentation.scanner_activity.ScannerActivity
import company.tap.tapcardformkit.open.web_wrapper.presentation.threeDsWebView.ThreeDsWebViewActivity
import company.tap.tapuilibrary.themekit.ThemeManager
import company.tap.tapuilibrary.uikit.atoms.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("ViewConstructor")
class TapCardKit : LinearLayout {
    lateinit var webViewFrame: FrameLayout
    private var cardPrefillPair: Pair<String, String> = Pair("", "")
    private var userIpAddress = ""
    private val retrofit = RetrofitClient.getClient()
    private val retrofit2 = RetrofitClient.getClient2()
    private val cardConfigurationApi = retrofit.create(UserApi::class.java)
    private val ipAddressConfiguration = retrofit2.create(IPaddressApi::class.java)
    private lateinit var cardUrlPrefix: String


    companion object {
        var alreadyEvaluated = false
        var NFCopened: Boolean = false
        lateinit var threeDsResponse: ThreeDsResponse
        lateinit var cardWebview: WebView
        var languageThemePair: Pair<String?, String?> = Pair("", "")

        var card: Card? = null
        fun fillCardNumber(
            cardNumber: String,
            expiryDate: String,
            cvv: String,
            cardHolderName: String
        ) {
            cardWebview.loadUrl("javascript:window.fillCardInputs({cardNumber:'$cardNumber',expiryDate:'$expiryDate',cvv:'$cvv',cardHolderName:'$cardHolderName'})")
        }

        fun setIpAddress(ipAddress: String) {
            cardWebview.loadUrl("javascript:window.setIP('$ipAddress')")
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


    init {
        LayoutInflater.from(context).inflate(R.layout.activity_card_web_wrapper, this)
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        cardWebview = findViewById(R.id.webview)
        webViewFrame = findViewById(R.id.webViewFrame)
        with(cardWebview) {
            with(settings) {
                javaScriptEnabled = true
                domStorageEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)
                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
                useWideViewPort = true
                loadWithOverviewMode = true
            }
            webViewClient = MyWebViewClient()
            setBackgroundColor(Color.TRANSPARENT)
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }

    }


    fun init(
        cardNumber: String = "",
        cardExpiry: String = ""
    ) {

        MainScope().launch {
            getCardUrlPrefixFromApi()
            getDeviceLocation()
            cardPrefillPair = Pair(cardNumber, cardExpiry)
            applyThemeForShimmer()
            val url =
                "${cardUrlPrefix}${encodeConfigurationMapToUrl(DataConfiguration.configurationsAsHashMap)}"
            Log.e("url", url)
             cardWebview.loadUrl(url)
        }


    }

    private suspend fun getDeviceLocation() {
        try {
            /**
             * request to get GeoLocation, ip address of device
             */

            val geoLocationResponse = ipAddressConfiguration.getGeoLocation()
            userIpAddress = geoLocationResponse.IPv4

        } catch (e: Exception) {
            Log.e("error", e.message.toString())
        }
    }

    private suspend fun getCardUrlPrefixFromApi() {
        try {
            val usersResponse = cardConfigurationApi.getCardConfiguration()
            if (usersResponse.android.toString()
                    .contains(BuildConfig.VERSION_CODE.toString())
            ) {
                cardUrlPrefix = usersResponse.android.`50`
            }

        } catch (e: Exception) {
            //   Log.e("error",e.message.toString())
            cardUrlPrefix = urlWebStarter
        }
    }

    private fun applyThemeForShimmer() {
        /**
         * need to be refactored : mulitple copies of same code
         */

        val tapInterface = DataConfiguration.configurationsAsHashMap?.get("interface") as? Map<*, *>
        var lanugage =
            tapInterface?.get("locale")?.toString() ?: getDeviceLocale()?.language
        if (lanugage == "dynamic") {
            lanugage = getDeviceLocale()?.language
        }
        var theme = tapInterface?.get("theme")?.toString() ?: context.getDeviceTheme()
        if (theme == "dynamic") {
            theme = context.getDeviceTheme()
        }
        languageThemePair = Pair(lanugage, theme)
        setTapThemeAndLanguage(
            this.context,
            language = lanugage,
            themeMode = theme
        )
    }


    private fun setTapThemeAndLanguage(context: Context, language: String?, themeMode: String?) {
        when (themeMode) {
            TapTheme.light.name -> {
                DataConfiguration.setTheme(
                    context, context.resources, null,
                    R.raw.defaultlighttheme, TapTheme.light.name
                )
                ThemeManager.currentThemeName = TapTheme.light.name
            }

            TapTheme.dark.name -> {
                DataConfiguration.setTheme(
                    context, context.resources, null,
                    R.raw.defaultdarktheme, TapTheme.dark.name
                )
                ThemeManager.currentThemeName = TapTheme.dark.name
            }

            else -> {}
        }
        DataConfiguration.setLocale(
            this.context,
            language ?: "en",
            null,
            this@TapCardKit.context.resources,
            R.raw.lang
        )

    }


    private fun encodeConfigurationMapToUrl(configuraton: HashMap<String, Any>?): String? {
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
                /**
                 * listen for states of cardWebStatus of onReady , onValidInput .. etc
                 */
                if (request?.url.toString().contains(CardFormWebStatus.onReady.name)) {
                    /**
                     * this scenario only for the first launch of the app , due to issue navigation
                     * of webview after shimmering , if issue appears [in first install only] init function isCalled again .
                     *
                     *
                     */
                    val isFirstTime = Pref.getValue(context, firstRunKeySharedPrefrence, "true")
                    if (isFirstTime == "true") {
                        init()
                        Pref.setValue(context, firstRunKeySharedPrefrence, "false")
                    } else {
                        DataConfiguration.getTapCardStatusListener()?.onReady()
                        /**
                         * here we send ip Address to front end
                         */
                        if (userIpAddress.isNotEmpty()) {
                            setIpAddress(userIpAddress)
                        }
                        /**
                         * here we ensure prefilling card with numbers passed from merchant
                         * commented for now
                         */

                        when (cardPrefillPair.first.isNotBlank()) {
                            true -> {
                                if (cardPrefillPair.first.length >= 7) {
                                    fillCardNumber(
                                        cardNumber = cardPrefillPair.first,
                                        expiryDate = cardPrefillPair.second,
                                        "",
                                        ""
                                    )
                                }
                            }

                            false -> {}
                        }

                    }

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
                    params?.height = webViewFrame.context.getDimensionsInDp(newHeight?.toInt() ?: 95)
                    webViewFrame.layoutParams = params

                    DataConfiguration.getTapCardStatusListener()
                        ?.onHeightChange(newHeight.toString())


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
                    val queryParams =
                        request?.url?.getQueryParameterFromUri(keyValueName).toString()
                    threeDsResponse = queryParams.getModelFromJson()
                    navigateTo3dsActivity()


                }
                if (request?.url.toString().contains(CardFormWebStatus.onScannerClick.name)) {
                    /**
                     * navigate to Scanner Activity
                     */
                    val intent = Intent(context, ScannerActivity::class.java)
                    (context).startActivity(intent)

                }
                if (request?.url.toString().contains(CardFormWebStatus.onNfcClick.name)) {
                    /**
                     * navigate to NFC Activity
                     */
                    if (TapNfcUtils.isNfcAvailable(context)) {
                        NFCopened = true
                        /**
                         * old NFCLauncher
                         */
//                        val intent = Intent(context,NFCLaunchActivity::class.java)
                        /**
                         * new NFC act as bottomSheet
                         */
                        val intent = Intent(context, NFCBottomSheetActivity()::class.java)
                        (context).startActivity(intent)
                    } else {
                        DataConfiguration.getTapCardStatusListener()
                            ?.onError("NFC is not supported on this device")
                    }


                }
                return true

            } else {
                return false
            }
        }

        @SuppressLint("WebViewClientOnReceivedSslError")
        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            view?.handleSSlError(error,handler)
        }

    }

    fun navigateTo3dsActivity() {
        /**
         * navigate to 3ds Activity
         */
        val intent = Intent(context, ThreeDsWebViewActivity::class.java)
        (context).startActivity(intent)
        ThreeDsWebViewActivity.tapCardKit = this@TapCardKit

    }

    fun generateTapToken() {
        cardWebview.loadUrl("javascript:window.generateTapToken()")
    }
    override fun onDetachedFromWindow() {
        cardWebview.destroy()
        super.onDetachedFromWindow()
    }
}

fun  WebView.handleSSlError(error: SslError?, handler: SslErrorHandler?){
    val builder = AlertDialog.Builder(this.context)
    var message: String = when (error?.primaryError) {
        SslError.SSL_EXPIRED -> resources.getString(R.string.ssl_error_certificate)
        SslError.SSL_IDMISMATCH -> resources.getString(R.string.ssl_error_host_name)
        SslError.SSL_NOTYETVALID ->  resources.getString(R.string.ssl_error_certifc_error)
        SslError.SSL_UNTRUSTED ->  resources.getString(R.string.ssl_error_certifc_not_trusted)
        SslError.SSL_DATE_INVALID ->  resources.getString(R.string.ssl_error_certifc_not_trusted)
        SslError.SSL_INVALID ->  resources.getString(R.string.ssl_error_certifc_not_trusted)

        else ->  resources.getString(R.string.ssl_error_certifc_uknwon_ssl_error)
    }
    message += resources.getString(R.string.continueSubtitle)
    builder.setTitle(resources.getString(R.string.ssl_error))
    builder.setMessage(message)
    builder.setPositiveButton(
        resources.getString(R.string.continueTitle)
    ) { dialog, which -> handler?.proceed() }
    builder.setNegativeButton(
        resources.getString(R.string.canceltitle)
    ) { dialog, which ->
        handler?.cancel()
    }
        if (error?.primaryError != SslError.SSL_IDMISMATCH) {
        val dialog = builder.create()
        dialog.show()
    } else {
        handler?.proceed()
    }
}




