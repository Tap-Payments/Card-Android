package company.tap.tapcardformkit.open.web_wrapper.data

const val rawFolderRefrence = "raw"
const val CardWebUrlPrefix = "tapCardWebSDK://"
const val keyValueName = "data"
const val firstRunKeySharedPrefrence = "firstRun"

const val packageName = "com.example.tapcardwebsdk"

const val parcelableThreeDsKey = "threeDsUrl"
const val authDataPayerKey = "authDataPayer"
const val waitDelaytime = 4000L
const val chunkSize = 2048
const val keyValueForAuthPayer = "auth_payer"

//const val urlWebStarter = "https://demo.dev.tap.company/v2/sdk/checkout?type=card-iframe&configurations="
const val urlWebStarter = "https://sdk.dev.tap.company/v2/card/wrapper?configurations="
//const val urlWebStarter = "https://sdk.staging.tap.company/v2/card/wrapper?configurations="
const val lightLottieUrlJson = "https://tap-assets.b-cdn.net/card-sdk/shimmer/Light_Mode_Button_Loader.json"
const val darkLottieUrlJson = "https://tap-assets.b-cdn.net/card-sdk/shimmer/Dark_Mode_Button_Loader.json"
const val lightLottieAssetName = "lottie_light"
const val darkLottieAssetName = "lottie_dark"

const val publicKeyToGet ="publicKey"
const val HeadersApplication ="application"
const val HeadersMdn ="mdn"
const val operatorKey ="operator"
const val headersKey ="headers"

enum class CardFormWebStatus {
    onReady, onFocus, onValidInput, onInvalidInput, onError, onSuccess, onBinIdentification, onHeightChange, on3dsRedirect,onScannerClick,onNfcClick
}


