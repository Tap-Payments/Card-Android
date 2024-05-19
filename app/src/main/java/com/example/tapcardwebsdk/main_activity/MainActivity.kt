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
        val cardNumber= intent.getStringExtra("cardNumberKey")
        val cardExpiry = intent.getStringExtra("cardExpiryKey")
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
     //   operator.put("publicKey","pk_live_opg04O1K8UdsnAmxqIkyEeT5")

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
        val fieldVisibility = HashMap<String,Any>()
        /**
         * card
         */
        val card = HashMap<String,Any>()
         card.put("cvv",cvv)
         card.put("cardHolder",cardHolder)
        fieldVisibility.put("card",card)

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
        configuration.put("fieldVisibility",fieldVisibility)
        configuration.put("interface",interfacee)
        configuration.put("redirect",redirect)
        configuration.put("post",post)

        Log.e("cardPrefil", "cardnumber + $cardNumber + card + $cardExpiry")
        if (cardNumber != null) {
            if (cardExpiry != null) {
                TapCardConfiguration.configureWithTapCardDictionaryConfiguration(
                    context = this,
                    tapCardInputViewWeb= findViewById<TapCardKit>(R.id.tapCardForm),
                    tapMapConfiguration =  configuration,
                    tapCardStatusDelegate =   object : TapCardStatusDelegate {
                        override fun onSuccess(data: String) {

                            Toast.makeText(this@MainActivity, "onSuccess $data", Toast.LENGTH_SHORT).show()
                            Log.e("data",data.toString())
                            println("onSuccess $data")
                        }

                        override fun onReady() {
                            //   Toast.makeText(this@MainActivity, "onReady", Toast.LENGTH_SHORT).show()
                            findViewById<TextView>(R.id.tokenizeBtn).visibility = View.VISIBLE

                        }

                        override fun onBindIdentification(data: String) {
                            Log.e("data",data.toString())

                        }


                        override fun onValidInput(isValid: String) {
                        }

                        override fun onError(error: String) {
                            Toast.makeText(this@MainActivity, "onError ${error}", Toast.LENGTH_SHORT).show()
                            Log.e("test",error.toString())

                        }


                    },cardNumber,cardExpiry)
            }
        }

