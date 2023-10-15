package com.example.tapcardwebsdk.main_activity
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tapcardwebsdk.R
import com.example.tapcardwebsdk.select_choice.SelectChoiceActivity
import com.example.tapcardwebsdk.select_choice.SettingsActivity
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.TapCardConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit
import java.util.ArrayList
class MainActivity : AppCompatActivity() {
    lateinit var tapCardKit: TapCardKit
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 7
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_main)

        checkAndroidVersion()
        findViewById<TextView>(R.id.tokenizeBtn).setOnClickListener {
        //    findViewById<TapCardKit>(R.id.tapCardForm).generateTapToken()
            DataConfiguration.generateToken(findViewById<TapCardKit>(R.id.tapCardForm))

        }
        /**
         * case of passing a new Model
         */
       // getDataFromSelectChoice()

        /**
         * case of passing a hashmap
         */

          getDataFromHashMap()
    }

    private fun getDataFromHashMap() {
        val lang = intent.getStringExtra("languageSelected").toString()
        val selectedLanguage: String = lang.toString()
        val selectedCurrency: String = intent.getStringExtra("selectedCurrency").toString()
        val selectedTheme: String = intent.getStringExtra("themeSelected").toString()
        val selectedCardType: String = intent.getStringExtra("selectedCardType").toString()
        val showSaved = intent.getBooleanExtra("showSaveSwitch", false)
        val selectedCardEdge = intent.getStringExtra("selectedCardEdge")
        val showCardBrands: Boolean = intent.getBooleanExtra("selectedCardBrand", true)
        val showHideScanner: Boolean = intent.getBooleanExtra("showHideScanner", true)
        val showHideNFC: Boolean = intent.getBooleanExtra("showHideNFC", true)
        val amount = intent.getStringExtra("amount")
        val cardBrands = intent.getStringArrayListExtra("cardBrands")
        val cardFundSources = intent.getStringArrayListExtra("cardFundSources")

        val scopeType = intent.getStringExtra("scope")
        val powerdBy = intent.getBooleanExtra("showPowerdBy", false)
        val showLoadingState: Boolean = intent.getBooleanExtra("showLoadingState", true)
        val sandboxKey = intent.getStringExtra("publicKey")
        val merchantIdKey = intent.getStringExtra("merchantId")
        val selectedCardDirection = intent.getStringExtra("selectedCardDirection")

        val ordrId =  intent.getStringExtra("orderId")
        val orderDescription =  intent.getStringExtra("orderDescription")
        val transactionRefrence =  intent.getStringExtra("transactionRefrence")
        val postUrl =  intent.getStringExtra("postUrl")
        val invoiceId =  intent.getStringExtra("invoiceId")

        /**
         * new
         */
        val purpose =  intent.getStringExtra("purpose")
        val saveCard =  intent.getBooleanExtra("saveCard",true)
        val autoSaveCard =  intent.getBooleanExtra("autoSaveCard",true)
        val redirectURL =  intent.getStringExtra("redirectURL")
        val selectedColorStyle =  intent.getStringExtra("selectedColorStyle")
        val cardHolder =  intent.getBooleanExtra("cardHolder",true)
        val cvv =  intent.getBooleanExtra("cvv",true)

        /**
         * operator
         */
        val operator = HashMap<String,Any>()
        operator.put("publicKey",sandboxKey.toString())

        /**
         * merchant
         */
        val scope = HashMap<String,Any>()
        scope.put("scope",scopeType ?: "")

        /**
         * merchant
         */
        val merchant = HashMap<String,Any>()
        merchant.put("id",merchantIdKey ?: "")

        /**
         * invoice
         */
        val invoice = HashMap<String,Any>()
        invoice.put("id",invoiceId.toString())
        /**
         * post
         */
        val post = HashMap<String,Any>()
        post.put("url",postUrl.toString())


        /**
         * redirect
         */
        val redirect = HashMap<String,Any>()
        redirect.put("url",redirectURL.toString())

        /**
         * metadata
         */
        val metada = HashMap<String,Any>()
        metada.put("id","")


        /**
         * contract
         */
        val contract = HashMap<String,Any>()
        contract.put("id","")

        /**
         * paymentAgreement
         */
        val paymentAgreement = HashMap<String,Any>()
        paymentAgreement.put("id","")
        paymentAgreement.put("contract",contract)


        /**
         * transaction
         */

        val transaction = HashMap<String,Any>()

        transaction.put("paymentAgreement",paymentAgreement)
        transaction.put("metadata",metada)

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
         * name
         */
        val name = java.util.HashMap<String,Any>()
        name.put("lang",selectedLanguage)
        name.put("first", DataConfiguration.customerExample?.name?.get(0)?.first ?: "first")
        name.put("middle",DataConfiguration.customerExample?.name?.get(0)?.middle ?: "middle")
        name.put("last",DataConfiguration.customerExample?.name?.get(0)?.last ?: "last")

        /**
         * customer
         */
        val customer = java.util.HashMap<String,Any>()
        customer.put("nameOnCard", "test")
        customer.put("editable",cardHolder)
        customer.put("contact",contact)
        customer.put("name", listOf(name))




        /**
         * acceptance
         */
        val acceptance = java.util.HashMap<String,Any>()
        acceptance.put("supportedSchemes", cardBrands?.toList()?: listOf(""))
        acceptance.put("supportedFundSource",cardFundSources?.toList()?: listOf(""))
        acceptance.put("supportedPaymentAuthentications", mutableListOf("3DS"))

        /**
         * fields
         */
        val fieldsVisibility = HashMap<String,Any>()
        /**
         * card
         */
        val card = HashMap<String,Any>()
         card.put("cvv",cvv)
         card.put("cardHolder",cardHolder)
        fieldsVisibility.put("card",card)

        /**
         * customerCards
         */
        val customerCards = HashMap<String,Any>()
        customerCards.put("saveCard",saveCard)
        customerCards.put("autoSaveCard",autoSaveCard)

        /**
         * alternative cards
         */
        val alternativeCardInput = HashMap<String,Any>()
        alternativeCardInput.put("cardScanner",showHideScanner)
        alternativeCardInput.put("cardNFC",showHideNFC)
        /**
         * features
         */
        val features = HashMap<String,Any>()
        features.put("acceptanceBadge",showCardBrands)
        features.put("customerCards",customerCards)
        features.put("alternativeCardInputs",alternativeCardInput)



        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id",ordrId ?: "")
        order.put("amount", if (amount?.isEmpty() == true) "1" else amount.toString())
        order.put("currency",selectedCurrency)
        order.put("description",orderDescription.toString())
        order.put("reference",transactionRefrence ?:"")

        /**
         * interface
         */
        val interfacee = HashMap<String,Any>()
        interfacee.put("locale",selectedLanguage)
        interfacee.put("theme",selectedTheme)
        interfacee.put("edges",selectedCardEdge.toString())
        interfacee.put("cardDirection",selectedCardDirection.toString())
        interfacee.put("powered",powerdBy)
        interfacee.put("colorStyle",selectedColorStyle.toString())
        interfacee.put("loader",showLoadingState)

        val configuration = LinkedHashMap<String,Any>()

        configuration.put("operator",operator)
        configuration.put("scope",scopeType.toString())
        configuration.put("order",order)
        configuration.put("customer",customer)
        configuration.put("purpose",purpose.toString())
        configuration.put("transaction",transaction)
        configuration.put("invoice",invoice)
        configuration.put("merchant",merchant)
        configuration.put("features",features)
        configuration.put("acceptance",acceptance)
        configuration.put("fieldsVisibility",fieldsVisibility)
        configuration.put("interface",interfacee)
        configuration.put("redirect",redirect)
        configuration.put("post",post)


        TapCardConfiguration.configureWithTapCardDictionaryConfiguration(
            this,
            findViewById<TapCardKit>(R.id.tapCardForm),
            configuration,
            object : TapCardStatusDelegate {
                override fun onSuccess(data: String) {
                    Toast.makeText(this@MainActivity, "onSuccess $data", Toast.LENGTH_SHORT).show()
                    println("onSuccess $data")
                }

                override fun onReady() {
                 //   Toast.makeText(this@MainActivity, "onReady", Toast.LENGTH_SHORT).show()
                    findViewById<TextView>(R.id.tokenizeBtn).visibility = View.VISIBLE

                }
//                override fun onFocus() {
//                    Toast.makeText(this@MainActivity, "onFocus", Toast.LENGTH_SHORT).show()
//                }

//
//                override fun onBindIdentification(data: String) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        "on BindIdentification $data",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }

                override fun onValidInput(isValid: String) {
            //        Toast.makeText(this@MainActivity, "onValidInput ${isValid}", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: String) {
                    Toast.makeText(this@MainActivity, "onError ${error}", Toast.LENGTH_SHORT).show()
                    Log.e("test",error.toString())

                }



//                override fun onHeightChange(heightChange: String) {
//                    Log.e("heightChanged",heightChange.toString())
//                }

            })

    }


    override fun onBackPressed() {
        super.onBackPressed()
     //   val intent = Intent(this, SettingsActivity::class.java)

        val intent = Intent(this, SettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
        startActivity(intent)

    }
    private fun checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions()
        } else {
            // code for lollipop and pre-lollipop devices
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val camera = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        )
        val wtite =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val read =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

}


