package company.tap.tapcardformkit.open.web_wrapper

import Headers
import Operator
import TapCardConfigurations
import TapLocal
import TapTheme
import android.content.Context
import android.util.Log
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.doAfterSpecificTime
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.DataConfiguration.configurationsAsHashMap
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapnetworkkit.connection.NetworkApp
import company.tap.tapnetworkkit.utils.CryptoUtil
import company.tap.tapuilibrary.themekit.ThemeManager


class TapCardConfiguration {

    companion object {
//       private fun configureWithTapCardModelConfiguration(
//            context: Context,
//            tapCardInputViewWeb: TapCardKit?,
//            tapCardConfiguration: TapCardConfigurations,
//            tapCardStatusDelegate: TapCardStatusDelegate? = null,
//            ) {
//            with(tapCardConfiguration) {
//                DataConfiguration.configurations = tapCardConfiguration
//                addOperatorHeaderField(
//                    tapCardInputViewWeb, context, CardConfiguraton.ModelConfiguration,
//                    publicKey
//                )
//                DataConfiguration.addTapCardStatusDelegate(tapCardStatusDelegate) //** Required **
//                tapCardInputViewWeb?.init(CardConfiguraton.ModelConfiguration)
//            }
//        }

        fun configureWithTapCardDictionaryConfiguration(
            context: Context,
            tapCardInputViewWeb: TapCardKit?,
            tapMapConfiguration: java.util.HashMap<String, Any>,
            tapCardStatusDelegate: TapCardStatusDelegate? = null,
            cardNumber:String="",
            cardExpiry:String="",
        ) {
            with(tapMapConfiguration) {
                Log.e("map", tapMapConfiguration.toString())
                configurationsAsHashMap = tapMapConfiguration
                val operator = configurationsAsHashMap?.get(operatorKey) as HashMap<*, *>
                val publickKey = operator.get(publicKeyToGet)
                addOperatorHeaderField(
                    tapCardInputViewWeb,
                    context,
                    CardConfiguraton.MapConfigruation,
                    publickKey.toString()
                )

                DataConfiguration.addTapCardStatusDelegate(tapCardStatusDelegate)
                tapCardInputViewWeb?.init(CardConfiguraton.MapConfigruation,cardNumber,cardExpiry)


            }
        }

        fun addOperatorHeaderField(
            tapCardInputViewWeb: TapCardKit?,
            context: Context,
            modelConfiguration: CardConfiguraton,
            publicKey: String?
        ) {
            NetworkApp.initNetwork(
                tapCardInputViewWeb?.context ,
                publicKey ?: "",
                context.packageName,
                ApiService.BASE_URL,
                "android-cardFromKit",
                true,
                tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkey),
                null
            )
            val headers = Headers(
                application = NetworkApp.getApplicationInfo(),
                mdn = CryptoUtil.encryptJsonString(
                    context.packageName.toString(),
                    tapCardInputViewWeb?.context?.resources?.getString(R.string.enryptkey)
                )
            )
            Log.e("publick",publicKey.toString())

//            val operator = Operator(
//                publicKey
//            )

            when (modelConfiguration) {
                CardConfiguraton.MapConfigruation -> {
                    val hashMapHeader = HashMap<String, Any>()
                    hashMapHeader[HeadersMdn] = headers.mdn.toString()
                    hashMapHeader[HeadersApplication] = headers.application.toString()
                    configurationsAsHashMap?.put(headersKey, hashMapHeader)

                }
            }


        }
    }
}


