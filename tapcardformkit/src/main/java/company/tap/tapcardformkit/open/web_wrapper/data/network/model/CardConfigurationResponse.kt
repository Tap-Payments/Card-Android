package company.tap.tapcardformkit.open.web_wrapper.data.network.model

data class CardConfigurationResponse(
    val android: Android,
    val encryptionkeys: Encryptionkeys,
    val ios: Ios
)
data class Android(
    val `50`: String
)
data class Encryptionkeys(
    val production: String,
    val sandbox: String
)

data class Ios(
    val `27`: String
)