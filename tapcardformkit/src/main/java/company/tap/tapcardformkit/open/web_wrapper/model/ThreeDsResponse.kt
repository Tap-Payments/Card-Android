package company.tap.tapcardformkit.open.web_wrapper.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ThreeDsResponse(var threeDsUrl: String, var redirectUrl: String, var keyword: String,var powered:Boolean) :
    Parcelable