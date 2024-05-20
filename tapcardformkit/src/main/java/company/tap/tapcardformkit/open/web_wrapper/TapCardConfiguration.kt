package company.tap.tapcardformkit.open.web_wrapper

import Headers
import android.content.Context
import android.util.Log
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.DataConfiguration.configurationsAsHashMap
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.ApiService.BASE_URL
import company.tap.tapcardformkit.open.web_wrapper.data.*
import company.tap.tapcardformkit.open.web_wrapper.data.network.model.TapSDKConfigUrlResponse
import company.tap.tapnetworkkit.connection.NetworkApp
import company.tap.tapnetworkkit.utils.CryptoUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class TapCardConfiguration {

    companion object {
        private val retrofit3 = RetrofitClient.getClient3()
        private val tapSDKConfigsUrl = retrofit3.create(TapSDKConfigUrls::class.java)
        private var testEncKey: String? = null
        private var prodEncKey: String? = null
        private var dynamicBaseUrlResponse: String? = null

        @OptIn(DelicateCoroutinesApi::class)
        fun configureWithTapCardDictionaryConfiguration(
            context: Context,
            tapCardInputViewWeb: TapCardKit?,
            tapMapConfiguration: java.util.HashMap<String, Any>,
            tapCardStatusDelegate: TapCardStatusDelegate? = null,
            cardNumber: String = "",
            cardExpiry: String = "",
        ) {

            MainScope().launch {
                getTapSDKConfigUrls(
                    tapMapConfiguration,
                    tapCardInputViewWeb,
                    context,
                    tapCardStatusDelegate,
                    cardNumber,
                    cardExpiry
                )
            }


            /*  GlobalScope.launch {
           // delay(1800)

            with(tapMapConfiguration) {
                configurationsAsHashMap = tapMapConfiguration
                val operator = configurationsAsHashMap?.get(operatorKey) as HashMap<*, *>
                val publickKey = operator.get(publicKeyToGet)
                addOperatorHeaderField(
                    tapCardInputViewWeb,
                    context,
                    publickKey.toString()
                )
                DataConfiguration.addTapCardStatusDelegate(tapCardStatusDelegate)
                tapCardInputViewWeb?.init(cardNumber.filter { it.isDigit() }, cardExpiry)


            }*/
        }


        fun addOperatorHeaderField(
            tapCardInputViewWeb: TapCardKit?,
            context: Context,
            publicKey: String?
        ) {
            NetworkApp.initNetwork(
                tapCardInputViewWeb?.context,
                publicKey ?: "",
                context.packageName,
                BASE_URL.replace("wrapper?configurations=", ""),//adjusting as its not mattering now
                "android-cardFromKit",
                true,
                getPublicEncryptionKey(publicKey, tapCardInputViewWeb),
                null
            )
            val headers = Headers(
                application = NetworkApp.getApplicationInfo(),
                mdn = CryptoUtil.encryptJsonString(
                    context.packageName.toString(),
                    getPublicEncryptionKey(publicKey, tapCardInputViewWeb)
                )
            )

            val hashMapHeader = HashMap<String, Any>()
            hashMapHeader[HeadersMdn] = headers.mdn.toString()
            hashMapHeader[HeadersApplication] = headers.application.toString()
            configurationsAsHashMap?.put(headersKey, hashMapHeader)


        }


        private suspend fun getTapSDKConfigUrls(
            tapMapConfiguration: HashMap<String, Any>,
            tapCardInputViewWeb: TapCardKit?,
            context: Context,
            tapCardStatusDelegate: TapCardStatusDelegate?,
            cardNumber: String,
            cardExpiry: String
        ) {

            try {
                /**
                 * request to get Tap configs
                 */

                val tapSDKConfigUrlResponse = tapSDKConfigsUrl.getSDKConfigUrl()
                BASE_URL = tapSDKConfigUrlResponse.baseURL
                prodEncKey = tapSDKConfigUrlResponse.prodEncKey
                testEncKey = tapSDKConfigUrlResponse.testEncKey
                urlWebStarter = tapSDKConfigUrlResponse.baseURL

                startSDKWithConfigs(
                    tapMapConfiguration,
                    tapCardInputViewWeb,
                    context,
                    tapCardStatusDelegate,
                    cardNumber,
                    cardExpiry
                )

            } catch (e: Exception) {
                BASE_URL = urlWebStarter
                testEncKey =  tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkey)
                prodEncKey = tapCardInputViewWeb?.context?.resources?.getString(R.string.prodenryptkey)

                startSDKWithConfigs(
                    tapMapConfiguration,
                    tapCardInputViewWeb,
                    context,
                    tapCardStatusDelegate,
                    cardNumber,
                    cardExpiry
                )
                Log.e("error Config", e.message.toString())
            }
        }

        private fun startSDKWithConfigs(
            tapMapConfiguration: HashMap<String, Any>,
            tapCardInputViewWeb: TapCardKit?,
            context: Context,
            tapCardStatusDelegate: TapCardStatusDelegate? = null,
            cardNumber: String = "",
            cardExpiry: String = ""
        ) {
            with(tapMapConfiguration) {
                configurationsAsHashMap = tapMapConfiguration
                val operator = configurationsAsHashMap?.get(operatorKey) as HashMap<*, *>
                val publickKey = operator.get(publicKeyToGet)
                addOperatorHeaderField(
                    tapCardInputViewWeb,
                    context,
                    publickKey.toString()
                )
                DataConfiguration.addTapCardStatusDelegate(tapCardStatusDelegate)
                tapCardInputViewWeb?.init(cardNumber.filter { it.isDigit() }, cardExpiry)


            }
        }


        private fun getPublicEncryptionKey(
            publicKey: String?,
            tapCardInputViewWeb: TapCardKit?
        ): String? {
            if (!testEncKey.isNullOrBlank() && !prodEncKey.isNullOrBlank()) {
                return if (publicKey?.contains("test") == true) {
                   // println("EncKey>>>>>" + testEncKey)
                    testEncKey
                } else {
                  //  println("EncKey<<<<<<" + prodEncKey)
                    prodEncKey
                }
            } else {
              //  println("EncKey<<<<<<>>>>>>>>>" + testEncKey)
                return if (publicKey?.contains("test") == true) {
                    tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkey)
                }else{
                    tapCardInputViewWeb?.context?.resources?.getString(R.string.prodenryptkey)
                }


            }

        }

    }

}




