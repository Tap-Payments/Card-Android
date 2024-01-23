package company.tap.tapcardformkit.open.web_wrapper

import Headers
import android.content.Context
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.DataConfiguration.configurationsAsHashMap
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.data.*
import company.tap.tapnetworkkit.connection.NetworkApp
import company.tap.tapnetworkkit.utils.CryptoUtil


class TapCardConfiguration {

    companion object {

        fun configureWithTapCardDictionaryConfiguration(
            context: Context,
            tapCardInputViewWeb: TapCardKit?,
            tapMapConfiguration: java.util.HashMap<String, Any>,
            tapCardStatusDelegate: TapCardStatusDelegate? = null,
            cardNumber: String = "",
            cardExpiry: String = "",
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

        fun addOperatorHeaderField(
            tapCardInputViewWeb: TapCardKit?,
            context: Context,
            publicKey: String?
        ) {
            NetworkApp.initNetwork(
                tapCardInputViewWeb?.context,
                publicKey ?: "",
                "com.baraka.app",
                ApiService.BASE_URL,
                "android-cardFromKit",
                true,
                tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkey),
                null
            )
            val headers = Headers(
                application = NetworkApp.getApplicationInfo(),
                mdn = CryptoUtil.encryptJsonString(
                    "com.baraka.app",
                    tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkey)
                )
            )

            val hashMapHeader = HashMap<String, Any>()
            hashMapHeader[HeadersMdn] = headers.mdn.toString()
            hashMapHeader[HeadersApplication] = headers.application.toString()
            configurationsAsHashMap?.put(headersKey, hashMapHeader)


        }
    }
}


