package company.tap.tapcardformkit.open

import Customer
import TapAuthentication
import TapCardConfigurations
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.open.web_wrapper.TapCardConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit
import company.tap.taplocalizationkit.LocalizationManager
import company.tap.tapuilibrary.themekit.ThemeManager
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







    fun setTheme(
        context: Context?,
        resources: Resources?,
        urlString: String?,
        urlPathLocal: Int?,
        fileName: String?
    ) {
        if (resources != null && urlPathLocal != null) {
            if (fileName != null && fileName.contains("dark")) {
                if (urlPathLocal != null) {
                    ThemeManager.loadTapTheme(resources, urlPathLocal, "darktheme")
                }
            } else {
                if (urlPathLocal != null) {
                    ThemeManager.loadTapTheme(resources, urlPathLocal, "lighttheme")
                }
            }
        } else if (urlString != null) {
            if (context != null) {
                println("urlString>>>" + urlString)
                ThemeManager.loadTapTheme(context, urlString, "lighttheme")
            }
        }

    }

    fun setLocale(
        context: Context,
        languageString: String,
        urlString: String?,
        resources: Resources?,
        urlPathLocal: Int?
    ) {
        LocalizationManager.setLocale(context, Locale(languageString))
        Log.e("lanugage local",LocalizationManager.getLocale(context).language.toString())

        if (resources != null && urlPathLocal != null) {
            LocalizationManager.loadTapLocale(resources, R.raw.lang)
        } else if (urlString != null) {
            if (context != null) {
                LocalizationManager.loadTapLocale(context, urlString)
                Log.e("local", urlString.toString())

            }
        }

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

    fun generateToken(tapCardKit: TapCardKit){
        tapCardKit.generateTapToken()
    }
    fun initializeSDK(activity: Activity,configurations: HashMap<String,Any>,tapCardKit: TapCardKit){
        TapCardConfiguration.configureWithTapCardDictionaryConfiguration(activity,tapCardKit,configurations)
    }






}

interface TapCardStatusDelegate {

    fun onSuccess(data: String)


    fun onReady()

    fun onFocus()

    fun onBindIdentification(data: String)

    fun onValidInput(isValid: String)


    fun onError(error: String)

    fun onHeightChange(heightChange:String)


}
