package company.tap.tapcardformkit.open.web_wrapper

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.open.web_wrapper.model.ThreeDsResponse

const val chunkSize = 2048
const val keyValueForAuthPayer = "auth_payer"

class ThreeDsWebViewActivity : AppCompatActivity() {
    lateinit var threeDsBottomsheet: BottomSheetDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_three_ds_web_view)

        threeDsBottomsheet = ThreeDsBottomSheetFragment()
        threeDsBottomsheet.show(supportFragmentManager,"")
    }


    }
