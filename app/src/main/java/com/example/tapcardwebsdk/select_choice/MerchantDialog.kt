package company.tap.cardformkit.activities


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.tapcardwebsdk.R
import com.tap.commondatamodels.Contact
import com.tap.commondatamodels.Customer
import com.tap.commondatamodels.Name
import com.tap.commondatamodels.Phone
import com.tap.commondatamodels.TapLocal
import company.tap.tapcardformkit.open.CardDataConfiguration

class MerchantDialog(var contexts: Context) : Dialog(contexts) {
    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_dialog_)
        setOnDismissListener {
            CardDataConfiguration.setCustomer(
                Customer(
                    //id = findViewById<EditText>(R.id.et_customer_id).text.toString(),
                    nameOnCard = findViewById<EditText>(R.id.et_nameOnCard).text.toString(),
                    editable = true,
                    contact = Contact(
                        email = findViewById<EditText>(R.id.et_email).text.toString(),
                        phone = Phone(
                            countryCode = findViewById<EditText>(R.id.et_country_code).text.toString(),
                            number = findViewById<EditText>(R.id.et_number).text.toString()
                        )
                    ),
                    name = mutableListOf<Name>(
                        Name(
                            lang = TapLocal.valueOf("en"),
                            first = findViewById<EditText>(R.id.et_first_name).text.toString(),
                            last = findViewById<EditText>(R.id.et_last_name).text.toString(),
                            middle = findViewById<EditText>(R.id.et_middle_name).text.toString()
                        )
                    )
                )
            )

        }

        findViewById<Button>(R.id.ok_button).setOnClickListener {
            dismiss()

        }
    }

}
