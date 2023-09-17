package com.example.tapcardwebsdk.select_choice

import Authentication
import Invoice
import Post
import Refrence
import TapAuthentication
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.tapcardwebsdk.R
import company.tap.tapcardformkit.getRandomNumbers
import company.tap.tapcardformkit.open.DataConfiguration
import java.util.*

const val prefix = "tck_LV02G"

class TransactionDialog(var contexts: Context) : Dialog(contexts) {
    init {
        setCancelable(true)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog_authentication)
        val allowedChars = ('A'..'Z') + ('0'..'9')

        findViewById<EditText>(R.id.et_ref_order).setText(getRandomNumbers(17))
//        findViewById<EditText>(R.id.et_ref_transaction).setText(
//            "${prefix}${getRandomNumbers(10)}X${getRandomNumbers(9)}"
//        )

        setOnDismissListener {
            DataConfiguration.setTapAuthentication(
                TapAuthentication(
                    description = findViewById<EditText>(R.id.et_trans_desc_id).text.toString(),
                    reference = Refrence(
                        transaction = findViewById<EditText>(R.id.et_ref_transaction).text.toString(),
                        order = findViewById<EditText>(R.id.et_ref_order).text.toString()
                    ),
                    invoice = Invoice(
                        id = findViewById<EditText>(R.id.et_invoice).text.toString()

                    ),
                    authentication = Authentication(
                        channel = findViewById<EditText>(R.id.et_auth_channel).text.toString(),
                        purpose = findViewById<EditText>(R.id.et_auth_purpose).text.toString(),
                    ),
                    post = Post(findViewById<EditText>(R.id.et_post_url).text.toString())
                )
            )

        }

        findViewById<Button>(R.id.ok_button).setOnClickListener {
            dismiss()

        }
    }

}
