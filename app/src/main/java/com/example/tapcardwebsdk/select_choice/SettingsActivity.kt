package com.example.tapcardwebsdk.select_choice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.preference.PreferenceFragmentCompat
import com.chillibits.simplesettings.core.SimpleSettings
import com.chillibits.simplesettings.tool.getPrefBooleanValue
import com.chillibits.simplesettings.tool.getPrefObserver
import com.chillibits.simplesettings.tool.getPrefStringValue
import com.chillibits.simplesettings.tool.getPrefs
import com.example.tapcardwebsdk.R
import com.example.tapcardwebsdk.main_activity.MainActivity
import company.tap.tapcardformkit.getRandomNumbers
import company.tap.tapcardformkit.getRandomTrx
import company.tap.tapcardformkit.open.DataConfiguration

class SettingsActivity : AppCompatActivity() {
    private var dataModelChecked: ArrayList<String>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
       /* if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }*/
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        SimpleSettings(this).show {
            Section {
                title = context.getString(R.string.operation)
                InputPref {
                    title = context.getString(R.string.sandbox_key)
                    summary = "pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"
                    defaultValue = "pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"
                    key="publicKey"

                }
            }
            Section {
                title = context.getString(R.string.scope_title)
                DropDownPref {
                    title = context.getString(R.string.Token)
                    summary = "Token"
                    simpleSummaryProvider = true
                    entries =
                        listOf("Token", "AuthenticatedToken", "SaveToken", "SaveAuthenticatedToken")
                    values =
                        listOf("Token", "AuthenticatedToken", "SaveToken", "SaveAuthenticatedToken")
                    key="scopeKey"
                    defaultIndex=0

                }
            }
            Section {

                title = context.getString(R.string.purpose)
                DropDownPref {
                    title = context.getString(R.string.purpose)
                    summary = "PAYMENT_TRANSACTION"
                    simpleSummaryProvider = true
                    entries = listOf(
                        "PAYMENT_TRANSACTION",
                        "RECURRING_TRANSACTION",
                        "INSTALLMENT_TRANSACTION",
                        "ADD_CARD",
                        "CARDHOLDER_VERIFICATION"
                    )
                    values = listOf(
                        "PAYMENT_TRANSACTION",
                        "RECURRING_TRANSACTION",
                        "INSTALLMENT_TRANSACTION",
                        "ADD_CARD",
                        "CARDHOLDER_VERIFICATION"
                    )

                    key="purposeKey"
                    defaultIndex=0

                }

            }
            Section {

                title = context.getString(R.string.transaction)
                InputPref {
                    title = context.getString(R.string.trans_refrence)
                    summary = "tck_LVL8sXyzVSXfSgG0SFkPvQO1Ns"
                    defaultValue = "tck_LVL8sXyzVSXfSgG0SFkPvQO1Ns"
                    key="transRefKey"



                }
            }
            Section {
                title = context.getString(R.string.order)
                InputPref {
                    title = context.getString(R.string.order_id)
                    key="orderIdKey"


                }
                InputPref {
                    title = context.getString(R.string.amount)
                    summary = "2"
                    key="amountKey"
                    defaultValue="2"


                }
                InputPref {
                    title = context.getString(R.string.order_desc)
                    summary = "test"
                    key="orderDescKey"
                    defaultValue="test"

                }
            }
            Section {
                title = context.getString(R.string.currency)
                DropDownPref {
                    entries = listOf("KWD", "AED", "SAR", "BHD")
                    values = listOf("KWD", "AED", "SAR", "BHD")
                    key="selectedCurrencyKey"
                    defaultIndex=0

                }

            }
            Section {
                title = context.getString(R.string.invoice)
                InputPref {
                    title = context.getString(R.string.invoice_id)
                    key="invoiceIdKey"


                }
                InputPref {
                    title = context.getString(R.string.merchant_id)
                   key="merchantIdKey"


                }

            }

            Section {
                title = context.getString(R.string.features)
                for (i in 0 until 6) {
                    SwitchPref {
                        title =
                            if (i == 0) context.getString(R.string.loading) else if (i == 1) context.getString(
                                R.string.display_payment_brands
                            ) else if (i == 2) context.getString(R.string.display_card_scanning) else if (i == 3) context.getString(
                                R.string.display_nfc
                            )
                            else if (i == 4) context.getString(R.string.saveCard) else context.getString(
                                R.string.autoSaveCard
                            )
                        defaultValue = true
                        key=if (i == 0) "showLoadingKey" else if (i == 1)"displayPymtBrndKey" else if (i == 2) "displayScannerKey" else if (i == 3) "displayNFCKey"
                        else if (i == 4) "displaySaveCardKey" else "displayAutosaveCardKey"
                    }
                }

            }
            Section {
                title = context.getString(R.string.acceptance)
                ListPref {
                    title = context.getString(R.string.supportedFundSource)
                    simpleSummaryProvider = true
                    entries = listOf("ALL", "CREDIT", "DEBIT")
                    values = listOf("ALL", "CREDIT", "DEBIT")
                    defaultIndex = 0
                    key="supportedFundSourceKey"


                }
                ListPref {
                    title = context.getString(R.string.supportedPaymentAuthentications)
                    simpleSummaryProvider = true
                    entries = listOf("3DS")
                    values = listOf("3DS")
                    defaultIndex = 0

                }
                MSListPref {
                    title = context.getString(R.string.supported_brands)
                    simpleSummaryProvider = true
                    entries = listOf("mada", "visa", "masterCard", "AMEX", "omanNet", "meeza")
                    key="selectedBrandsKey"

                }

            }
            Section {
                title = context.getString(R.string.fields)
                SwitchPref {
                        title =
                           context.getString(R.string.display_holdername)
                        defaultValue = true
                        key="displayHoldernameKey"
                    }
                    SwitchPref {
                        title =
                           context.getString(
                                R.string.display_CVV
                            )
                        defaultValue = true
                        key="displayCVVKey"
                    }


            }
            Section {
                title = context.getString(R.string.interface_)
                ListPref {
                    title = context.getString(R.string.choose_language)
                    simpleSummaryProvider = true
                    entries = listOf("English", "العربية")
                    values=  listOf("en", "ar")
                    defaultIndex = 0
                    key="selectedlangKey"

                }
                ListPref {
                    title = context.getString(R.string.choose_theme)
                    simpleSummaryProvider = true
                    entries = listOf("dark", "light", "dynamic")
                    values = listOf("dark", "light", "dynamic")
                    defaultIndex = 1
                    key="selectedthemeKey"

                }

                ListPref {
                    title = context.getString(R.string.card_edges)
                    simpleSummaryProvider = true
                    entries = listOf("curved", "flat")
                    values = listOf("curved", "flat")
                    defaultIndex = 1
                    key="selectedcardedgeKey"

                }
                ListPref {
                    title = context.getString(R.string.card_directions)
                    simpleSummaryProvider = true
                    entries = listOf("ltr", "dynamic")
                    values = listOf("ltr", "dynamic")
                    defaultIndex = 1
                    key="selectedcardirectKey"

                }
                ListPref {
                    title = context.getString(R.string.colorStyle)
                    simpleSummaryProvider = true
                    entries = listOf("colored", "monochrome")
                    values = listOf("colored", "monochrome")
                    defaultIndex = 0
                    key="selectedcolorstyleKey"

                }
                SwitchPref {
                    title = context.getString(R.string.display_powerdby_logo)
                    defaultValue = true
                    key="displayPoweredByKey"
                }

            }
            Section {
                title = context.getString(R.string.redirect)
                InputPref {
                    title = context.getString(R.string.redirect_url)
                    key="redirectUrlKey"


                }

            }
            Section {
                title = context.getString(R.string.post)
                InputPref {
                    title = context.getString(R.string.post_url)
                    key="posturlKey"


                }

            }


        }


    }
        class SettingsFragment : PreferenceFragmentCompat() {
            override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
              //  setPreferencesFromResource(R.xml.root_preferences, rootKey)
            }
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menumain, menu)
        return true
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_generateRandom -> {
            findViewById<EditText>(R.id.trans_refrence).setText(getRandomTrx())
            findViewById<EditText>(R.id.order_id).setText(getRandomNumbers(17))

            Log.e("data", DataConfiguration.authenticationExample.toString())
            true

        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
    fun startTokenizationactivity(view: View) {

        getPrefObserver(this@SettingsActivity, "selectedCurrencyKey", Observer<String> { value ->
            println("vall"+value)
        })
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("languageSelected", getPrefStringValue("selectedlangKey"))
            intent.putExtra("themeSelected", getPrefStringValue("selectedthemeKey"))
            intent.putExtra("selectedCardBrand", getPrefBooleanValue("displayPymtBrndKey",true))
            intent.putExtra("showHideScanner", getPrefBooleanValue("displayScannerKey",true))
            intent.putExtra("showHideNFC", getPrefBooleanValue("displayNFCKey",true))
            intent.putExtra("selectedCurrency", getPrefStringValue("selectedCurrencyKey"))
            intent.putExtra("selectedCardType", getPrefStringValue("supportedFundSourceKey"))
           // intent.putExtra("setdefaultHolderName", setdefaultHolderName)
          //  intent.putExtra("defaultCardHolderName", defaultCardHolderName)
          //  intent.putExtra("defaultColorScanner", defaultScannerBorder)
          //  intent.putExtra("setHeaderView", setHeaderView)
            intent.putExtra("showLoadingState", getPrefBooleanValue("showLoadingKey",true))
         //   intent.putExtra("editDefaultHolderName", editDefaultHolderName)
            intent.putExtra("selectedCardEdge", getPrefStringValue("selectedcardedgeKey"))
            intent.putExtra("selectedCardDirection", getPrefStringValue("selectedcardirectKey"))
            /**
             * new configs
             */
            intent.putExtra("orderId", getPrefStringValue("orderIdKey"))
            intent.putExtra("orderDescription", getPrefStringValue("orderDescKey"))
            intent.putExtra("transactionRefrence",getRandomTrx())
            intent.putExtra("invoiceId",getPrefStringValue("invoiceIdKey"))
            intent.putExtra("postUrl", getPrefStringValue("posturlKey"))

            /**
             * added newly
             */
           // Log.e("cardBrands", dataModelChecked.toString())
            Log.e("cardBrands",  getPrefs().getStringSet("selectedBrandsKey",null).toString())
            intent.putExtra("amount", getPrefStringValue("amountKey"))
          //  intent.putStringArrayListExtra("cardBrands", getPrefStringValue("selectedBrandsKey") as ArrayList<String>)
        val cardBrandArrayList: ArrayList<String> = ArrayList<String>(getPrefs().getStringSet("selectedBrandsKey",null))

        intent.putStringArrayListExtra("cardBrands", cardBrandArrayList)


            intent.putExtra("showPowerdBy", getPrefBooleanValue("displayPoweredByKey",true))
            intent.putExtra("publicKey", getPrefStringValue("publicKey"))
            intent.putExtra("merchantId", getPrefStringValue("merchantIdKey"))

            /**
             * new config
             */
            intent.putExtra("scope", getPrefStringValue("scopeKey"))

            intent.putExtra("purpose", getPrefStringValue("purposeKey"))
            intent.putExtra("saveCard", getPrefBooleanValue("displaySaveCardKey",true))
            intent.putExtra("autoSaveCard", getPrefBooleanValue("displayAutosaveCardKey",true))
            intent.putExtra("redirectURL", getPrefStringValue("redirectUrlKey"))
            intent.putExtra("selectedColorStyle", getPrefStringValue("selectedcolorstyleKey"))
            intent.putExtra("cardHolder",  getPrefBooleanValue("displayHoldernameKey",true))
            intent.putExtra("cvv",getPrefBooleanValue("displayCVVKey",true))



            finish()
            startActivity(intent)



    }

    fun getPrefValue(prefString: String){

    }



}
