package com.example.tapcardwebsdk.select_choice

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.chillibits.simplesettings.core.SimpleSettings
import com.example.tapcardwebsdk.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        SimpleSettings(this).show {
            Section {
                title = context.getString(R.string.operation)
                InputPref {
                    title = context.getString(R.string.sandbox_key)
                    summary = "pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7"

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

                }
            }
                Section {

                    title = context.getString(R.string.purpose)
                    DropDownPref {
                        title = context.getString(R.string.purpose)
                        summary = "PAYMENT_TRANSACTION"
                        simpleSummaryProvider=true
                        entries = listOf("PAYMENT_TRANSACTION","RECURRING_TRANSACTION","INSTALLMENT_TRANSACTION","ADD_CARD","CARDHOLDER_VERIFICATION")

                    }

                }
                Section {

                    title = context.getString(R.string.transaction)
                    InputPref {
                        title = context.getString(R.string.trans_refrence)
                        summary = "tck_LVL8sXyzVSXfSgG0SFkPvQO1Ns"


                    }
                }
                Section {
                    title = context.getString(R.string.order)
                    InputPref {
                        title = context.getString(R.string.order_id)


                    }
                    InputPref {
                        title = context.getString(R.string.amount)
                        summary = "1"


                    }
                    InputPref {
                        title = context.getString(R.string.order_desc)
                        summary = "test"

                    }
                }
                Section {
                    title = context.getString(R.string.currency)
                    DropDownPref {
                       entries = listOf("KWD","AED","SAR","BHD")
                        summary ="KWD"
                     }

                }
                Section {
                    title = context.getString(R.string.invoice)
                    InputPref {
                        title = context.getString(R.string.invoice_id)



                    }
                    InputPref {
                        title = context.getString(R.string.merchant_id)
                        summary = "1"


                    }

                }

               Section {
                    title = context.getString(R.string.features)
                    for (i in 0 until 6) {
                        SwitchPref {
                            title = if(i==0) context.getString(R.string.loading) else if (i==1) context.getString(R.string.display_payment_brands) else if (i==2) context.getString(R.string.display_card_scanning) else if (i==3) context.getString(R.string.display_nfc)
                            else if (i==4) context.getString(R.string.saveCard) else context.getString(R.string.autoSaveCard)
                            defaultValue = true
                        }
                    }

                }
            Section {
                title = context.getString(R.string.acceptance)
                ListPref {
                    title = context.getString(R.string.supportedFundSource)
                    simpleSummaryProvider = true
                    entries = listOf("ALL", "CREDIT", "DEBIT")
                    defaultIndex = 0



                }
                ListPref {
                    title = context.getString(R.string.supportedPaymentAuthentications)
                    simpleSummaryProvider = true
                    entries = listOf("3DS")
                    defaultIndex = 0

                }
                MSListPref {
                    title = context.getString(R.string.supported_brands)
                    simpleSummaryProvider = true
                    entries = listOf("mada", "visa", "masterCard", "AMEX","omanNet","meeza")

                }

            }
            Section {
                title = context.getString(R.string.fields)
                for (i in 0 until 2) {
                    SwitchPref {
                        title = if(i==0) context.getString(R.string.display_holdername) else context.getString(R.string.display_CVV)
                        defaultValue = true
                    }
                }

            }
            Section {
                title = context.getString(R.string.interface_)
                ListPref {
                    title = context.getString(R.string.choose_language)
                    simpleSummaryProvider = true
                    entries = listOf("English","العربية")
                    defaultIndex = 0

                }
                ListPref {
                    title = context.getString(R.string.choose_theme)
                    simpleSummaryProvider = true
                    entries = listOf("Dark","Light","Dynamic")
                    defaultIndex = 1

                }

                ListPref {
                    title = context.getString(R.string.card_edges)
                    simpleSummaryProvider = true
                    entries = listOf("curved","flat")
                    defaultIndex = 1

                }
                ListPref {
                    title = context.getString(R.string.card_directions)
                    simpleSummaryProvider = true
                    entries = listOf("ltr","dynamic")
                    defaultIndex = 1

                }
                ListPref {
                    title = context.getString(R.string.colorStyle)
                    simpleSummaryProvider = true
                    entries = listOf("colored","monochrome")
                    defaultIndex = 0

                }
                SwitchPref {
                    title = context.getString(R.string.display_powerdby_logo)
                    defaultValue = true
                }

            }
            Section {
                title = context.getString(R.string.redirect)
                InputPref {
                        title = context.getString(R.string.redirect_url)


                    }

            }
            Section {
                title = context.getString(R.string.post)
                InputPref {
                    title = context.getString(R.string.post_url)


                }

            }


        }
    }
        class SettingsFragment : PreferenceFragmentCompat() {
            override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
                setPreferencesFromResource(R.xml.root_preferences, rootKey)
            }
        }


    }
