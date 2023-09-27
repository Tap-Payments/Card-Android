/*
 * Created By AhlaamK 8/5/22, 7:13 PM
 * Copyright (c) 2022    Tap Payments.
 * All rights reserved.
 */

package com.example.tapcardwebsdk.select_choice

import Authentication
import Invoice
import Post
import Refrence
import Scope
import TapAuthentication
import TapTheme
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.SwitchCompat
import com.example.tapcardwebsdk.R
import com.example.tapcardwebsdk.main_activity.MainActivity
import com.example.tapcardwebsdk.select_choice.adapter.CustomAdapter
import com.example.tapcardwebsdk.select_choice.adapter.DataModel
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.tap.commondatamodels.cardBrands.CardBrand
import company.tap.cardformkit.activities.MerchantDialog
import company.tap.tapcardformkit.getRandomNumbers
import company.tap.tapcardformkit.getRandomTrx
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.tapuilibrary.themekit.ThemeManager

class SelectChoiceActivity : AppCompatActivity() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var themeRadioGroup: RadioGroup
    private lateinit var cardEges:RadioGroup
    private lateinit var cardDirection: RadioGroup
    private lateinit var darkRadioButton: AppCompatRadioButton
    private lateinit var lightRadioButton: AppCompatRadioButton

    private lateinit var switchCardHolderButton: SwitchCompat
    private lateinit var switchCardBrandButton: SwitchCompat
    private lateinit var switchEditHolderNameButton: SwitchCompat
    private lateinit var switchScannerButton: SwitchCompat
    private lateinit var switchNFCButton: SwitchCompat
    private lateinit var switchDefaultCardHolderButton: SwitchCompat
    private lateinit var switchShowHeaderView: SwitchCompat
    private lateinit var switchShowLoadingState: SwitchCompat
    private lateinit var switchShowPowerdBy: SwitchCompat
    private lateinit var switchShowSaved: SwitchCompat

    private lateinit var editTextCardHolderName: TextInputEditText

    private lateinit var selectedUserLanguage: String
    private lateinit var selectedUserTheme: String
    private lateinit var selectedCardEdge: String
    private lateinit var selectedCardDirection: String

    private lateinit var selectedUserAnim: String
    private var selectedCardHolderName: Boolean = false
    private var setHeaderView: Boolean = false
    private var setdefaultHolderName: Boolean = false
    private var editDefaultHolderName: Boolean = false
    private var selectedCardBrand: Boolean = false
    private var showHideScanner: Boolean = false
    private var showHideNFC: Boolean = false
    private var showLoadingState: Boolean = true
    private var showPowerdBy: Boolean = false
    private var showSavedSwitch: Boolean = false

    private var defaultCardHolderName: String? = null
    var toggleButtonGroup: MaterialButtonToggleGroup? = null
    var toggleButtonGroup2: MaterialButtonToggleGroup? = null
    var toggleAuthentication: MaterialButtonToggleGroup? = null
    var toggleOperation: MaterialButtonToggleGroup? = null

    var toggleButtonGroup3: MaterialButtonToggleGroup? = null
    var selectedCurrency: String? = null
    var selectedCardType: String? = null
    var selectAuthenticationType: Scope? = null
