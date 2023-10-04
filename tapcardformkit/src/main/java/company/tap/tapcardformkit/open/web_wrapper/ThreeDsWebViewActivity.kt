package company.tap.tapcardformkit.open.web_wrapper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.open.DataConfiguration
import company.tap.taplocalizationkit.LocalizationManager
import java.util.*

const val chunkSize = 2048
const val keyValueForAuthPayer = "auth_payer"

class ThreeDsWebViewActivity : AppCompatActivity() {
    lateinit var threeDsBottomsheet: BottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_ds_web_view)
        LocalizationManager.setLocale(this, Locale(DataConfiguration.lanuage.toString()))
        TapCardKit.alreadyEvaluated = false

        threeDsBottomsheet = ThreeDsBottomSheetFragment()
        threeDsBottomsheet.show(supportFragmentManager,"")
    }


    }
