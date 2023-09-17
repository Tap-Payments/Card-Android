package company.tap.tapcardformkit.open.web_wrapper.scanner_activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cards.pay.paycardsrecognizer.sdk.Card
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent
import cards.pay.paycardsrecognizer.sdk.ui.InlineViewCallback
import cards.pay.paycardsrecognizer.sdk.ui.InlineViewFragment
import company.tap.cardscanner.TapCard
import company.tap.cardscanner.TapScannerCallback
import company.tap.cardscanner.TapTextRecognitionCallBack
import company.tap.cardscanner.TapTextRecognitionML
import java.lang.Exception

private const val SCAN_CARD_ID = 101

class ScannerActivity : AppCompatActivity(), TapTextRecognitionCallBack, TapScannerCallback,
    InlineViewCallback {
    lateinit var webView: WebView
    private var textRecognitionML: TapTextRecognitionML? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(company.tap.tapcardformkit.R.layout.scanner_activity)

//
//        val chooseImageIntent = Intent(
//            this,
//            CameraActivity::class.java
//        )
//        startActivity(chooseImageIntent)
        val intent = ScanCardIntent.Builder(this).build()
        startActivityForResult(intent, SCAN_CARD_ID)

//        supportFragmentManager
//            .beginTransaction()
//            .replace(company.tap.tapcardformkit.R.id.inline_container, InlineViewFragment())
//            .commit()
//        supportFragmentManager
//            .beginTransaction()
//            .replace(company.tap.tapcardformkit.R.id.inline_container, CameraFragment())
//            .commit()
//        textRecognitionML = TapTextRecognitionML(this)
//        textRecognitionML?.addTapScannerCallback(this)
//       textRecognitionML?.setFrameColor(Color.BLUE)

    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SCAN_CARD_ID -> if (resultCode == Activity.RESULT_OK) {
                val card = data!!.getParcelableExtra<Card>(ScanCardIntent.RESULT_PAYCARDS_CARD)
                if (card != null) {
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }



    override fun onRecognitionSuccess(card: TapCard?) {
        Toast.makeText(this@ScannerActivity, card?.cardNumber.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onRecognitionFailure(error: String?) {
        Toast.makeText(this@ScannerActivity, error?.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onReadSuccess(card: TapCard?) {
        Toast.makeText(this@ScannerActivity, card?.cardNumber.toString(), Toast.LENGTH_LONG).show()

    }

    override fun onReadFailure(error: String?) {
        Toast.makeText(this@ScannerActivity, error?.toString(), Toast.LENGTH_LONG).show()

    }

    override fun onScanCardFailed(e: Exception?) {
        Toast.makeText(this@ScannerActivity, e?.message.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onScanCardFinished(card: Card?, cardImage: ByteArray?) {
        Toast.makeText(this@ScannerActivity, card?.cardNumber.toString(), Toast.LENGTH_LONG).show()

    }

}
