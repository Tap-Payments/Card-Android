package company.tap.tapcardformkit.open.web_wrapper.enums



enum class CardFormWebStatus() {
    onReady, onFocus, onValidInput, onInvalidInput, onError, onSuccess, onBinIdentification, onHeightChange, on3dsRedirect,onScannerClick,onNfcClick
}
enum class PaymentChannels{
    PAYMENT_TRANSACTION,RECURRING_TRANSACTION,INSTALLMENT_TRANSACTION,ADD_CARD,CARDHOLDER_VERIFICATION,SAVED_CARD
}

enum class TapCardColorStyle {
    colored, monochrome
}