//        findViewById<WebView>(R.id.default_webview).settings.javaScriptEnabled = true
//        findViewById<WebView>(R.id.default_webview).settings.domStorageEnabled = true
//        findViewById<WebView>(R.id.default_webview).settings.cacheMode = WebSettings.LOAD_NO_CACHE
//
//        findViewById<WebView>(R.id.default_webview).loadUrl("https://sdk.dev.tap.company/v2/card/wrapper?configurations=%7B%22operator%22%3A%7B%22publicKey%22%3A%22pk_test_YhUjg9PNT8oDlKJ1aE2fMRz7%22%7D%2C%22scope%22%3A%22Token%22%2C%22order%22%3A%7B%22reference%22%3A%22%22%2C%22amount%22%3A%221%22%2C%22description%22%3A%22%22%2C%22currency%22%3A%22KWD%22%2C%22id%22%3A%22%22%7D%2C%22customer%22%3A%7B%22nameOnCard%22%3A%22test%22%2C%22editable%22%3Atrue%2C%22contact%22%3A%7B%22phone%22%3A%7B%22number%22%3A%22011%22%2C%22countryCode%22%3A%22%2B20%22%7D%2C%22email%22%3A%22test%40gmail.com%22%7D%2C%22name%22%3A%5B%7B%22middle%22%3A%22middle%22%2C%22last%22%3A%22last%22%2C%22lang%22%3A%22en%22%2C%22first%22%3A%22first%22%7D%5D%7D%2C%22purpose%22%3A%22Transaction%22%2C%22transaction%22%3A%7B%22metadata%22%3A%7B%22id%22%3A%22%22%7D%2C%22paymentAgreement%22%3A%7B%22contract%22%3A%7B%22id%22%3A%22%22%7D%2C%22id%22%3A%22%22%7D%7D%2C%22invoice%22%3A%7B%22id%22%3A%22%22%7D%2C%22merchant%22%3A%7B%22id%22%3A%22%22%7D%2C%22features%22%3A%7B%22customerCards%22%3A%7B%22saveCard%22%3Atrue%2C%22autoSaveCard%22%3Atrue%7D%2C%22alternativeCardInputs%22%3A%7B%22cardScanner%22%3Atrue%2C%22cardNFC%22%3Atrue%7D%2C%22acceptanceBadge%22%3Atrue%7D%2C%22acceptance%22%3A%7B%22supportedSchemes%22%3A%5B%5D%2C%22supportedFundSource%22%3A%5B%5D%2C%22supportedPaymentAuthentications%22%3A%5B%223DS%22%5D%7D%2C%22fieldVisibility%22%3A%7B%22card%22%3A%7B%22cvv%22%3Atrue%2C%22cardHolder%22%3Atrue%7D%7D%2C%22interface%22%3A%7B%22powered%22%3Atrue%2C%22loader%22%3Atrue%2C%22edges%22%3A%22flat%22%2C%22colorStyle%22%3A%22colored%22%2C%22theme%22%3A%22light%22%2C%22cardDirection%22%3A%22dynamic%22%2C%22locale%22%3A%22en%22%7D%2C%22redirect%22%3A%7B%22url%22%3A%22%22%7D%2C%22post%22%3A%7B%22url%22%3A%22%22%7D%2C%22headers%22%3A%7B%22application%22%3A%22cu%5Cu003dZexEgJuZsBpoqtD7uSETy6RTNaq3AYmcOu1NjxZ2T1g5NqOem8qNJcc7XkrnWs9HU8aanDJmBHw2pMzS%2FwYedfjXGIVAcctVInohPd%2FbU0AYrJs4A4cX3DdHYzEzXkx2b91MHGPyfJ%2B%2B8Tl2LSrwsNH%2BqQUP45Seou7ctbpOk5E%5Cu003d%7Caid%5Cu003daUl28yWuMIY6epYoQlEauHWKrbbQct1ScGnkQQfgbAJz3H2mqF8sRb8YwlO0lAhdNenQuEvA4kNPOO07JLorNtjUYM1IFCDllRrJcHsD67s29wDcamblTjwJRAeF%2BdLVwd%2BZwkjTytdHMUZgkRsxCG7IhTx%2FFP6o95iVZ44H%2BjQ%5Cu003d%7Cat%5Cu003dVIc1Csd%2BuM5NLnl2Y0k4D%2B5s8lhKErYpUcii49jKF5QvLPIrIOJrw842X2mbfT9Q5271cKTyLTuaFI99N%2BRhErNfe0ANVsBGw6AX9ueOlO270Hc%2Fph9%2F4AxRX0xOVOWOoCmSDnVg%2FrdFBsi2yez%2F2PjTceBUpqO4I5Zt0WKaMT4%5Cu003d%7Cav%5Cu003dU3JL1gyE%2FZ1NyjPBBarQCC%2FTgWB3NdJ6MX8KLmpuzdZ9EkaIBZed24iCNSRxLjPTq8MZZutcw%2BoDN8AevNlfwNgwskeV8FU2zYvDO7XrYnDHTdJzrHC6y9qsyIltbnSnG3sjEn7bEgIOZWpJJx7YYXaHVkF4axu0kXQYaK0ha5g%5Cu003d%7Crn%5Cu003dUCqJoa0atyT13NW49ufg%2Br4F7j0DXMb1elMzTq0MeTQHT3Z7njRDY7lQ107H0mzAZLQH%2FsmDf%2BWyjqF2l%2FuzDVyT4hzuzlETGe9AvD3IjiBMBIQXPgKFWo4ihh21kNFc3jzw4hBgrs%2BgSvHpSYInEK8WF4u2qMsJSAdfafp%2BSa4%5Cu003d%7Crt%5Cu003dkQxC4ou6YC72Pl%2FVI2tMP38tOJK%2BNeMXc4%2FGDP65ZeQ4TdZHx3OBkEFfUhbgZZl%2BICSKGT3Yo3mfqGfYjCpW5RsDRlT5BFG%2FssOg99fJQflq9W0sLe6dtbpoDo%2FzOCWxGzo1nLTbIGPe0XHSRY%2Bb6%2F9xh9upg%2FSp77xQs6XEM2Q%5Cu003d%7Crb%5Cu003db1kjnxsDeVD6xM9jEh3bMw0It19G7MoMAC%2BpMqnvmdmn66rlFcQMABzN7HbX32Fq49Zo%2F0SvneGoBWZ8QCgOJ4guMfz46pJDIltQ1kEeD9yOSsnuJKtbD1tjfnL0HM2J4I7bzUWGppw0ptTHHWKTL1HJqwXna8CjHr3AS4eaHs0%5Cu003d%7Crm%5Cu003dTaGrFqm4fsbL7t%2F5zC7lg4Hw5UE6xQl6eD0b%2Bx7ur94z3oTvejtiURkRDTOm7xVx8BPqXXlUOSkFFcgvExGklZGRaIjTDkN5HnARZzs6J4ZuyqCVLKskdI1dlaizf4wwPUAjtrVNrlJZN7vc0G9nGC2lXVW8JZ1RhBIV7AKTe1Y%5Cu003d%7Cro%5Cu003dKiJtS6XKX5cGKLgdDGt%2FL%2B%2FpYC7V1q14%2FLV5ldNu2vm2ONhmNesN5QDl%2FhZ7pFLStWIKy0lxnQJiAVktaeTjgH9%2B0GAfrXA%2Bf7PhYQBIp8RLa68ryPLFDyOlg5g%2F3T%2Bsmgr3U2EWRRD8DFRpPH%2F1TQWYijVIeGCI8ZJajNpblzo%5Cu003d%7Crov%5Cu003dbzJ%2FDNyqOXPCoadHA093QTgCizw%2Bh517o4jWXMz%2FeWpvFLMBonwUZK5%2BYHo9vz8wUSJKpjZPPN69qCv60QzoCFtjDAgQADlJAJjH9UPJ%2FZutL7sLKXMxVOC%2FK%2FQ9TReFR9cEeaXPYip7t66vzL4k7Mr7v4ix%2BBUoApOoPvk5n9E%5Cu003d%7Cal%5Cu003dqWL3b0W8ODqqHs0iYnVBcXd89%2FZGI6INbBb163afErCajvaBWMh9XQ3xbf6D3jZzEu9GyMaMn%2B4jKRqpKmTA4O5hvpKlmKrj0smLfnNz2Nu3gACqk2OtLqjBN6d47dTGcW8vBT1b8Ybh%2F%2F7HRSOIxZjK1jsmwVsLUvyyZzF7kjU%5Cu003d%22%2C%22mdn%22%3A%22poyLbAO2O1AWI%2FrKsw4Hl1Y6rPfEHlSfAtM8DlpyvjoeQK0ovSrhWhN%2FkB3zGYwRyLwzzzv19RB6x4Kb9psc4zdwSFSPipseRcxT9YfLaunfyVf6R%2FC5A3mDvJvfdfYnvTV%2FTlyUuOTwnkwedYuBl87kwbp8rcdfxyIjKCJzmLs%5Cu003d%22%7D%7D")

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


