package company.tap.tapcardformkit.open.web_wrapper.nfc_activity


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import company.tap.nfcreader.open.reader.TapEmvCard
import company.tap.nfcreader.open.reader.TapNfcCardReader
import company.tap.nfcreader.open.utils.TapCardUtils
import company.tap.nfcreader.open.utils.TapNfcUtils
import company.tap.tapcardformkit.R
import company.tap.tapuilibrary.uikit.fragment.NFCFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class NFCLaunchActivity : AppCompatActivity() {
    private lateinit var tapNfcCardReader: TapNfcCardReader
    private var cardReadDisposable = Disposables.empty()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nfclaunch)

        tapNfcCardReader = TapNfcCardReader(this)

        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.nfccFragment, NFCFragment())
        transaction.addToBackStack(null)
        transaction.commit()

    /*    cardReadDisposable = tapNfcCardReader!!
            .readCardRx2(intent)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::showCardInfo,
                Consumer<Throwable> { throwable: Throwable ->
                    displayError(
                        throwable.message
                    )
                })*/

}

override fun onNewIntent(intent: Intent?) {
    // TODO Auto-generated method stub
    // if this activity is in stack , this mwthod will be called
    handleNFCResult(intent)
}



fun handleNFCResult(intent: Intent?) {
    if (tapNfcCardReader?.isSuitableIntent(intent)== true) {
        cardReadDisposable = tapNfcCardReader
            .readCardRx2(intent)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ emvCard: TapEmvCard? ->
                if (emvCard != null) {
                    // tapCheckoutFragment.viewModel?.handleNFCScannedResult(emvCard)
                    println("emvCard$emvCard")
                }
            },
                { throwable -> throwable.message?.let { println("error is nfc" + throwable.printStackTrace()) } })
    }

}

private fun displayError(message: String?) {
    Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show()
}

override fun onResume() {
    if (TapNfcUtils.isNfcAvailable(this)) {
        if (TapNfcUtils.isNfcEnabled(this)) {
            tapNfcCardReader?.enableDispatch();
            //  scancardContent.setVisibility(View.VISIBLE);
        }
        // else
        // enableNFC();
    } else {
//            scancardContent.setVisibility(View.GONE);
//            cardreadContent.setVisibility(View.GONE);
//            noNfcText.setVisibility(View.VISIBLE);
    }
    super.onResume();
}

override fun onPause() {
    super.onPause()
    //if (tapCheckoutFragment.isNfcOpened) {
    cardReadDisposable.dispose()
    tapNfcCardReader?.disableDispatch()
    //  }
//changed above condition ELSE of simply finish to check gpay and finish , otherwise it ws not calling onactivity result

}

private fun showCardInfo(emvCard: TapEmvCard) {
    val text: String = TextUtils.join(
        "\n", arrayOf<Any>(
            TapCardUtils.formatCardNumber(emvCard.cardNumber, emvCard.type),
            DateFormat.format("M/y", emvCard.expireDate),
            "---",
            "Bank info (probably): ",
            emvCard.atrDescription,
            "---",
            emvCard.toString().replace(", ", ",\n")
        )
    )
    Log.e("showCardInfo:", text)

}

}