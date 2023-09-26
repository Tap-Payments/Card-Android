package com.example.tapcardwebsdk.main_activity
import Scope
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tapcardwebsdk.R
import com.example.tapcardwebsdk.select_choice.SelectChoiceActivity
import com.tap.commondatamodels.cardBrands.CardBrand
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapcardformkit.open.TapCardStatusDelegate
import company.tap.tapcardformkit.open.web_wrapper.TapCardConfiguration
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit

class MainActivity : AppCompatActivity() {
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
        val cardHolder: Boolean = intent.getBooleanExtra("selectedCardHolderName", true)
        val showCardBrands: Boolean = intent.getBooleanExtra("selectedCardBrand", true)
        val showHideScanner: Boolean = intent.getBooleanExtra("showHideScanner", true)
        val showHideNFC: Boolean = intent.getBooleanExtra("showHideNFC", true)
        val amount = intent.getStringExtra("amount")
        val cardBrands = intent.getStringArrayListExtra("cardBrands")
        val scopeType: Scope =
            intent.getSerializableExtra("authentication") as Scope
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

        Log.e("auth",ordrId.toString() + " " + orderDescription + transactionRefrence + " " + postUrl)

        val configuration = LinkedHashMap<String,Any>()

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
        acceptance.put("supportedBrands", cardBrands?.toList()?: listOf(""))
        acceptance.put("supportedCards",if (selectedCardType == CardType.ALL.name) mutableListOf(
                    CardType.DEBIT.name,
                    CardType.CREDIT.name
                ) else mutableListOf(selectedCardType))

        /**
         * fields
         */
        val fields = HashMap<String,Any>()
        fields.put("cardHolder",cardHolder)

        /**
         * addons
         */
        val addons = HashMap<String,Any>()
        addons.put("loader",showLoadingState)
        addons.put("saveCard",showSaved)
        addons.put("displayPaymentBrands",showCardBrands)
        addons.put("scanner",showHideScanner)
        addons.put("nfc",showHideNFC)


        /**
         * order
         */
        val order = HashMap<String,Any>()
        order.put("id",ordrId.toString())
        order.put("amount",amount.toString())
        order.put("currency",selectedCurrency)
        order.put("description",orderDescription.toString())
        /**
         * interface
         */
        val interfacee = HashMap<String,Any>()
        interfacee.put("locale",selectedLanguage)
        interfacee.put("theme",selectedTheme)
        interfacee.put("edges",selectedCardEdge.toString())
        interfacee.put("direction",selectedCardDirection.toString())




        configuration.put("merchant",merchant)
        configuration.put("transaction",transaction)
        configuration.put("order",order)
        configuration.put("invoice",invoice)
        configuration.put("post",post)
        configuration.put("purpose","PAYMENT_TRANSACTION")
        configuration.put("fields",fields)
        configuration.put("acceptance",acceptance)
        configuration.put("addons",addons)
        configuration.put("publicKey",sandboxKey.toString())
        configuration.put("interface",interfacee)
        configuration.put("scope",scopeType)
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

}


