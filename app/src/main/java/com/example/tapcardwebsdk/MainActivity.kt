package com.example.tapcardwebsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.CustomWebViewWrapper
import company.tap.tapcardformkit.open.web_wrapper.TapCardConfiguration

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val request = java.util.HashMap<String,Any>()

        /**
         * merchant
         */
        val merchant = java.util.HashMap<String,Any>()
        merchant.put("id","")
        /**
         * transaction
         */
        val transaction = java.util.HashMap<String,Any>()
        transaction.put("amount","1")
        transaction.put("currency","SAR")
        /**
         * phone
         */
        val phone = java.util.HashMap<String,Any>()
        phone.put("countryCode","+20")
        phone.put("number","011")

        /**
         * contact
         */
        val contact = java.util.HashMap<String,Any>()
        contact.put("email","test@gmail.com")
        contact.put("phone",phone)

        /**
         * customer
         */
        val customer = java.util.HashMap<String,Any>()
        customer.put("nameOnCard","test")
        customer.put("editable","true")
        customer.put("contact",contact)
        customer.put("name","[{\"lang\":\"en\",\"first\":\"Ahmed\",\"last\":\"Sharkawy\",\"middle\":\"Mohamed\"}]")

        /**
         * acceptance
         */

        val acceptance = java.util.HashMap<String,Any>()
        acceptance.put("supportedBrands","[${"VISA"},${"MADA"}]")
        acceptance.put("supportedCards","[\"CREDIT\",\"DEBIT\"]")

        /**
         * fields
         */
        val fields = java.util.HashMap<String,Any>()
        fields.put("cardHolder","true")

        /**
         * addons
         */
        val addons = java.util.HashMap<String,Any>()
        addons.put("loader","true")
        addons.put("saveCard","true")
        addons.put("displayPaymentBrands","true")
        addons.put("scanner","true")
        addons.put("nfc","true")



        /**
         * interface
         */
        val interf = java.util.HashMap<String,Any>()
        interf.put("locale","en")
        interf.put("theme","light")
        interf.put("edges","curved")
        interf.put("direction","ltr")


        request.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7")
        request.put("merchant",merchant)
        request.put("transaction",transaction)
        request.put("customer",customer)
        request.put("interface",interf)
        request.put("addons",addons)
        request.put("acceptance",acceptance)
        request.put("fields",fields)


        TapCardConfiguration.configureWithTapCardDictionaryConfiguration(
            this,
            findViewById<CustomWebViewWrapper>(R.id.tapCardForm),
            request,
            object : TapCardStatusDelegate {
                override fun onSuccess(data: String) {
                    Toast.makeText(this@MainActivity, "onSuccess $data", Toast.LENGTH_SHORT).show()
                }

                override fun onReady() {
                    Toast.makeText(this@MainActivity, "onReady", Toast.LENGTH_SHORT).show()

                }

                override fun onFocus() {
                    Toast.makeText(this@MainActivity, "onFocus", Toast.LENGTH_SHORT).show()
                }

                override fun onBindIdentification(data: String) {
                    Toast.makeText(
                        this@MainActivity,
                        "on BindIdentification $data",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onValidInput(isValid: String) {
                    Toast.makeText(this@MainActivity, "onValidInput ${isValid}", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onError(error: String) {
                    Toast.makeText(this@MainActivity, "onError", Toast.LENGTH_SHORT).show()
                }

            })

    }

}