package company.tap.tapcardformkit.open.web_wrapper.presentation.threeDsWebView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.doAfterSpecificTime
import company.tap.tapcardformkit.getDeviceSpecs
import company.tap.tapcardformkit.open.web_wrapper.TapCardKit
import company.tap.tapuilibrary.uikit.views.TapBrandView

class ThreeDsBottomSheetFragment (var webView: WebView?, var onCancel:()->Unit): BottomSheetDialogFragment() {


    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog, null)
        val linearLayout= view.findViewById<LinearLayout>(R.id.webLinear)
        linearLayout.addView(webView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tapBrandView = view.findViewById<TapBrandView>(R.id.tab_brand_view)

        try {
            val powerd  = TapCardKit.threeDsResponse.powered
            when(powerd){
                false ->tapBrandView.poweredByImage.visibility = View.INVISIBLE
                else -> {}
            }
        }catch (e:java.lang.Exception){
            Log.e("excption",e.toString())
        }


        ( dialog as BottomSheetDialog).behavior.isFitToContents = false
        ( dialog as BottomSheetDialog).behavior.peekHeight = (context?.getDeviceSpecs()?.first?: 950) - 450

        this.dialog?.setOnDismissListener {
            if (isAdded){
                doAfterSpecificTime {
                    requireActivity().finish()
                }
            }

        }
        isCancelable = false
        tapBrandView.backButtonLinearLayout.setOnClickListener {
            this.dialog?.dismiss()
            onCancel.invoke()

        }



    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimations
        setStyle(STYLE_NORMAL,R.style.CustomBottomSheetDialogFragment)

    }






    override fun getTheme(): Int = R.style.CustomBottomSheetDialogFragment




}