//    var selectedOperation: "SDKMODE"? = null

    var defaultScannerBorder: Int? = Color.WHITE
    private var dataModel: ArrayList<DataModel>? = null
    private var dataModelChecked: ArrayList<String>? = arrayListOf()

    // Declaring the elements from the main layout file
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_choice)
        initializeViews()
        saveTheSelectedValue()
    }


    private fun initializeViews() {
        radioGroup = findViewById(R.id.radioGroup)
        themeRadioGroup = findViewById(R.id.radioGroup2)
        cardEges = findViewById(R.id.card_edges)
        cardDirection = findViewById(R.id.card_direction)
        findViewById<TextView>(R.id.merchant_tv).setOnClickListener {
            MerchantDialog(this).show()
        }
//        findViewById<TextView>(R.id.transaction).setOnClickListener {
//            TransactionDialog(this).show()
//        }
      //  radioGroup7 = findViewById(R.id.radioGroup7)
        darkRadioButton = findViewById<AppCompatRadioButton>(R.id.theme_dark)
        lightRadioButton = findViewById<AppCompatRadioButton>(R.id.theme_light)
        listView = findViewById<View>(R.id.list_view_1) as ListView
        dataModel = ArrayList<DataModel>()
        dataModel!!.add(DataModel(CardBrand.mada.name, true))
        dataModel!!.add(DataModel(CardBrand.visa.name, true))
        dataModel!!.add(DataModel(CardBrand.masterCard.name, true))
        dataModel!!.add(DataModel("AMEX", true))
        dataModel!!.add(DataModel(CardBrand.omanNet.name, true))
        dataModel!!.add(DataModel(CardBrand.meeza.name, true))
        adapter = CustomAdapter(dataModel!!, applicationContext)
        listView.adapter = adapter



        switchCardHolderButton = findViewById(R.id.switchButton_cardHolderName)
        switchCardBrandButton = findViewById(R.id.switchButton_cardBrands)
        //switchEditHolderNameButton = findViewById(R.id.switchButton_editCardHolderName)
        switchScannerButton = findViewById(R.id.switchButton_showHidescan)
        switchNFCButton = findViewById(R.id.switchButton_showHidenfc)
      //  switchShowHeaderView = findViewById(R.id.switchButton_showHeaderWebview)
        switchShowLoadingState = findViewById(R.id.switchButton_showLoadingView)
        switchShowPowerdBy = findViewById(R.id.switchButton_showPowerdBy)
        switchShowSaved = findViewById(R.id.switchButton_showSavedBy)

        // switchDefaultCardHolderButton = findViewById(R.id.switchButton_defaultCardHolderName)
        toggleButtonGroup = findViewById(R.id.toggleButtonGroup)
        toggleButtonGroup2 = findViewById(R.id.toggleButtonGroup2)
        toggleAuthentication = findViewById(R.id.toggle_authentication)
        toggleOperation = findViewById(R.id.toggleOperation)

        toggleButtonGroup3 = findViewById(R.id.toggleButtonGroup3)
      //  editTextCardHolderName = findViewById(R.id.editTextCardHolderName)


        selectedCurrency = "KWD"
        toggleButtonGroup?.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.btnKuwait -> selectedCurrency = "KWD"
                    R.id.btnUae -> selectedCurrency = "AED"
                    R.id.btnSaudi -> selectedCurrency = "SAR"
                    R.id.btnOman -> selectedCurrency = "OMR"

                }
            } else {
                if (toggleButtonGroup.checkedButtonId == View.NO_ID) {
                    showToast("Nothing Selected")
                }
            }
        }
        selectedCardType = CardType.ALL.name
        toggleButtonGroup2?.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.btnCredit -> selectedCardType = CardType.CREDIT.name
                    R.id.btnDebit -> selectedCardType = CardType.DEBIT.name
                    R.id.btnAll -> selectedCardType = CardType.ALL.name
                }
            } else {
                if (toggleButtonGroup2!!.checkedButtonId == View.NO_ID) {
                    showToast("Nothing Selected")
                }
            }
        }

        selectAuthenticationType = Scope.Authenticate
        toggleAuthentication?.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.btn_token -> selectAuthenticationType = Scope.Token
                    R.id.btn_auth -> selectAuthenticationType =
                        Scope.Authenticate
                }
            } else {
                if (toggleAuthentication!!.checkedButtonId == View.NO_ID) {
                    showToast("Nothing Selected")
                }
            }
        }

     //   selectedOperation = "SDKMODE.SANDBOX"
        toggleOperation?.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                 //   R.id.btnSandBox -> selectedOperation = "SDKMODE.SANDBOX"
                  //  R.id.btnProduction -> selectedOperation = "SDKMODE.PRODUCTION"
                }
            } else {
                if (toggleAuthentication!!.checkedButtonId == View.NO_ID) {
                    showToast("Nothing Selected")
                }
            }
        }
        toggleButtonGroup3?.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.btnWhite -> defaultScannerBorder = Color.WHITE
                    R.id.btnRed -> defaultScannerBorder = Color.RED
                    R.id.btnGreen -> defaultScannerBorder = Color.GREEN
                    R.id.btnBlue -> defaultScannerBorder = Color.BLUE

                }

            } else {
                if (toggleButtonGroup3!!.checkedButtonId == View.NO_ID) {
                    showToast("Nothing Selected")
                }
            }
        }
    }

    private fun saveTheSelectedValue() {

        if (radioGroup.checkedRadioButtonId == R.id.lang_english) {
            selectedUserLanguage = "en"
        }
        if (radioGroup.checkedRadioButtonId == R.id.lang_arabic) {
            selectedUserLanguage = "ar"
        }
        radioGroup.setOnCheckedChangeListener { _, _ ->
            // This will get the radiobutton that has changed in its check state .
            if (radioGroup.checkedRadioButtonId == R.id.lang_english) {
                selectedUserLanguage = "en"
            }
            if (radioGroup.checkedRadioButtonId == R.id.lang_arabic) {
                selectedUserLanguage = "ar"
            }
        }
        val nightModeFlags: Int =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                darkRadioButton.isChecked = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                lightRadioButton.isChecked = true

            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                lightRadioButton.isChecked = true

            }
        }
        if (themeRadioGroup.checkedRadioButtonId == R.id.theme_dark) {
            selectedUserTheme = TapTheme.dark.name
            ThemeManager.currentThemeName = TapTheme.dark.name
        }
        if (themeRadioGroup.checkedRadioButtonId == R.id.theme_light) {
            selectedUserTheme = TapTheme.light.name
            ThemeManager.currentThemeName = TapTheme.light.name
        }
        if (themeRadioGroup.checkedRadioButtonId == R.id.theme_dynamic) {
            selectedUserTheme = TapTheme.dynamic.name
           // ThemeManager.currentThemeName = ThemeMode.light.name
        }

        themeRadioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->

            if (themeRadioGroup.checkedRadioButtonId == R.id.theme_dark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                selectedUserTheme = TapTheme.dark.name
                ThemeManager.currentThemeName = TapTheme.dark.name

            }
            if (themeRadioGroup.checkedRadioButtonId == R.id.theme_light) {
                selectedUserTheme = TapTheme.light.name
                ThemeManager.currentThemeName = TapTheme.light.name
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
            if (themeRadioGroup.checkedRadioButtonId == R.id.theme_dynamic) {
                selectedUserTheme = TapTheme.dynamic.name
            //    ThemeManager.currentThemeName = ThemeMode.light.name
              //  AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            }
        })
        if (cardEges.checkedRadioButtonId == R.id.curved) {
            selectedCardEdge = TapCardEdges.curved.name
        }
        if (cardEges.checkedRadioButtonId == R.id.straight) {
            selectedCardEdge = TapCardEdges.flat.name
        }
        cardEges.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (cardEges.checkedRadioButtonId == R.id.curved) {
                selectedCardEdge = TapCardEdges.curved.name
            }
            if (cardEges.checkedRadioButtonId == R.id.straight) {
                selectedCardEdge = TapCardEdges.flat.name
            }
        })

        if (cardDirection.checkedRadioButtonId == R.id.leftToRight) {
            selectedCardDirection = TapCardDirections.ltr.name
        }
        if (cardDirection.checkedRadioButtonId == R.id.rightToLeft) {
            selectedCardDirection = TapCardDirections.dynamic.name
        }
        cardDirection.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if (cardDirection.checkedRadioButtonId == R.id.leftToRight) {
                selectedCardDirection = TapCardDirections.ltr.name
            }
            if (cardDirection.checkedRadioButtonId == R.id.rightToLeft) {
                selectedCardDirection = TapCardDirections.dynamic.name
            }
        })
