package com.example.tapcardwebsdk.main_activity

import Scope
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.appcompat.widget.SwitchCompat



import com.example.tapcardwebsdk.R
import com.example.tapcardwebsdk.select_choice.SelectChoiceActivity
import com.tap.commondatamodels.cardBrands.CardBrand
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
            findViewById<TapCardKit>(R.id.tapCardForm).generateTapToken()
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

        val selectedLanguage: String = intent.getStringExtra("languageSelected").toString()
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
        val scopeType: Scope = intent.getSerializableExtra("authentication") as Scope
        val powerdBy = intent.getBooleanExtra("showPowerdBy", false)
        val showLoadingState: Boolean = intent.getBooleanExtra("showLoadingState", true)
        val sandboxKey = intent.getStringExtra("sandboxKey")
        val productionKey = intent.getStringExtra("productionKey")
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


        val configuration = LinkedHashMap<String,Any>()

        /**
         * merchant
         */
        val scope = HashMap<String,Any>()
        scope.put("scope",scopeType.name ?: "")

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
         * transaction
         */

        val transaction = java.util.HashMap<String,Any>()

        transaction.put("metadata",metada)
        transaction.put("reference",transactionRefrence ?:"tck_LVL8sXyzVSXfSgG0SFkPvQO1Ns")

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
        customer.put("nameOnCard",DataConfiguration.customerExample?.nameOnCard ?: "test")
        customer.put("editable",cardHolder)
        customer.put("contact",contact)
        customer.put("name", listOf(name))




        /**
         * acceptance
         */
        val acceptance = java.util.HashMap<String,Any>()
        acceptance.put("supportedSchemes", cardBrands?.toList()?: listOf(""))
        acceptance.put("supportedFundSource",if (selectedCardType == CardType.ALL.name) mutableListOf(
                    CardType.DEBIT.name,
                    CardType.CREDIT.name
                ) else mutableListOf(selectedCardType))
        acceptance.put("supportedPaymentAuthentications", mutableListOf("3DS"))

        /**
         * fields
         */
        val fields = HashMap<String,Any>()
        /**
         * card
         */
        val card = HashMap<String,Any>()
         card.put("cvv",cvv)
         card.put("cardHolder",cardHolder)
        fields.put("card",card)

        /**
         * customerCards
         */
        val customerCards = HashMap<String,Any>()
        customerCards.put("saveCard",saveCard)
        customerCards.put("autoSaveCard",autoSaveCard)

        /**
         * features
         */
        val features = HashMap<String,Any>()
        features.put("scanner",showHideScanner)
        features.put("nfc",showHideNFC)
        features.put("acceptanceBadge",showCardBrands)
        features.put("customerCards",customerCards)



        /**
         * addons
         */
        val addons = HashMap<String,Any>()
        addons.put("loader",showLoadingState)

        /**
         * operator
         */
        val operator = HashMap<String,Any>()
        operator.put("publicKey",sandboxKey.toString())


        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id",ordrId ?: "")
        order.put("amount", amount?.toInt() ?: 1)
        order.put("currency",selectedCurrency)
        order.put("description",orderDescription.toString())
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


        configuration.put("publicKey",sandboxKey.toString())
        configuration.put("scope",scopeType)
        configuration.put("purpose",purpose.toString())
        configuration.put("transaction",transaction)
        configuration.put("order",order)
        configuration.put("invoice",invoice)
        configuration.put("merchant",merchant)
        configuration.put("customer",customer)
        configuration.put("features",features)
        configuration.put("acceptance",acceptance)
        configuration.put("fields",fields)
        configuration.put("addons",addons)
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
                }

                override fun onReady() {
                    Toast.makeText(this@MainActivity, "onReady", Toast.LENGTH_SHORT).show()
                    findViewById<TextView>(R.id.tokenizeBtn).visibility = View.VISIBLE

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
                    Toast.makeText(this@MainActivity, "onError ${error}", Toast.LENGTH_SHORT).show()
                }



                override fun onHeightChange(heightChange: String) {
                    Log.e("heightChanged",heightChange.toString())
                }

            })

    }

//    private fun getDataFromSelectChoice() {
//
//
//
//        val tapCardConfig = TapCardConfigurations(
//            scope = scopeType,
//            publicKey = sandboxKey,
//            merchant = Merchant(id = merchantIdKey.toString()),
//            transaction = Transaction(
//                amount = amount.toString(),
//                currency = GlobalCurrency.valueOf(selectedCurrency)
//            ),
//            customer = Customer(
//                nameOnCard = DataConfiguration.customerExample?.nameOnCard ?: "test",
//                editable = showHideCardHolderName,
//                contact = Contact(
//                    email = DataConfiguration.customerExample?.contact?.email ?: "test@gmail.com",
//                    phone = Phone(
//                        countryCode = DataConfiguration.customerExample?.contact?.phone?.countryCode
//                            ?: "+20",
//                        number = DataConfiguration.customerExample?.contact?.phone?.number ?: "010"
//                    )
//                ),
//                name = mutableListOf<Name>(
//                    Name(
//                        lang = TapLocal.valueOf(selectedLanguage.lowercase()),
//                        first = DataConfiguration.customerExample?.name?.get(0)?.first ?: "first",
//                        last = DataConfiguration.customerExample?.name?.get(0)?.last ?: "last",
//                        middle = DataConfiguration.customerExample?.name?.get(0)?.middle ?: "middle"
//                    )
//                )
//            ),
//            acceptance = Acceptance(
//                supportedBrands = mutableListOf(CardBrand.americanExpress,CardBrand.visa,CardBrand.masterCard),
//                supportedCards = if (selectedCardType == CardType.ALL.name) mutableListOf(
//                    CardType.DEBIT.name,
//                    CardType.CREDIT.name
//                ) else mutableListOf(selectedCardType)
//            ),
//            addons = Addons(
//                loader = showLoadingState,
//                saveCard = showSaved.toBoolean(),
//                displayPaymentBrands = selectedCardBrand,
//                scanner = showHideScanner,
//                nfc = showHideNFC
//            ),
//            tapCardConfigurationInterface = `interface`(
//                locale = TapLocal.valueOf(selectedLanguage.lowercase()),
//                theme = TapTheme.valueOf(selectedTheme.lowercase()),
//                edges = TapCardEdges.valueOf(selectedCardEdge.toString()),
//                direction = TapCardDirections.valueOf(selectedCardDirection.toString()),
//            ),
//            fields = Fields(cardHolder = showHideCardHolderName),
//            authentication = TapAuthentication(
//                description = DataConfiguration.authenticationExample?.description
//                    ?: "test Description",
//                reference = DataConfiguration.authenticationExample?.reference ?: Refrence(
//                    transaction = "tck_LV02G1720231634Xj54695435",
//                    order = "77302316303719338"
//                ),
//                invoice = DataConfiguration.authenticationExample?.invoice
//                    ?: Invoice(id = "Test Description"),
//                authentication = DataConfiguration.authenticationExample?.authentication
//                    ?: Authentication(
//                        channel = "PAYER_BROWSER",
//                        purpose = "PAYMENT_TRANSACTION"
//                    ),
//                post = DataConfiguration.authenticationExample?.post ?: Post(url = "")
//            )
//
//        )
//
//
//
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SelectChoiceActivity::class.java)
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


