package com.example.tapcardwebsdk.main_activity

import Acceptance
import Addons
import Authentication
import Contact
import Customer
import Fields
import Invoice
import Merchant
import Name
import Phone
import Post
import Refrence
import Scope
import TapAuthentication
import TapCardConfigurations
import Transaction
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.tapcardwebsdk.R
import com.example.tapcardwebsdk.select_choice.SelectChoiceActivity
import com.tap.commondatamodels.cardBrands.CardBrand
import com.tap.commondatamodels.currencies.GlobalCurrency
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit
import company.tap.tapcardformkit.open.web_wrapper.TapCardConfiguration
import `interface`

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /**
         * case of passing a new Model
         */
        getDataFromSelectChoice()

        /**
         * case of passing a hashmap
         */

      // getDataFromHashMap()
        findViewById<TextView>(R.id.tokenizeBtn).setOnClickListener {
            findViewById<TapCardKit>(R.id.tapCardForm).generateTapToken()
        }

    }

    private fun getDataFromHashMap() {

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
        acceptance.put("supportedBrands","[${"VISA"},${"AMEX"}]")
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
         * refrence
         */
        val refrence = java.util.HashMap<String,Any>()
        refrence.put("transaction","tck_LV02G1729746334Xj54695435")
        refrence.put("order","77302326303711438")

        /**
         * authchanel
         */
        val auth = java.util.HashMap<String,Any>()
        auth.put("channel","PAYER_BROWSER")
        auth.put("purpose","PAYMENT_TRANSACTION")


        /**
         * authentication
         */
        val authentication = java.util.HashMap<String,Any>()
        authentication.put("refrence",refrence)
        authentication.put("authentication",auth)





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
        request.put("scope","Authenticate")
        request.put("authentication",authentication)

        TapCardConfiguration.configureWithTapCardDictionaryConfiguration(
            this,
            findViewById<TapCardKit>(R.id.tapCardForm),
            request,
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
                    Toast.makeText(this@MainActivity, "onError", Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun getDataFromSelectChoice() {
        val selectedLanguage: String = intent.getStringExtra("languageSelected").toString()
        val selectedCurrency: String = intent.getStringExtra("selectedCurrency").toString()
        val selectedTheme: String = intent.getStringExtra("themeSelected").toString()
        val selectedCardType: String = intent.getStringExtra("selectedCardType").toString()
        val showSaved = intent.getBooleanExtra("showSaveSwitch", false).toString()
        val selectedCardEdge = intent.getStringExtra("selectedCardEdge")
        val showHideCardHolderName: Boolean = intent.getBooleanExtra("selectedCardHolderName", true)
        val selectedCardBrand: Boolean = intent.getBooleanExtra("selectedCardBrand", true)
        val showHideScanner: Boolean = intent.getBooleanExtra("showHideScanner", true)
        val showHideNFC: Boolean = intent.getBooleanExtra("showHideNFC", true)
        val amount = intent.getStringExtra("amount")
        val cardBrands = intent.getStringArrayListExtra("cardBrands")
        val scopeType: Scope =
            intent.getSerializableExtra("authentication") as Scope
        val powerdBy = intent.getBooleanExtra("showPowerdBy", false)
        val showLoadingState: Boolean = intent.getBooleanExtra("showLoadingState", true)
//        val sdkMode = intent.getSerializableExtra("operation") as SDK
        val sandboxKey = intent.getStringExtra("sandboxKey")
        val productionKey = intent.getStringExtra("productionKey")
        val merchantIdKey = intent.getStringExtra("merchantId")
        val selectedCardDirection = intent.getStringExtra("selectedCardDirection")



        val tapCardConfig = TapCardConfigurations(
            scope = scopeType,
            publicKey = sandboxKey,
            merchant = Merchant(id = merchantIdKey.toString()),
            transaction = Transaction(
                amount = amount.toString(),
                currency = GlobalCurrency.valueOf(selectedCurrency)
            ),
            customer = Customer(
                nameOnCard = DataConfiguration.customerExample?.nameOnCard ?: "test",
                editable = showHideCardHolderName,
                contact = Contact(
                    email = DataConfiguration.customerExample?.contact?.email ?: "test@gmail.com",
                    phone = Phone(
                        countryCode = DataConfiguration.customerExample?.contact?.phone?.countryCode
                            ?: "+20",
                        number = DataConfiguration.customerExample?.contact?.phone?.number ?: "010"
                    )
                ),
                name = mutableListOf<Name>(
                    Name(
                        lang = TapLocal.valueOf(selectedLanguage.lowercase()),
                        first = DataConfiguration.customerExample?.name?.get(0)?.first ?: "first",
                        last = DataConfiguration.customerExample?.name?.get(0)?.last ?: "last",
                        middle = DataConfiguration.customerExample?.name?.get(0)?.middle ?: "middle"
                    )
                )
            ),
            acceptance = Acceptance(
                supportedBrands = cardBrands?.map { CardBrand.valueOf(it) }?.toMutableList()!!,
                supportedCards = if (selectedCardType == CardType.ALL.name) mutableListOf(
                    CardType.DEBIT.name,
                    CardType.CREDIT.name
                ) else mutableListOf(selectedCardType)
            ),
            addons = Addons(
                loader = showLoadingState,
                saveCard = showSaved.toBoolean(),
                displayPaymentBrands = selectedCardBrand,
                scanner = showHideScanner,
                nfc = showHideNFC
            ),
            tapCardConfigurationInterface = `interface`(
                locale = TapLocal.valueOf(selectedLanguage.lowercase()),
                theme = TapTheme.valueOf(selectedTheme.lowercase()),
                edges = TapCardEdges.valueOf(selectedCardEdge.toString()),
                direction = TapCardDirections.valueOf(selectedCardDirection.toString()),
            ),
            fields = Fields(showHideCardHolderName),
            authentication = TapAuthentication(
                description = DataConfiguration.authenticationExample?.description
                    ?: "test Description",
                reference = DataConfiguration.authenticationExample?.reference ?: Refrence(
                    transaction = "tck_LV02G1720231634Xj54695435",
                    order = "77302316303719338"
                ),
                invoice = DataConfiguration.authenticationExample?.invoice
                    ?: Invoice(id = "Test Description"),
                authentication = DataConfiguration.authenticationExample?.authentication
                    ?: Authentication(
                        channel = "PAYER_BROWSER",
                        purpose = "PAYMENT_TRANSACTION"
                    ),
                post = DataConfiguration.authenticationExample?.post ?: Post(url = "")
            )

        )

        TapCardConfiguration.configureWithTapCardModelConfiguration(
            this,
            findViewById<TapCardKit>(R.id.tapCardForm),
            tapCardConfig,
            object : TapCardStatusDelegate {
                override fun onSuccess(data: String) {
                    findViewById<TextView>(R.id.textView_Logs).append("onSuccess $data")

                }

                override fun onReady() {
                    findViewById<TextView>(R.id.textView_Logs).text = ""
                    findViewById<TextView>(R.id.textView_Logs).append("onReady")
                    findViewById<TextView>(R.id.tokenizeBtn).visibility = View.VISIBLE


                }

                override fun onFocus() {
                    findViewById<TextView>(R.id.textView_Logs).text = ""
                    findViewById<TextView>(R.id.textView_Logs).append("onFocus")
                }

                override fun onBindIdentification(data: String) {
                    findViewById<TextView>(R.id.textView_Logs).text = ""
                    findViewById<TextView>(R.id.textView_Logs).append("on BindIdentification $data")

                }

                override fun onValidInput(isValid: String) {
                    findViewById<TextView>(R.id.textView_Logs).text = ""
                    findViewById<TextView>(R.id.textView_Logs).append("onValidInput $isValid")

                }

                override fun onError(error: String) {
                    findViewById<TextView>(R.id.textView_Logs).text = ""
                    findViewById<TextView>(R.id.textView_Logs).append("onError $error")
                }

            })


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SelectChoiceActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
        startActivity(intent)

    }

}