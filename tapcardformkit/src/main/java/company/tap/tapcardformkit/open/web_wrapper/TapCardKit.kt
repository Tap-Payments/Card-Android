package company.tap.tapcardformkit.open.web_wrapper

//import com.airbnb.lottie.LottieAnimationView

import TapCardConfigurations
import TapLocal
import TapTheme
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.*
import cards.pay.paycardsrecognizer.sdk.Card
import com.google.gson.Gson
import company.tap.nfcreader.open.utils.TapNfcUtils
import company.tap.tapcardformkit.*
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.web_wrapper.enums.CardFormWebStatus
import company.tap.tapcardformkit.open.web_wrapper.model.ThreeDsResponse
import company.tap.tapcardformkit.open.web_wrapper.nfc_activity.NFCLaunchActivity
import company.tap.tapcardformkit.open.web_wrapper.scanner_activity.ScannerActivity
import company.tap.tapuilibrary.themekit.ThemeManager
import company.tap.tapuilibrary.uikit.atoms.*
import java.net.URLEncoder
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("ViewConstructor")
class TapCardKit : LinearLayout {
    lateinit var hideableWebView: WebView
//    lateinit var threeDsBottomsheet: BottomSheetDialogFragment
//    lateinit var lottieAnimationView: LottieAnimationView
    private var isRedirected = false
    lateinit var constraintLayout: LinearLayout
    lateinit var webViewFrame: FrameLayout
    lateinit var webFrame3ds: FrameLayout

