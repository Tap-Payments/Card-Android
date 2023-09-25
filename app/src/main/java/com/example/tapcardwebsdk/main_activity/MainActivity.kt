package com.example.tapcardwebsdk.main_activity
import Acceptance
import Addons
import Authentication
import CardType
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
import TapCardDirections
import TapCardEdges
import TapLocal
import TapTheme
import Transaction
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tapcardwebsdk.R
import com.example.tapcardwebsdk.select_choice.SelectChoiceActivity
import com.tap.commondatamodels.cardBrands.CardBrand
import com.tap.commondatamodels.currencies.GlobalCurrency
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.TapCardConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit
import `interface`

class MainActivity : AppCompatActivity(),TapCardStatusDelegate {
    lateinit var tapCardKit: TapCardKit
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<TextView>(R.id.tokenizeBtn).setOnClickListener {
            findViewById<TapCardKit>(R.id.tapCardForm).generateTapToken()
        }
        /**
         * case of passing a new Model
         */
      //  getDataFromSelectChoice()

        /**
         * case of passing a hashmap
         */

          getDataFromHashMap()
    }

    private fun getDataFromHashMap() {


        val configuration = LinkedHashMap<String,Any>()

        /**
         * merchant
         */
        val merchant = java.util.HashMap<String,Any>()
        merchant.put("id","")
        /**
         * refrence
         */
//        val refrence = java.util.HashMap<String,Any>()
//        refrence.put("transaction","tck_LVL8sXysVSXfSgG0SFkPhQO1Gi")
//        refrence.put("order","695646918101292112")

//        /**
//         * auth chanel
//         */
//        val auth = java.util.HashMap<String,Any>()
//        auth.put("channel","PAYER_BROWSER")
//        auth.put("purpose","PAYMENT_TRANSACTION")

        /**
         * invoic
         */
        val invoice = java.util.HashMap<String,Any>()
        invoice.put("id","")
        /**
         * post
         */
        val post = java.util.HashMap<String,Any>()
        post.put("url","")

        /**
         * metadata
         */
        val metada = HashMap<String,Any>()
        metada.put("id","")


//        /**
//         * authenticate
//         */
//
//        val authentication = java.util.HashMap<String,Any>()
//        authentication.put("description","description")
//        authentication.put("reference",refrence)
//        authentication.put("invoice",invoice)
//        authentication.put("authentication",auth)
//        authentication.put("post",post)

        /**
         * transaction
         */
        val transaction = java.util.HashMap<String,Any>()
        transaction.put("amount","1")
        transaction.put("currency","SAR")
        transaction.put("description","description")
        transaction.put("metadata",metada)
        transaction.put("reference","tck_LVL8sXyzVSXfSgG0SFkPvQO1Ns")

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
        name.put("lang","en")
        name.put("first","Ahmed")
        name.put("middle","test")
        name.put("last","test")

        /**
         * customer
         */
        val customer = java.util.HashMap<String,Any>()
        customer.put("nameOnCard","test")
        customer.put("editable",true)
        customer.put("contact",contact)
        customer.put("name", listOf(name))




        /**
         * acceptance
         */
        val acceptance = java.util.HashMap<String,Any>()
        acceptance.put("supportedBrands", listOf("MADA","VISA","MASTERCARD","AMEX"))
        acceptance.put("supportedCards",listOf("CREDIT","DEBIT"))

        /**
         * fields
         */
        val fields = HashMap<String,Any>()
        fields.put("cardHolder",true)

        /**
         * addons
         */
        val addons = HashMap<String,Any>()
        addons.put("loader",true)
        addons.put("saveCard",true)
        addons.put("displayPaymentBrands",true)
        addons.put("scanner",true)
        addons.put("nfc",true)


        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id","699246911101421132")
        /**
         * interface
         */
        val interfacee = HashMap<String,Any>()
        interfacee.put("locale","en")
        interfacee.put("theme","light")
        interfacee.put("edges","curved")
        interfacee.put("direction","ltr")
        configuration.put("merchant",merchant)
        configuration.put("transaction",transaction)
        configuration.put("order",order)
        configuration.put("invoice",invoice)
        configuration.put("post",post)
        configuration.put("purpose","PAYMENT_TRANSACTION")
        configuration.put("fields",fields)
        configuration.put("acceptance",acceptance)
        configuration.put("addons",addons)
        configuration.put("publicKey","pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7")
        configuration.put("interface",interfacee)
        configuration.put("scope","Authenticate")
        configuration.put("customer",customer)


        /**
         * from settings we need to add Key :
         */

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
                supportedBrands = mutableListOf(CardBrand.americanExpress,CardBrand.visa,CardBrand.masterCard),
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
            fields = Fields(cardHolder = showHideCardHolderName),
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



    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, SelectChoiceActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        finish()
        startActivity(intent)

    }

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
        DataConfiguration.generateToken(findViewById<TapCardKit>(R.id.tapCardForm))

    }

    override fun onError(error: String) {
        findViewById<TextView>(R.id.textView_Logs).text = ""
        findViewById<TextView>(R.id.textView_Logs).append("onError $error")
    }

    override fun onHeightChange(heightChange: String) {
        Log.e("heightChanged",heightChange.toString())
    }

}