//
//        if (radioGroup7.checkedRadioButtonId == R.id.anim_zoom) {
//            selectedUserAnim = "zoom"
//        }
//        if (radioGroup7.checkedRadioButtonId == R.id.anim_bottom) {
//            selectedUserAnim = "bottom"
//        }
//        radioGroup7.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
//
//            if (radioGroup7.checkedRadioButtonId == R.id.anim_zoom) {
//                selectedUserAnim = "zoom"
//            }
//            if (radioGroup7.checkedRadioButtonId == R.id.anim_bottom) {
//                selectedUserAnim = "bottom"
//            }
//        })

        switchCardHolderButton.setOnCheckedChangeListener { _, isChecked ->
            selectedCardHolderName = isChecked
        }
//        switchShowHeaderView.setOnCheckedChangeListener { _, isChecked ->
//            setHeaderView = isChecked
//        }
        switchShowLoadingState.setOnCheckedChangeListener { _, isChecked ->
            showLoadingState = isChecked
        }
        switchShowPowerdBy.setOnCheckedChangeListener { _, isChecked ->
            showPowerdBy = isChecked
        }
        switchShowSaved.setOnCheckedChangeListener { _, isChecked ->
            showSavedSwitch = isChecked

        }

//        switchEditHolderNameButton.setOnCheckedChangeListener { _, isChecked ->
//            editDefaultHolderName = isChecked
//        }