    companion object{
         var alreadyEvaluated = false

        lateinit var threeDsResponse: ThreeDsResponse
        lateinit var cardConfiguraton: CardConfiguraton
       // lateinit var cardWebview: WebView

        var card:Card?=null
        fun fillCardNumber(cardNumber:String,expiryDate:String,cvv:String,cardHolderName:String){
        //    cardWebview.loadUrl("javascript:window.fillCardInputs({cardNumber:'$cardNumber',expiryDate:'$expiryDate',cvv:'$cvv',cardHolderName:'$cardHolderName'})")
        }

        fun generateTapAuthenticate(authIdPayer: String) {
           // cardWebview.loadUrl("javascript:window.loadAuthentication('$authIdPayer')")
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

    private var mWebView: WebView? = null


    private fun initWebView() {
      //  cardWebview = findViewById(R.id.webview)
      //  hideableWebView = findViewById(R.id.hideableWebView)
//        webViewFrame = findViewById(R.id.webViewFrame)
   //     webFrame3ds = findViewById(R.id.webViewFrame3ds)
        constraintLayout = findViewById(R.id.constraint)
//         cardWebview.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK


      //   cardWebview.settings.javaScriptEnabled = true
       //  cardWebview.settings.useWideViewPort = true
        // hide ableWebView.settings.javaScriptEnabled = true
    //    cardWebview.settings.domStorageEnabled = true
//        cardWebview.setBackgroundColor(Color.TRANSPARENT)
//        cardWebview.setLayerType(LAYER_TYPE_SOFTWARE, null)

//         cardWebview.webChromeClient = WebChromeClient()
    //     cardWebview.webViewClient = MyWebViewClient()
      //   hideableWebView.webViewClient = HideableWebViewClient()
       //  cardWebview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
//         val file = File(this.getExternalFilesDir(null), "fileName.html")
//         FileUtils.copyURLToFile(URL("http://www.bmimobile.co.uk/why-bmi.php"), file)

//         cardWebview.settings.domStorageEnabled= true
//         cardWebview.settings.javaScriptEnabled= true
//         cardWebview.loadUrl("https://www.google.com/")


     }
    fun CustomRelativeLayout(context: Context, url: String) {
        mWebView = WebView(context)
        mWebView?.setId(View.NO_ID)
        mWebView?.setScrollContainer(false)
        mWebView?.loadUrl(url)
        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
            context.resources.displayMetrics.widthPixels,
            context.resources.displayMetrics.heightPixels
        )
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        mWebView?.layoutParams = params
        addView(mWebView)
    }

    fun getTheWebView(): WebView? {
        return mWebView
    }

     fun init(configuraton: CardConfiguraton) {
        cardConfiguraton = configuraton
        applyThemeForShimmer()
      //  startShimmer()
        when (configuraton) {
            CardConfiguraton.MapConfigruation -> {
                val wv = WebView(context)

             //   wv.loadUrl("https://www.google.com")

                var url  = "${urlWebStarter}${encodeConfigurationMapToUrl(DataConfiguration.configurationsAsHashMap)}"
//                cardWebview.loadData("test",null,null)


//                cardWebview.loadUrl("http://www.google.com")
         //       doAfterSpecificTime(time = 5000) {
             //      cardWebview.loadUrl("${urlWebStarter}${encodeConfigurationMapToUrl(DataConfiguration.configurationsAsHashMap)}")
             //   }
//                cardWebview.clearCache(false)

                val params = LayoutParams(1500,1500);
                wv.layoutParams = params;


             //   wv.loadUrl(url)

                wv.settings.javaScriptEnabled= true
                wv.settings.cacheMode = WebSettings.LOAD_DEFAULT

                  wv.webViewClient = MyWebViewClient()
              //  wv.loadUrl(url)
                wv.loadUrl("https://sdk.dev.tap.company/v2/card/wrapper?configurations=%7B%22operator%22%3A%7B%22publicKey%22%3A%22pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7%22%7D%2C%22scope%22%3A%22Token%22%2C%22order%22%3A%7B%22reference%22%3A%22tck_LVDdp2zw5iGyPByCiNuCht0Dj%22%2C%22amount%22%3A%22%22%2C%22description%22%3A%22%22%2C%22currency%22%3A%22KWD%22%2C%22id%22%3A%22%22%7D%2C%22customer%22%3A%7B%22nameOnCard%22%3A%22test%22%2C%22editable%22%3Atrue%2C%22contact%22%3A%7B%22phone%22%3A%7B%22number%22%3A%22011%22%2C%22countryCode%22%3A%22%2B20%22%7D%2C%22email%22%3A%22test%40gmail.com%22%7D%2C%22name%22%3A%5B%7B%22middle%22%3A%22middle%22%2C%22last%22%3A%22last%22%2C%22lang%22%3A%22en%22%2C%22first%22%3A%22first%22%7D%5D%7D%2C%22purpose%22%3A%22Transaction%22%2C%22transaction%22%3A%7B%22metadata%22%3A%7B%22id%22%3A%22%22%7D%2C%22paymentAgreement%22%3A%7B%22contract%22%3A%7B%22id%22%3A%22%22%7D%2C%22id%22%3A%22%22%7D%7D%2C%22invoice%22%3A%7B%22id%22%3A%22%22%7D%2C%22merchant%22%3A%7B%22id%22%3A%22%22%7D%2C%22features%22%3A%7B%22customerCards%22%3A%7B%22saveCard%22%3Atrue%2C%22autoSaveCard%22%3Atrue%7D%2C%22alternativeCardInputs%22%3A%7B%22cardScanner%22%3Atrue%2C%22cardNFC%22%3Atrue%7D%2C%22acceptanceBadge%22%3Atrue%7D%2C%22acceptance%22%3A%7B%22supportedSchemes%22%3A%5B%5D%2C%22supportedFundSource%22%3A%5B%22CREDIT%22%2C%22DEBIT%22%5D%2C%22supportedPaymentAuthentications%22%3A%5B%223DS%22%5D%7D%2C%22fieldsVisibility%22%3A%7B%22card%22%3A%7B%22cvv%22%3Atrue%2C%22cardHolder%22%3Atrue%7D%7D%2C%22interface%22%3A%7B%22powered%22%3Atrue%2C%22loader%22%3Atrue%2C%22edges%22%3A%22flat%22%2C%22colorStyle%22%3A%22colored%22%2C%22theme%22%3A%22light%22%2C%22cardDirection%22%3A%22dynamic%22%2C%22locale%22%3A%22en%22%7D%2C%22redirect%22%3A%7B%22url%22%3A%22%22%7D%2C%22post%22%3A%7B%22url%22%3A%22%22%7D%2C%22headers%22%3A%7B%22application%22%3A%22cu%5Cu003dVBBaeoPAEullVOi4F1WGQh%2FyBGeb75Ztnj59IjaykssoR9bNCqUUTmKSbADtf7CPH4Gh%2BkpHAlFFite4zuFC3x1NuD4xaQmiwbouvAMWav9Qxp100azlxigJA3AXyAwPJI4eKGGBLQqoLhS9UszL%2BIAR8a0gO11g1zTX1eCuoHA%5Cu003d%7Caid%5Cu003duqoFeECwOgjPG6dDKojFvFKuolP1oNuLo5VCSr6EFzIbpduF7dVE6%2Fcq5SIM7GFG%2BSvVkK8x73jOJWhtZvJ08fSXzW4eCQ5Rge1qbm8WThJgnJJHcsGtWfbDLpiUKaPbubQfT00GrXpeYX7JEPHvU%2F3%2FF%2BX43GEFfAuUo31ed18%5Cu003d%7Cat%5Cu003dQYIgqltASRLOWi2DCE%2FS7s7vNHnwf8Qde2XjnGaSpsq%2FENJfrcYnnrFnWxkJfGlHWR3EI9uFCVUuMeYF6XDfYZF3EVPJtZraSknDnBUQC4C6uqmMlKwO5bCmEBMAUvBJTCLiPEiSSJPCFOJO08fDn9NEIodOwcDan6cxZWgNzxY%5Cu003d%7Cav%5Cu003dqifL%2BxvxUBUc1ynBenNIWW%2Fqb5%2FhklbeluSUd3rlkhrYno2P5WL15V7eTIeP9pjzhfHj0VQ17WQmGJ59QA8zz262COTuQTPD93GHs3Q0p03NXGpLkh47R8CYxz85kxEr%2B61C6quZ4HxsJzNZQKiT6Pb%2BuYO2lZjWyuI4gV4Dq68%5Cu003d%7Crn%5Cu003dMoQ4LTvxH79ukadQzE3pvmF4BIijYULXg8VkpsXD1%2FXXBr2zu8YQn7rKy6SOBNdZzIulc8RGPtxx9%2FCtxLl1n57k3peWhDCmpdBvycs4KTnc1DWdeI1rtetAHhfA3WYPZVGlJDt9W%2BlnA2T704LbMK3aFfIKgYFE7slPNwX4wc4%5Cu003d%7Crt%5Cu003dD9nKDmzC6914e0Mkh4Z1G5c1EZznO1DndmVlavofla%2B7BUtDzNlzwAqcsFUq%2Br6Cl4Ci%2BmxP2RruxAEphw%2FnPQQflnT2owIKzK37vJLCL15t%2FNKneFhke6qGC50HuSfQKV7sffbpzbPXV2To6EgDQPldf2YcgYIUaiMXkhkxSxg%5Cu003d%7Crb%5Cu003dD66hUJDxt4m4yVk%2B5sPcsEixzVmDpr1DM1G9SVgYSAz9WbhEd1%2FYpJ7f2%2F%2BN2DlphSRai%2FTxYurasQwAUQ48nczMiscyI4kSVGI%2F2y3DkGJZcE2sYWJQMovQl9dtfnz4bBjCeRBVutQGDJiCrt1XHo4K8Cu1esAqu6WKjHc86yQ%5Cu003d%7Crm%5Cu003dDkIfDTh2Y97qfs9UQsOAOCwoa08AjCZ%2Fc5x2%2FaUGjBBlQk1yQk%2BscjUBBfdgZ7jkVQKCNaDFqKsLU%2BQuOdqp8LvGwVGefFwbK47mao9Usu8wHkEr2l1CabnUsSEuPPGDgo9z0k4cjEH4fZSY%2F026zP%2Bkl2T8VhZox4WkyQWVGD8%5Cu003d%7Cro%5Cu003dCFwpy8CSDqQf4l1V2CMhxnmLGuFnyHuiPgbT%2BkXhkpjOu0bspxlfvKPMz0sk7WbF5pd9XjuHyaKlufDZ5rZNmq6sCQ8JbL8bxvXe%2FL7gi6diX%2Fa%2BZo8L2EjJLt0D%2Fs6DXdHP2juG1FAObVylFUhaWrimZ0UvmNwpyy5uJgmAIK8%5Cu003d%7Crov%5Cu003dKG3vlvFc%2B5BxK9ldoolKqLgB83mKyX6nofF5dWFB9Saq2u3J33%2BxPHMp8EnyR3Z8yu%2Bf11%2B%2Bo%2BDP3HRXKabNxkOhLH3Ehb53T0CcEzxM5z2xNF%2BGiYpHFVgx7043qBhgkvZbm7yXkjNIuF%2BqBebIcRA1JwBOnW8ogb3oE2%2FUS0k%5Cu003d%7Cal%5Cu003dK5t74VWs1mEzA3CyipF%2FXzXuqOr8VY19QEpiq2Y8v3jtF0NDgM4TvzTh2XVOcuJjM9dJmMrDIRpF3P3btEifGd1W%2Fxr5IiluorYMjFUqge4ABrhBSgZ8oCh5zf8bjePn93%2B4ABrGEhd2rDzirnNImU4Ey7fVdD9NFc27Ily3CfY%5Cu003d%22%2C%22mdn%22%3A%22DI%2FZOLVvYnE5cP8Rg8yxagATPCwNkt0lJZDSKY6lJkMCwWhhglqf6EvCLL4SSJvD7tJv2ouok8%2B4GwZUrXtyORzL6NthYtyN1utzOwKYSU0SYxeNkzAZUgrOTGuxyEYB5DbGTzZ6%2BSR8Wgi35I7XqVi1SKpIcHWAalVUo8RUoiA%5Cu003d%22%7D%7D");


                constraintLayout.addView(wv)
                doAfterSpecificTime(time = 5000) {
                    wv.loadUrl(url)
                    wv.reload()


                }
            }
        }

    }


    private fun applyThemeForShimmer() {
        /**
         * need to be refactored : mulitple copies of same code
         */
        when(cardConfiguraton){
            CardConfiguraton.MapConfigruation ->{
                val tapInterface = DataConfiguration.configurationsAsHashMap?.get("interface") as? Map<*, *>
                with(tapInterface?.get("theme").toString()) {
              //      lottieAnimationView.setCacheComposition(true)
                    when (this) {
                        TapTheme.light.name -> {
                         //   setLottieAnimationAccordingToTheme(lightLottieUrlJson,lightLottieAssetName)
                        }
                        TapTheme.dark.name -> {
//                            setLottieAnimationAccordingToTheme(
//                                darkLottieUrlJson,
//                                darkLottieAssetName)
                        }
                        else -> {}
                    }


                }
//                with(tapInterface["edges"].toString()) {
//                    when (this) {
//                        TapCardEdges.flat.name -> {}
//                        TapCardEdges.curved.name -> {
//                            val radius = TypedValue.applyDimension(
//                                TypedValue.COMPLEX_UNIT_DIP,
//                                10f,
//                                context.resources.displayMetrics
//                            )
//                            findViewById<CardView>(R.id.card_lottie).radius = radius
//                        }
//                        else -> {}
//                    }
//                }

              setTapThemeAndLanguage(
                    this.context,
                    TapLocal.valueOf(tapInterface?.get("locale")?.toString() ?: TapLocal.en.name),
                  TapTheme.valueOf(tapInterface?.get("theme")?.toString() ?: TapTheme.light.name))
            }
        }


    }
//
//    private fun setLottieAnimationAccordingToTheme(url:String,assetNameForFailure:String) {
//        lottieAnimationView.setAnimationFromUrl(url)
//        lottieAnimationView.setFailureListener { throwable ->
//            lottieAnimationView.setAnimation(
//                context.getAssetFile(assetNameForFailure)
//            )
//
//        }
//    }

//
//    fun startShimmer() {
//
//        lottieAnimationView.visibility = View.VISIBLE;
//        cardWebview.visibility = View.GONE
//        findViewById<CardView>(R.id.card_lottie).visibility = View.VISIBLE
//
//
//    }

    private fun setTapThemeAndLanguage(context: Context, language: TapLocal?, themeMode: TapTheme?) {
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
        DataConfiguration.setLocale(this.context, language?.name ?:"en", null, this@TapCardKit.context.resources, R.raw.lang)

    }

    private fun stopShimmer() {
     //   lottieAnimationView.visibility = View.GONE;
       // cardWebview.visibility = View.VISIBLE
       // findViewById<CardView>(R.id.card_lottie).visibility = View.GONE

    }



    private fun encodeConfigurationToUrl(configuraton: TapCardConfigurations?): String? {
        val gson = Gson()
        val json: String = gson.toJson(configuraton)

        val encodedUrl = URLEncoder.encode(json, "UTF-8")
        return encodedUrl

    }
    private fun encodeConfigurationMapToUrl(configuraton: HashMap<String,Any>?): String? {
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
                    DataConfiguration.getTapCardStatusListener()?.onReady()

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
                    DataConfiguration.getTapCardStatusListener()?.onSuccess(request?.url?.getQueryParameterFromUri(keyValueName).toString())
                }
                if (request?.url.toString().contains(CardFormWebStatus.onHeightChange.name)) {
                    val newHeight = request?.url?.getQueryParameter(keyValueName)
//                    val params: ViewGroup.LayoutParams? = webViewFrame.layoutParams
//                    params?.height = webViewFrame.context.getDimensionsInDp(newHeight?.toInt() ?: 95)
//                    webViewFrame.layoutParams = params

                    DataConfiguration.getTapCardStatusListener()?.onHeightChange(newHeight.toString())



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

                }
                if (request?.url.toString().contains(CardFormWebStatus.onNfcClick.name)) {
                    /**
                     * navigate to NFC Activity
                     */
                    if (TapNfcUtils.isNfcAvailable(context)) {
                        val intent = Intent(context,NFCLaunchActivity::class.java)
                        (context).startActivity(intent)
                    }else
                    {
                        //TODO:check if u need any other call back here if device doesn't support NFC
                        DataConfiguration.getTapCardStatusListener()?.onError("NFC is not supported on this device")
                    }



                }
                return true

            }
        else return false
        }

        override fun onPageFinished(view: WebView, url: String) {

        }



        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            Log.e("error",error.toString())
            Log.e("error",request.method.toString())
            Log.e("error",request.url.toString())
            Log.e("error",request.isForMainFrame.toString())

            super.onReceivedError(view, request, error)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            Log.e("error",error.toString())
            Log.e("error",handler.toString())
            handler?.proceed()

        }
    }


    inner class HideableWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun shouldOverrideUrlLoading(
            webView: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            isRedirected = true;
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
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


            val intent = Intent(context,ThreeDsWebViewActivity::class.java)
            (context).startActivity(intent)
            ThreeDsBottomSheetFragment.tapCardKit = this@TapCardKit

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
       // cardWebview.loadUrl("javascript:window.generateTapToken()")
    }
}



enum class CardConfiguraton() {
    MapConfigruation, ModelConfiguration
}




