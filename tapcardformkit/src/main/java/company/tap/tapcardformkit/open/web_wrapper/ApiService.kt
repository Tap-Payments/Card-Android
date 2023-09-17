package company.tap.tapcardformkit.open.web_wrapper

import androidx.annotation.RestrictTo

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
@RestrictTo(RestrictTo.Scope.LIBRARY)
object ApiService {
  //  const val BASE_URL = "https://api.tap.company/v2/"
 //   const val BASE_URL = "https://checkout-mw-java.dev.tap.company/api/"
  const val BASE_URL = "https://mw-sdk.dev.tap.company/v2/checkout/"
  //  const val INIT = "init"
  //  const val INIT = "checkout/init"
  const val INIT = "checkoutprofile"
    /**
     * The Token.
     */
    const val TOKEN = "token/"
    const val PAYMENT_TYPES = "payment/types"
    const val RETURN_URL ="gosellsdk://return_url"
    //const val BIN ="bin/"
    const val CONFIG ="config"
    const val BIN ="card/bin/"

  /**
   * The Save card.
   */
  const val SAVE_CARD = "card/verify"
  const val SAVE_CARD_ID = "card/verify/"
}