//        switchDefaultCardHolderButton.setOnCheckedChangeListener { _, isChecked ->
//            setdefaultHolderName = isChecked
//
//            if (isChecked) {
//                editTextCardHolderName.visibility = View.VISIBLE
//
//
//            } else editTextCardHolderName.visibility = View.GONE
//
//        }

        selectedCardBrand = switchCardBrandButton.isChecked
        switchCardBrandButton.setOnCheckedChangeListener { _, isChecked ->
            selectedCardBrand = isChecked
        }
        showHideScanner = switchScannerButton.isChecked
        switchScannerButton.setOnCheckedChangeListener { _, isChecked ->
            showHideScanner = isChecked
        }

        showHideNFC = switchNFCButton.isChecked
        switchNFCButton.setOnCheckedChangeListener { _, isChecked ->
            showHideNFC = isChecked
        }

//        editTextCardHolderName.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                // editTextCardHolderName.text = s
//                defaultCardHolderName = s.toString()
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//
//        })
    }

    fun startTokenizationactivity(view: View) {
        if (::selectedUserLanguage.isInitialized && ::selectedUserTheme.isInitialized) {
            println("defaultCardHolderName is" + defaultCardHolderName)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("languageSelected", selectedUserLanguage)
            intent.putExtra("themeSelected", selectedUserTheme)
            intent.putExtra("selectedCardHolderName", selectedCardHolderName)
            intent.putExtra("selectedCardBrand", selectedCardBrand)
            intent.putExtra("showHideScanner", showHideScanner)
            intent.putExtra("showHideNFC", showHideNFC)
            intent.putExtra("selectedCurrency", selectedCurrency)
            intent.putExtra("selectedCardType", selectedCardType)
            intent.putExtra("setdefaultHolderName", setdefaultHolderName)
            intent.putExtra("defaultCardHolderName", defaultCardHolderName)
            intent.putExtra("defaultColorScanner", defaultScannerBorder)
//            intent.putExtra("animSelected", selectedUserAnim)
            intent.putExtra("setHeaderView", setHeaderView)
            intent.putExtra("showLoadingState", showLoadingState)
            intent.putExtra("editDefaultHolderName", editDefaultHolderName)
            intent.putExtra("selectedCardEdge", selectedCardEdge)
            intent.putExtra("selectedCardDirection", selectedCardDirection)
            /**
             * new configs
             */
            intent.putExtra("orderId", findViewById<EditText>(R.id.order_id).text.toString())
            intent.putExtra("orderDescription", findViewById<EditText>(R.id.order_description).text.toString())
            intent.putExtra("transactionRefrence",getRandomTrx())
            intent.putExtra("invoiceId", findViewById<EditText>(R.id.invoice_id).text.toString())
            intent.putExtra("postUrl", findViewById<EditText>(R.id.post_url).text.toString())

            /**
             * added newly
             */
            Log.e("cardBrands", dataModelChecked.toString())
            intent.putExtra("amount", findViewById<EditText>(R.id.amount).text.toString())
            intent.putStringArrayListExtra("cardBrands", dataModelChecked)
            intent.putExtra("authentication", selectAuthenticationType)
          //  intent.putExtra("operation", selectedOperation)
            dataModel?.forEachIndexed { index, dataModel ->
                if (dataModel.checked) {
                    Log.e(
                        "cardBrands",
                        dataModel.name.toString() + " " + dataModel.checked.toString()
                    )

                    dataModelChecked?.add(dataModel.name.toString())
                }
            }
            intent.putExtra("showPowerdBy", showPowerdBy)
            intent.putExtra("showSaveSwitch", showSavedSwitch)

            intent.putExtra("sandboxKey", findViewById<EditText>(R.id.et_sandbox_key).text.toString())
            intent.putExtra("productionKey", findViewById<EditText>(R.id.et_production_key).text.toString())
            intent.putExtra("merchantId", findViewById<EditText>(R.id.merchant_id).text.toString())



            finish()
            startActivity(intent)


        } else {
            Toast.makeText(this, "Please make your selection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
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

//            DataConfiguration.setTapAuthentication(
//                TapAuthentication(
//                    description =  "description",
//                    reference = Refrence(
//                        transaction = getRandomTrx(),
//                        order = getRandomNumbers(17)
//                    ),
//                    invoice = Invoice(
//                        id = ""
//
//                    ),
//                    authentication = Authentication(
//                        channel = "PAYER_BROWSER",
//                        purpose = "PAYMENT_TRANSACTION",
//                    ),
//                    post = Post("")
//                )
//            )
            Log.e("data",DataConfiguration.authenticationExample.toString())
            true

        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}