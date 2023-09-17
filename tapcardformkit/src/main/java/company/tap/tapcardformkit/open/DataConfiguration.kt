package company.tap.tapcardformkit.open

import Customer
import TapAuthentication
import TapCardConfigurations
import android.annotation.SuppressLint
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by AhlaamK on 3/23/22.

Copyright (c) 2022    Tap Payments.
All rights reserved.
 **/
@SuppressLint("StaticFieldLeak")
object DataConfiguration {

    private var tapCardStatusDelegate: TapCardStatusDelegate? = null

    var configurations: TapCardConfigurations? = null
        get() = field
        set(value) {
            field = value
        }

    var customerExample: Customer? = null
        get() = field
        set(value) {
            field = value
        }

    var authenticationExample: TapAuthentication? = null
        get() = field
        set(value) {
            field = value
        }
    var configurationsAsJson: String? = null
        get() = field
        set(value) {
            field = value
        }

    var configurationsAsHashMap: HashMap<String,Any>? = null
        get() = field
        set(value) {
            field = value
        }







    fun setCustomer(customer: Customer) {
        customerExample = customer
    }

    fun setTapAuthentication(tapAuthentication: TapAuthentication) {
        authenticationExample = tapAuthentication
    }

    fun addTapCardStatusDelegate(_tapCardStatuDelegate: TapCardStatusDelegate?) {
        this.tapCardStatusDelegate = _tapCardStatuDelegate


    }

    fun getTapCardStatusListener(): TapCardStatusDelegate? {
        return tapCardStatusDelegate
    }




}

interface TapCardStatusDelegate {

    fun onSuccess(data: String)


    fun onReady()

    fun onFocus()

    fun onBindIdentification(data: String)

    fun onValidInput(isValid: String)


    fun onError(error: String)


}
