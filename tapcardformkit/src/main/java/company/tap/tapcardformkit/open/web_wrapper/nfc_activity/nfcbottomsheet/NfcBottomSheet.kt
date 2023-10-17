package company.tap.tapcardformkit.open.web_wrapper.nfc_activity.nfcbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import company.tap.tapcardformkit.R
import company.tap.tapcardformkit.doAfterSpecificTime
import company.tap.tapcardformkit.getDimensionsInDp
import company.tap.tapuilibrary.uikit.fragment.NFCFragment
import company.tap.tapuilibrary.uikit.views.TapBrandView

class NfcBottomSheet : BottomSheetDialogFragment() {

//    private val behavior by lazy { (dialog as BottomSheetDialog).behavior }


    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.nfc_bottom_sheet, null)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tapBrandView = view.findViewById<TapBrandView>(R.id.tab_brand_view_nfc)
        (dialog as BottomSheetDialog).behavior.peekHeight = requireContext().getDimensionsInDp(900)
        (dialog as BottomSheetDialog).behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.nfc_view, NFCFragment())
        transaction.addToBackStack(null)
        transaction.commit()


        tapBrandView.backButtonLinearLayout.setOnClickListener {
            this.dismiss()
            requireActivity().finish()

        }
        this.dialog?.setOnDismissListener {
            doAfterSpecificTime {
                requireActivity().finish()
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.dialog?.window?.attributes?.windowAnimations = R.style.DialogAnimations
//        behavior.isFitToContents = false
//        behavior.expandedOffset = 100

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogFragment)

    }



    override fun getTheme(): Int = R.style.CustomBottomSheetDialogFragment




}