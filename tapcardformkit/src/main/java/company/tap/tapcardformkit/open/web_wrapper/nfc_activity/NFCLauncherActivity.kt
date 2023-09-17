package company.tap.tapcardformkit.open.web_wrapper.nfc_activity

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.functions.Consumer


class NFCLauncherActivity : AppCompatActivity() {
    private var tapNfcCardReader: TapNfcCardReader? = null
    private var cardReadDisposable = Disposables.empty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tapNfcCardReader = TapNfcCardReader(this);
        cardReadDisposable = tapNfcCardReader!!
            .readCardRx2(intent)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                this::showCardInfo,
                Consumer<Throwable> { throwable: Throwable ->
                    displayError(
                        throwable.message
                    )
                })

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
            //else
            //     enableNFC();
        } else {
//            scancardContent.setVisibility(View.GONE);
//            cardreadContent.setVisibility(View.GONE);
//            noNfcText.setVisibility(View.VISIBLE);
        }
        super.onResume();
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